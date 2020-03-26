package cn.hikyson.godeye.core.internal.modules.viewcanary;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.VisibleForTesting;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.helper.SimpleActivityLifecycleCallbacks;
import cn.hikyson.godeye.core.internal.modules.viewcanary.levenshtein.ViewIdWithSize;
import cn.hikyson.godeye.core.internal.modules.viewcanary.levenshtein.ViewWithSizeInsDelInterface;
import cn.hikyson.godeye.core.internal.modules.viewcanary.levenshtein.ViewWithSizeSubstitutionInterface;
import cn.hikyson.godeye.core.internal.modules.viewcanary.levenshtein.WeightedLevenshtein;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;

class ViewCanaryInternal {
    private SimpleActivityLifecycleCallbacks callbacks;
    private static final String VIEW_CANARY_HANDLER = "godeye-viewcanary";
    private static final double THRESHOLD_LAYOUT_CHANGE = 0.5;
    private static final long INSPECT_DELAY_TIME_MILLIS = 800;

    void start(ViewCanary viewCanary, ViewCanaryConfig config) {
        Handler handler = ThreadUtil.createIfNotExistHandler(VIEW_CANARY_HANDLER);
        callbacks = new SimpleActivityLifecycleCallbacks() {

            private Map<Activity, ViewTreeObserver.OnGlobalLayoutListener> mOnGlobalLayoutListenerMap = new HashMap<>();
            private Map<Activity, List<List<ViewIdWithSize>>> mRecentLayoutListRecords = new HashMap<>();

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                mRecentLayoutListRecords.put(activity, new ArrayList<>());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                mRecentLayoutListRecords.remove(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                super.onActivityResumed(activity);
                ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView();
                Runnable callback = inspectInner(new WeakReference<>(activity), viewCanary, config, mRecentLayoutListRecords);
                ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = () -> {
                    handler.removeCallbacks(callback);
                    handler.postDelayed(callback, INSPECT_DELAY_TIME_MILLIS);
                };
                mOnGlobalLayoutListenerMap.put(activity, onGlobalLayoutListener);
                parent.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                super.onActivityPaused(activity);
                ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = mOnGlobalLayoutListenerMap.remove(activity);
                ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView();
                if (onGlobalLayoutListener != null) {
                    parent.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
                }
            }
        };
        GodEye.instance().getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    void stop(Application application) {
        if (callbacks == null) {
            return;
        }
        application.unregisterActivityLifecycleCallbacks(callbacks);
        callbacks = null;
        ThreadUtil.destoryHandler(VIEW_CANARY_HANDLER);
    }

    @VisibleForTesting
    Runnable inspectInner(WeakReference<Activity> activity, ViewCanary viewCanary, ViewCanaryConfig config, Map<Activity, List<List<ViewIdWithSize>>> recentLayoutListRecords) {
        return () -> {
            try {
                Activity p = activity.get();
                if (p != null) {
                    ViewGroup parent = (ViewGroup) p.getWindow().getDecorView();
                    inspect(p, parent, viewCanary, config, recentLayoutListRecords);
                }
            } catch (Throwable e) {
                L.e(e);
            }
        };
    }

    private void inspect(Activity activity, ViewGroup root, ViewCanary viewCanary, ViewCanaryConfig config, Map<Activity, List<List<ViewIdWithSize>>> recentLayoutListRecords) {
        long startTime = System.currentTimeMillis();
        // check whether layout is changed or not
        List<List<ViewIdWithSize>> records = recentLayoutListRecords.get(activity);
        if (records == null) {// activity has been destroyed if null
            return;
        }
        // find all views
        Map<View, Integer> depthMap = new HashMap<>();
        List<View> allViews = new ArrayList<>();
        // allViews should include decor view for inspecting overdraw. However, there is no need for depth inspection to include decor view
        allViews.add(root);
        recursiveLoopChildren(root, depthMap, allViews);
        List<ViewIdWithSize> layoutEigenvalue = getLayoutEigenvalue(root, allViews);
        boolean allNotSimilar = allNotSimilarByMeasureDistanceLayoutEigenvalueWithRecords(records, layoutEigenvalue);
        if (!allNotSimilar) {// layout is similar to before, only produce view issue when layout changed a lot and not similar to recent
//            L.d("ViewCanary interrupt because %s's layout is similar to someone in records, cost %sms.", activity.getClass().getSimpleName(), (System.currentTimeMillis() - startTime));
            return;
        }
        // layout is changed, then check view issues
        Map<Rect, Set<Object>> overDrawMap = checkOverDraw(allViews);
        ViewIssueInfo info = new ViewIssueInfo();
        int[] resolution = getResolution();
        info.activityName = activity.getClass().getName();
        info.timestamp = System.currentTimeMillis();
        info.maxDepth = config.maxDepth();
        info.screenWidth = resolution[0];
        info.screenHeight = resolution[1];
        for (Map.Entry<View, Integer> entry : depthMap.entrySet()) {
            info.views.add(getViewInfo(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<Rect, Set<Object>> entry : overDrawMap.entrySet()) {
            ViewIssueInfo.OverDrawArea overDrawArea = new ViewIssueInfo.OverDrawArea();
            overDrawArea.rect = entry.getKey();
            overDrawArea.overDrawTimes = getOverDrawTimes(entry.getValue()) - 1;
            info.overDrawAreas.add(overDrawArea);
        }
        records.add(layoutEigenvalue);
        viewCanary.produce(info);
        L.d("ViewCanary inspect %s complete, cost %sms.", activity.getClass().getSimpleName(), (System.currentTimeMillis() - startTime));
    }

    private static List<ViewIdWithSize> getLayoutEigenvalue(ViewGroup root, List<View> allViews) {
        int parentSize = root.getWidth() * root.getHeight();
        List<ViewIdWithSize> layoutEigenvalue = new ArrayList<>(allViews.size());
        for (int i = 0; i < allViews.size(); i++) {
            View view = allViews.get(i);
            int viewSize = view.getWidth() * view.getHeight();
            layoutEigenvalue.add(new ViewIdWithSize(view.hashCode(), viewSize * 1.0 / parentSize));
        }
        return layoutEigenvalue;
    }

    private static boolean allNotSimilarByMeasureDistanceLayoutEigenvalueWithRecords(List<List<ViewIdWithSize>> records, List<ViewIdWithSize> layoutEigenvalue) {
        if (records == null || records.isEmpty()) {
            return true;
        }
        WeightedLevenshtein l1 = new WeightedLevenshtein(new ViewWithSizeSubstitutionInterface() {
            @Override
            public double cost(ViewIdWithSize c1, ViewIdWithSize c2) {
                if (c1.id != c2.id) {
                    return c1.sizeInScreenPercent + c2.sizeInScreenPercent;
                } else {
                    return Math.abs(c1.sizeInScreenPercent - c2.sizeInScreenPercent);
                }
            }
        }, new ViewWithSizeInsDelInterface() {
            @Override
            public double deletionCost(ViewIdWithSize c) {
                return c.sizeInScreenPercent;
            }

            @Override
            public double insertionCost(ViewIdWithSize c) {
                return c.sizeInScreenPercent;
            }
        });
        for (List<ViewIdWithSize> record : records) {
            double changePercent = l1.distance(layoutEigenvalue, record);
            if (changePercent < THRESHOLD_LAYOUT_CHANGE) {// similar
                return false;
            }
        }
        return true;
    }

    private int getOverDrawTimes(Set<Object> objects) {
        int overdrawTimes = 0;
        for (Object object : objects) {
            if (object instanceof View) {
                overdrawTimes += ViewBgUtil.getLayer((View) object);
            } else {
                overdrawTimes++;
            }
        }
        return overdrawTimes;
    }

    private Map<Rect, Set<Object>> checkOverDraw(List<View> allViews) {
        Map<Rect, Set<Object>> map = new HashMap<>();
        for (int i = 0; i < allViews.size(); i++) {
            View view = allViews.get(i);
            for (int j = i + 1; j < allViews.size(); j++) {
                View other = allViews.get(j);
                int viewLayer = ViewBgUtil.getLayer(view);
                int otherLayer = ViewBgUtil.getLayer(other);
                if (view == other || viewLayer == 0 || otherLayer == 0) {
                    continue;
                }
                Rect r1 = new Rect();
                Rect r2 = new Rect();
                getViewRect(view, r1);
                getViewRect(other, r2);
                if (!Rect.intersects(r1, r2)) {
                    continue;
                }
                Rect overDraw = calculateOverDraw(r1, r2);
                Set<Object> set;
                if (map.containsKey(overDraw)) {
                    set = map.get(overDraw);
                } else {
                    set = new HashSet<>();
                }
                set.add(view);
                set.add(other);
                map.put(overDraw, set);
            }
        }

        Map<Rect, Set<Object>> rest = new HashMap<>();

        for (Map.Entry<Rect, Set<Object>> entry : map.entrySet()) {
            Rect rect = entry.getKey();
            for (Map.Entry<Rect, Set<Object>> otherEntry : map.entrySet()) {
                Rect otherRect = otherEntry.getKey();
                if (rect == otherRect || !Rect.intersects(rect, otherRect)) {
                    continue;
                }
                Rect overDraw = calculateOverDraw(rect, otherRect);
                if (map.containsKey(overDraw)) {
                    continue;
                }
                Set<Object> set;
                if (rest.containsKey(overDraw)) {
                    set = rest.get(overDraw);
                } else {
                    set = new HashSet<>();
                }
                set.add(otherRect);
                rest.put(overDraw, set);
            }
        }
        map.putAll(rest);
        return map;
    }

    private Rect calculateOverDraw(Rect r1, Rect r2) {
        Rect overDrawArea = new Rect();
        overDrawArea.left = Math.max(r1.left, r2.left);
        overDrawArea.right = Math.min(r1.right, r2.right);
        overDrawArea.bottom = Math.min(r1.bottom, r2.bottom);
        overDrawArea.top = Math.max(r1.top, r2.top);
        return overDrawArea;
    }

    private ViewIssueInfo.ViewInfo getViewInfo(View view, int depth) {
        ViewIssueInfo.ViewInfo viewInfo = new ViewIssueInfo.ViewInfo();
        viewInfo.className = view.getClass().getName();
        viewInfo.id = getId(view);
        Rect rect = new Rect();
        getViewRect(view, rect);
        viewInfo.rect = rect;
        viewInfo.depth = depth;
        if (view instanceof TextView) {
            CharSequence charSequence = ((TextView) view).getText();
            if (!TextUtils.isEmpty(charSequence)) {
                viewInfo.text = charSequence.toString();
                viewInfo.textSize = ((TextView) view).getTextSize();
            }
            viewInfo.hasBackground = ViewBgUtil.getLayer(view) > 1;
        } else {
            viewInfo.hasBackground = ViewBgUtil.getLayer(view) > 0;
        }
        viewInfo.isViewGroup = view instanceof ViewGroup;
        return viewInfo;
    }

    private static void getViewRect(View view, Rect rect) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        rect.left = location[0];
        rect.top = location[1];
        rect.bottom = rect.top + view.getHeight();
        rect.right = rect.left + view.getWidth();
    }

    private static String getId(View view) {
        return String.valueOf(view.getId());
    }

    private void recursiveLoopChildren(ViewGroup parent, Map<View, Integer> map, List<View> allViews) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            if (child == null || child.getVisibility() != View.VISIBLE) {
                continue;
            }
            allViews.add(child);
            Integer integer = map.get((View) child.getParent());
            if (integer == null) {
                // 最外层view
                map.put(child, 1);
            } else {
                // 子view
                map.put(child, integer + 1);
            }
            if (child instanceof ViewGroup) {
                recursiveLoopChildren((ViewGroup) child, map, allViews);
            }
        }
    }

    private int[] getResolution() {
        int[] resolution = new int[2];
        resolution[0] = Resources.getSystem().getDisplayMetrics().widthPixels;
        resolution[1] = Resources.getSystem().getDisplayMetrics().heightPixels;
        return resolution;
    }

}

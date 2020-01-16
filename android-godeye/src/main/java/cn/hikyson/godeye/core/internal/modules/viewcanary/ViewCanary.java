package cn.hikyson.godeye.core.internal.modules.viewcanary;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.ActivityStackUtil;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class ViewCanary extends ProduceableSubject<ViewIssueInfo> implements Install<ViewCanaryContext> {

    private ViewCanaryContext config;
    private boolean mInstalled = false;

    @Override
    public synchronized void install(ViewCanaryContext config) {
        if (mInstalled) {
            L.d("ViewCanary already installed, ignore.");
            return;
        }
        this.config = config;
        mInstalled = true;
        L.d("ViewCanary installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("ViewCanary already uninstalled, ignore.");
            return;
        }
        mInstalled = false;
        L.d("ViewCanary uninstalled.");
    }

    @Override
    public boolean isInstalled() {
        return mInstalled;
    }

    @Override
    public ViewCanaryContext config() {
        return config;
    }

    @Override
    protected Subject<ViewIssueInfo> createSubject() {
        return ReplaySubject.create();
    }

    public void inspect() {
        Activity activity = ActivityStackUtil.getTopActivity();
        if (activity == null) {
            return;
        }
        ViewGroup root = (ViewGroup) activity.getWindow().getDecorView();
        Map<View, Integer> depthMap = new HashMap<>();
        List<View> allViews = new ArrayList<>();
        // allViews should include decor view for inspecting overdraw. However, there is no need for depth inspection to include decor view
        allViews.add(root);
        recursiveLoopChildren(root, depthMap, allViews);
        Map<Rect, Set<Object>> overDrawMap = checkOverDraw(allViews);
        ViewIssueInfo info = new ViewIssueInfo();
        int[] resolution = getResolution();
        info.activityName = activity.getClass().getName();
        info.timestamp = System.currentTimeMillis();
        info.maxDepth = config.maxDepth();
        info.screenWidth = resolution[0];
        info.screenHeight = resolution[1];
        for (Map.Entry<View, Integer> entry : depthMap.entrySet()) {
            if (entry.getKey() instanceof ViewGroup) {
                continue;
            }
            info.views.add(getViewInfo(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<Rect, Set<Object>> entry : overDrawMap.entrySet()) {
            ViewIssueInfo.OverDrawArea overDrawArea = new ViewIssueInfo.OverDrawArea();
            overDrawArea.rect = entry.getKey();
            overDrawArea.overDrawTimes = getOverDrawTimes(entry.getValue()) - 1;
            info.overDrawAreas.add(overDrawArea);
        }
        produce(info);
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

    public static String getId(View view) {
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

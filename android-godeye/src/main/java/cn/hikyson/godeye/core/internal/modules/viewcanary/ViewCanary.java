package cn.hikyson.godeye.core.internal.modules.viewcanary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.Application;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.hikyson.godeye.core.helper.SimpleActivityLifecycleCallbacks;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.internal.modules.pageload.PageDrawMonitor;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ViewUtil;
import io.reactivex.schedulers.Schedulers;

public class ViewCanary extends ProduceableSubject<ViewIssueInfo> implements Install<ViewCanaryContext> {

    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks;
    private ViewCanaryContext config;
    private ViewUtil.OnDrawCallback currentListener;
    private boolean mInstalled = false;

    @Override
    public void install(ViewCanaryContext config) {
        if (mInstalled) {
            L.d("View canary already installed, ignore.");
            return;
        }
        L.d("view canary size installed.");
        this.config = config;
        activityLifecycleCallbacks = new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                currentListener = () -> Schedulers.io().scheduleDirect(() -> inspect(activity));
                PageDrawMonitor.newInstance(activity.getWindow().getDecorView(),currentListener).listen();
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                currentListener = null;
            }
        };
        if (config.application() != null) {
            config.application().registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        }
        mInstalled = true;
    }

    @Override
    public void uninstall() {
        if (!mInstalled) {
            L.d("View canary already uninstalled, ignore.");
            return;
        }
        if (activityLifecycleCallbacks != null && this.config != null && this.config.application() != null) {
            this.config.application().unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        }
        mInstalled = false;
    }

    private void inspect(Activity activity) {
        ViewGroup root = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        ViewGroup content = root.findViewById(android.R.id.content);
        Map<View, Integer> map = new HashMap<>();
        List<View> allViews = new ArrayList<>();
        recursiveLoopChildren(content, map, allViews);
        Map<Rect, Set<Object>> overDrawMap = checkOverDraw(allViews);
        ViewIssueInfo info = new ViewIssueInfo();
        info.activityName = activity.getClass().getName();
        info.timestamp = System.currentTimeMillis();
        info.maxDepth = config.maxDepth();
        for (Map.Entry<View, Integer> entry : map.entrySet()) {
            if (entry.getKey() instanceof ViewGroup && !hasNoTransparentBg(entry.getKey())) {
                continue;
            }
            info.views.add(getViewInfo(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<Rect, Set<Object>> entry : overDrawMap.entrySet()) {
            ViewIssueInfo.OverDrawArea overDrawArea = new ViewIssueInfo.OverDrawArea();
            overDrawArea.rect = entry.getKey();
            overDrawArea.times = entry.getValue().size();
            info.overDrawAreas.add(overDrawArea);
        }
        produce(info);
        map.clear();
    }

    private Map<Rect, Set<Object>> checkOverDraw(List<View> allViews) {
        Map<Rect, Set<Object>> map = new HashMap<>();
        for(View view : allViews) {
            if (!hasNoTransparentBg(view)) {
                continue;
            }
            for (View other: allViews) {
                if (view == other || !hasNoTransparentBg(other)) {
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

    private static ViewIssueInfo.ViewInfo getViewInfo(View view, int depth) {
        ViewIssueInfo.ViewInfo viewInfo = new ViewIssueInfo.ViewInfo();
        viewInfo.className = view.getClass().getName();
        viewInfo.id = getId(view);
        Rect rect = new Rect();
        getViewRect(view, rect);
        viewInfo.rect = rect;
        viewInfo.depth = depth;
        if (view instanceof TextView) {
            CharSequence charSequence = ((TextView) view).getText();
            if (charSequence != null) {
                viewInfo.text = charSequence.toString();
                viewInfo.textSize = ((TextView) view).getTextSize();
            }
        }
        viewInfo.hasBackground = view.getBackground() != null;
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
            Integer integer = map.get((View)child.getParent());
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

    private boolean hasNoTransparentBg(View view) {
        if (view == null || view.getVisibility() != View.VISIBLE) {
            return false;
        }
        Drawable background = view.getBackground();
        if (background != null) {
            if (background instanceof ColorDrawable) {
                int color = ((ColorDrawable) background).getColor();
                // 有背景且背景为透明
                return color != 0;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (background instanceof RippleDrawable) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            // 不可见的即为没有背景
            if (view.getVisibility() != View.VISIBLE) {
                return false;
            }
            // image view 没有src也为没有背景
            if (view instanceof ImageView) {
                if (((ImageView) view).getDrawable() == null) {
                    return false;
                }
            }
            // 没有文字也为没有背景
            if (view instanceof TextView) {
                if (TextUtils.isEmpty(((TextView) view).getText())) {
                    return false;
                }
            }
            return !(view instanceof ViewGroup);
        }
    }
}

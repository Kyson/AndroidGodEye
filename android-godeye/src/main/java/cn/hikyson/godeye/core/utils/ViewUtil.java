package cn.hikyson.godeye.core.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import cn.hikyson.godeye.core.internal.modules.pageload.PageDrawMonitor;

public class ViewUtil {
    public interface OnDrawCallback {
        void didDraw();
    }

    public static void measureActivityDidDraw(final Activity activity, final OnDrawCallback onDrawCallback) {
        measurePageDidDraw(activity.getWindow().getDecorView(), onDrawCallback);
    }

    public static void measureFragmentV4DidDraw(final Fragment fragment, final OnDrawCallback onDrawCallback) {
        View view = fragment.getView();
        if (view != null) {
            measurePageDidDraw(view, onDrawCallback);
        }
    }

    public static void measureFragmentDidDraw(final android.app.Fragment fragment, final OnDrawCallback onDrawCallback) {
        View view = fragment.getView();
        if (view != null) {
            measurePageDidDraw(view, onDrawCallback);
        }
    }

    private static void measurePageDidDraw(final View view, final OnDrawCallback onDrawCallback) {
        PageDrawMonitor.newInstance(view, onDrawCallback).listen();
    }

    public interface ViewProcess {
        void onViewProcess(View view);
    }

    public interface ViewFilter {
        boolean isExclude(View view);
    }

    /**
     * get all views in parent
     *
     * @param parent
     */
    public static List<View> getChildren(ViewGroup parent) {
        ViewFilter viewFilter = view -> false;
        ViewProcess viewProcess = view -> {
        };
        return getChildren(parent, viewFilter, viewProcess);
    }

    /**
     * get all views in parent exclude viewFilter
     *
     * @param parent
     * @param viewFilter
     * @return
     */
    public static List<View> getChildren(ViewGroup parent, ViewFilter viewFilter, ViewProcess viewProcess) {
        List<View> tmp = new ArrayList<>();
        getChildrenRecursive(tmp, parent, viewFilter, viewProcess);
        return tmp;
    }

    /**
     * get all views in parent
     *
     * @param allViews
     * @param parent
     * @param viewFilter
     */
    private static void getChildrenRecursive(List<View> allViews, ViewGroup parent, ViewFilter viewFilter, ViewProcess viewProcess) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            if (!viewFilter.isExclude(child)) {
                allViews.add(child);
                viewProcess.onViewProcess(child);
            }
            if (child instanceof ViewGroup) {
                getChildrenRecursive(allViews, (ViewGroup) child, viewFilter, viewProcess);
            }
        }
    }
}

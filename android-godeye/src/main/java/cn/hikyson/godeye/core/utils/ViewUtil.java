package cn.hikyson.godeye.core.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.View;

public class ViewUtil {
    public interface OnDrawCallback {
        void didDraw();
    }

    public static Handler sHandler = new Handler(Looper.getMainLooper());

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
        view.post(() -> sHandler.post(() -> {
            if (onDrawCallback != null) {
                onDrawCallback.didDraw();
            }
        }));
    }
}

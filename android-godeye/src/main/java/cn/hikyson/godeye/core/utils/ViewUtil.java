package cn.hikyson.godeye.core.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
}

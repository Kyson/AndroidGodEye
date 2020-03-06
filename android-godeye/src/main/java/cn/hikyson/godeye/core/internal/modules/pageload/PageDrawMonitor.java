package cn.hikyson.godeye.core.internal.modules.pageload;

import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;

import cn.hikyson.godeye.core.helper.ChoreographerInjecor;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ViewUtil;

public class PageDrawMonitor {

    private final int CALLBACK_COMMIT = 3;
    private final Object FRAME_CALLBACK_TOKEN = new Object() {
        public String toString() { return "FRAME_CALLBACK_TOKEN"; }
    };
    private boolean isDraw;
    private Method choreographerMethod;
    private View view;
    private ViewUtil.OnDrawCallback onDrawCallback;

    public static PageDrawMonitor newInstance(View view, ViewUtil.OnDrawCallback onDrawCallback) {
        return new PageDrawMonitor(view, onDrawCallback);
    }

    private PageDrawMonitor(View view, ViewUtil.OnDrawCallback onDrawCallback) {
        try {
            choreographerMethod = ChoreographerInjecor.getChoreographerProvider().getChoreographer().getClass().getMethod("postCallback", int.class, Runnable.class, Object.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        this.view = view;
        this.onDrawCallback = onDrawCallback;
    }

    public void listen() {
        ViewTreeObserver.OnDrawListener onDrawListener = () -> isDraw = true;
        view.getViewTreeObserver().addOnDrawListener(onDrawListener);
        runOnDrawEnd(view, onDrawListener, 3, onDrawCallback);
    }

    private void runOnDrawEnd(View view, ViewTreeObserver.OnDrawListener onDrawListener, int maxPostTimes, @NonNull ViewUtil.OnDrawCallback onDrawCallback) {
        if (view == null || onDrawListener == null) {
            return;
        }
        maxPostTimes --;
        int finalMaxPostTimes = maxPostTimes;
        postTraversalFinishCallBack(() -> {
            if (!isDraw && finalMaxPostTimes > 0) {
                runOnDrawEnd(view, onDrawListener, finalMaxPostTimes, onDrawCallback);
            } else {
                view.getViewTreeObserver().removeOnDrawListener(onDrawListener);
                onDrawCallback.didDraw();
            }
        });
    }

    private void postTraversalFinishCallBack(OnTraversalFinishListener onTraversalFinishListener) {
        if (choreographerMethod == null) {
            return;
        }
        try {
            choreographerMethod.invoke(ChoreographerInjecor.getChoreographerProvider().getChoreographer(), CALLBACK_COMMIT, (Runnable) () -> {
                if (onTraversalFinishListener != null) {
                    onTraversalFinishListener.onFinish();
                }
            }, FRAME_CALLBACK_TOKEN);
        } catch (Throwable throwable) {
            L.w(throwable.getMessage());
        }
    }
    private interface OnTraversalFinishListener{
        void onFinish();
    }
}

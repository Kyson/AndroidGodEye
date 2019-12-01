package cn.hikyson.godeye.core.internal.modules.imagecanary;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import cn.hikyson.godeye.core.helper.SimpleActivityLifecycleCallbacks;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ViewUtil;

class ImageCanaryInternal {

    //    private final List<BitmapInfoAnalyzer> mAnalyzerList;
    private final BitmapInfoAnalyzer mBitmapInfoAnalyzer;
    private final ImageCanaryConfigProvider mImageCanaryConfigProvider;

    ImageCanaryInternal(ImageCanaryConfigProvider imageCanaryConfigProvider) {
//        this.mAnalyzerList = imageCanaryConfigProvider.getExtraBitmapInfoAnalyzers();
        this.mBitmapInfoAnalyzer = new DefaultBitmapInfoAnalyzer();
        this.mImageCanaryConfigProvider = imageCanaryConfigProvider;
    }

    private SimpleActivityLifecycleCallbacks callbacks;

    void start(Application application, ImageCanary imageCanaryEngine) {
        callbacks = new SimpleActivityLifecycleCallbacks() {

            private Map<Activity, ViewTreeObserver.OnDrawListener> mOnDrawListenerMap = new HashMap<>();
            private Handler mHandler = new Handler();

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView();
                Runnable callback = inspectInner(activity.getClass().getName(), new WeakReference<>(parent), imageCanaryEngine);
                ViewTreeObserver.OnDrawListener onDrawListener = () -> {
                    mHandler.removeCallbacks(callback);
                    mHandler.postDelayed(callback, 300);
                };
                mOnDrawListenerMap.put(activity, onDrawListener);
                parent.getViewTreeObserver().addOnDrawListener(onDrawListener);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                ViewTreeObserver.OnDrawListener onDrawListener = mOnDrawListenerMap.remove(activity);
                ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView();
                if (onDrawListener != null) {
                    parent.getViewTreeObserver().removeOnDrawListener(onDrawListener);
                }
            }
        };
        application.registerActivityLifecycleCallbacks(callbacks);
    }

    void stop(Application application) {
        if (callbacks == null) {
            return;
        }
        application.unregisterActivityLifecycleCallbacks(callbacks);
        callbacks = null;
    }

    private Runnable inspectInner(String activityClass, WeakReference<ViewGroup> parent, ImageCanary imageCanaryEngine) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    ViewGroup p = parent.get();
                    if (p != null) {
                        recursiveLoopChildren(activityClass, p, imageCanaryEngine);
                    }
                } catch (Throwable e) {
                    L.e(e);
                }
            }
        };
    }

    private void recursiveLoopChildren(String activityClass, ViewGroup parent, ImageCanary imageCanaryEngine) {
        ViewUtil.getChildren(parent, view -> {
            boolean isValid = view.getWidth() > 0 && view.getHeight() > 0
                    && view instanceof ImageView
                    && view.getVisibility() == View.VISIBLE;
            return !isValid;
        }, view -> {
            BitmapInfo bitmapInfo = mBitmapInfoAnalyzer.analyze((ImageView) view);
            if (bitmapInfo.isValid()) {
                ImageIssue imageIssue = new ImageIssue();
                imageIssue.bitmapHeight = bitmapInfo.bitmapHeight;
                imageIssue.bitmapWidth = bitmapInfo.bitmapWidth;
                imageIssue.imageViewId = view.getId();
                imageIssue.imageViewWidth = view.getWidth();
                imageIssue.imageViewHeight = view.getHeight();
                imageIssue.activityClassName = activityClass;
                imageIssue.timestamp = System.currentTimeMillis();
                if (mImageCanaryConfigProvider.isBitmapQualityTooHigh(bitmapInfo.bitmapWidth, bitmapInfo.bitmapHeight, view.getWidth(), view.getHeight())) {
                    imageIssue.issueType = ImageIssue.IssueType.BITMAP_QUALITY_TOO_HIGH;
                    imageCanaryEngine.produce(imageIssue);
                } else if (mImageCanaryConfigProvider.isBitmapQualityTooLow(bitmapInfo.bitmapWidth, bitmapInfo.bitmapHeight, view.getWidth(), view.getHeight())) {
                    imageIssue.issueType = ImageIssue.IssueType.BITMAP_QUALITY_TOO_LOW;
                    imageCanaryEngine.produce(imageIssue);
                }
            }
        });
    }
}

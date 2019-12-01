package cn.hikyson.godeye.core.internal.modules.imagecanary;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            private Set<ImageIssue> mImageIssues = new HashSet<>();

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView();
                Runnable callback = inspectInner(new WeakReference<>(activity), imageCanaryEngine, mImageIssues);
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

    private Runnable inspectInner(WeakReference<Activity> activity, ImageCanary imageCanaryEngine, Set<ImageIssue> imageIssues) {
        return () -> {
            try {
                Activity p = activity.get();
                if (p != null) {
                    ViewGroup parent = (ViewGroup) p.getWindow().getDecorView();
                    recursiveLoopChildren(p, parent, imageCanaryEngine, imageIssues);
                }
            } catch (Throwable e) {
                L.e(e);
            }
        };
    }

    private void recursiveLoopChildren(Activity activity, ViewGroup parent, ImageCanary imageCanaryEngine, Set<ImageIssue> imageIssues) {
        ViewUtil.getChildren(parent, view -> false, view -> {
            List<BitmapInfo> bitmapInfos = mBitmapInfoAnalyzer.analyze(view);
            for (BitmapInfo bitmapInfo : bitmapInfos) {
                if (bitmapInfo.isValid()) {
                    ImageIssue imageIssue = new ImageIssue();
                    imageIssue.bitmapHeight = bitmapInfo.bitmapHeight;
                    imageIssue.bitmapWidth = bitmapInfo.bitmapWidth;
                    imageIssue.imageViewHashCode = view.hashCode();
                    imageIssue.imageViewWidth = view.getWidth();
                    imageIssue.imageViewHeight = view.getHeight();
                    imageIssue.activityClassName = activity.getClass().getName();
                    imageIssue.activityHashCode = activity.hashCode();
                    imageIssue.timestamp = System.currentTimeMillis();
                    if (mImageCanaryConfigProvider.isBitmapQualityTooHigh(bitmapInfo.bitmapWidth, bitmapInfo.bitmapHeight, view.getWidth(), view.getHeight())) {
                        imageIssue.issueType = ImageIssue.IssueType.BITMAP_QUALITY_TOO_HIGH;
                        if (!imageIssues.contains(imageIssue)) {
                            imageIssues.add(imageIssue);
                            imageCanaryEngine.produce(imageIssue);
                        }
                    } else if (mImageCanaryConfigProvider.isBitmapQualityTooLow(bitmapInfo.bitmapWidth, bitmapInfo.bitmapHeight, view.getWidth(), view.getHeight())) {
                        imageIssue.issueType = ImageIssue.IssueType.BITMAP_QUALITY_TOO_LOW;
                        if (!imageIssues.contains(imageIssue)) {
                            imageIssues.add(imageIssue);
                            imageCanaryEngine.produce(imageIssue);
                        }
                    }
                }
            }
        });
    }
}

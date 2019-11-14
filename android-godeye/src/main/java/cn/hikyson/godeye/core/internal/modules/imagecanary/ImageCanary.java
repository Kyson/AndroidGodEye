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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.hikyson.godeye.core.helper.SimpleActivityLifecycleCallbacks;
public class ImageCanary {

    private static List<BitmapInfoAnalyzer> analyzerList = new ArrayList<>();

    static {
        analyzerList.add(new DefaultBitmapInfoAnalyzer());
    }

    static void addAnalyzers(List<BitmapInfoAnalyzer> analyzers) {
        if (analyzers != null) {
            analyzerList.addAll(analyzers);
        }
    }

    private static SimpleActivityLifecycleCallbacks callbacks;

    public static void start(Application application, ImageCanaryEngine imageCanaryEngine) {
        callbacks = new SimpleActivityLifecycleCallbacks() {

            private Set<Integer> viewSet = new HashSet<>();

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                viewSet.clear();
                ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView();
                parent.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
                    private Handler handler = new Handler();
                    @Override
                    public void onDraw() {
                        handler.removeCallbacksAndMessages(null);
                        handler.postDelayed(inspectInner(new WeakReference<>(parent), imageCanaryEngine, viewSet), 100);
                    }
                });
            }
        };
        application.registerActivityLifecycleCallbacks(callbacks);
    }

    public static void stop(Application application) {
        if (callbacks == null) {
            return;
        }
        application.unregisterActivityLifecycleCallbacks(callbacks);
        callbacks = null;
    }

    private static Runnable inspectInner(WeakReference<ViewGroup> parent, ImageCanaryEngine imageCanaryEngine, Set<Integer> viewSet) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    recursiveLoopChildren(parent, imageCanaryEngine, viewSet);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        };
    }


    private static void recursiveLoopChildren(WeakReference<ViewGroup> parent, ImageCanaryEngine imageCanaryEngine, Set<Integer> viewSet) {
        for (int i = 0; i < parent.get().getChildCount(); i++) {
            final View child = parent.get().getChildAt(i);
            if (child instanceof ViewGroup) {
                recursiveLoopChildren((new WeakReference<>((ViewGroup) child)), imageCanaryEngine, viewSet);
            } else {
                if (child instanceof ImageView && child.getVisibility() == View.VISIBLE) {
                    if (viewSet.contains(child.getId())){
                        return;
                    }
                    viewSet.add(child.getId());
                    BitmapInfo bitmapInfo = new BitmapInfo();
                    for (BitmapInfoAnalyzer analyzer : analyzerList) {
                        bitmapInfo = analyzer.analyze((ImageView) child);
                        if (bitmapInfo.isValid()) {
                            break;
                        }
                    }
                    if (bitmapInfo.isValid() && child.getWidth() > 0 && child.getHeight() > 0) {
                        ImageIssue imageIssue = new ImageIssue();
                        imageIssue.bitmapHeight = bitmapInfo.bitmapHeight;
                        imageIssue.bitmapWidth = bitmapInfo.bitmapWidth;
                        imageIssue.imageViewId = child.getId();
                        imageIssue.imageViewWidth = child.getWidth();
                        imageIssue.imageViewHeight = child.getHeight();
                        if (imageCanaryEngine.config().getImageCanaryConfigProvider().isBitmapQualityTooHigh(bitmapInfo.bitmapWidth, bitmapInfo.bitmapHeight, child.getWidth(), child.getHeight())) {
                            imageIssue.issueType = ImageIssue.IssueType.BITMAP_QUALITY_TOO_HIGH;
                            imageCanaryEngine.produce(imageIssue);
                        } else if (imageCanaryEngine.config().getImageCanaryConfigProvider().isBitmapQualityTooLow(bitmapInfo.bitmapWidth, bitmapInfo.bitmapHeight, child.getWidth(), child.getHeight())) {
                            imageIssue.issueType = ImageIssue.IssueType.BITMAP_QUALITY_TOO_LOW;
                            imageCanaryEngine.produce(imageIssue);
                        }
                    }
                }
            }
        }
    }
}

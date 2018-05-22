package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;

/**
 * Created by kysonchao on 2018/1/25.
 */
public class PageloadEngine implements Engine {
    private ActivityStack mActivityStack;
    private Producer<PageloadInfo> mProducer;
    private PageloadContext mPageloadContext;
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;
    private final Handler mHandler;

    public PageloadEngine(Producer<PageloadInfo> producer, PageloadContext config) {
        mProducer = producer;
        mPageloadContext = config;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void work() {
        if (mActivityStack == null) {
            mActivityStack = new ActivityStack();
        }
        if (mActivityLifecycleCallbacks == null) {
            mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
                    final long time = System.currentTimeMillis();
                    PageloadInfo pageloadInfo = new PageloadInfo(String.valueOf(activity.hashCode()), activity.getClass().getSimpleName(), "created", time);
                    pageloadInfo.loadTimeInfo = mActivityStack.onCreate(activity, time);
                    mProducer.produce(pageloadInfo);
                    measureActivityDidAppearOnCreate(activity, new OnActivityDidAppearCallback() {
                        @Override
                        public void didAppear() {
                            final long time2 = System.currentTimeMillis();
                            PageloadInfo pageloadInfo2 = new PageloadInfo(String.valueOf(activity.hashCode()), activity.getClass().getSimpleName(), "didDraw", time2);
                            pageloadInfo2.loadTimeInfo = mActivityStack.onDidDraw(activity, time2);
                            mProducer.produce(pageloadInfo2);
                        }
                    });
                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    final long time = System.currentTimeMillis();
                    PageloadInfo pageloadInfo = new PageloadInfo(String.valueOf(activity.hashCode()), activity.getClass().getSimpleName(), "destroyed", time);
                    pageloadInfo.loadTimeInfo = mActivityStack.onDestory(activity);
                    mProducer.produce(pageloadInfo);
                }
            };
        }
        mPageloadContext.application().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    @Override
    public void shutdown() {
        mPageloadContext.application().unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        mActivityLifecycleCallbacks = null;
        mActivityStack = null;
    }

    public interface OnActivityDidAppearCallback {
        void didAppear();
    }

    private void measureActivityDidAppearOnCreate(final Activity activity, final OnActivityDidAppearCallback onActivityDidAppearCallback) {
        activity.getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onActivityDidAppearCallback != null) {
                            onActivityDidAppearCallback.didAppear();
                        }
                    }
                });
            }
        });
    }

}

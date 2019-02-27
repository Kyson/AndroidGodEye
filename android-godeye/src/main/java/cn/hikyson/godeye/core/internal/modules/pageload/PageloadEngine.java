package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


import cn.hikyson.godeye.core.helper.SimpleActivityLifecycleCallbacks;
import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;

/**
 * Created by kysonchao on 2018/1/25.
 */
public class PageloadEngine implements Engine {
    private PageloadActivityStack mActivityStack;
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
            mActivityStack = new PageloadActivityStack();
        }
        if (mActivityLifecycleCallbacks == null) {
            mActivityLifecycleCallbacks = new SimpleActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
                    final long time = System.currentTimeMillis();
                    PageloadInfo pageloadInfo = new PageloadInfo(activity, String.valueOf(activity.hashCode()), activity.getClass().getSimpleName(), "created", time);
                    pageloadInfo.loadTimeInfo = mActivityStack.onCreate(activity, time);
                    mProducer.produce(pageloadInfo);
                }

                @Override
                public void onActivityStarted(final Activity activity) {
                    measureActivityDidAppear(activity, new OnActivityDidAppearCallback() {
                        @Override
                        public void didAppear() {
                            final long time = System.currentTimeMillis();
                            PageloadInfo pageloadInfo = new PageloadInfo(activity, String.valueOf(activity.hashCode()), activity.getClass().getSimpleName(), "didDraw", time);
                            pageloadInfo.loadTimeInfo = mActivityStack.onDidDraw(activity, time);
                            mProducer.produce(pageloadInfo);
                        }
                    });
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    final long time = System.currentTimeMillis();
                    PageloadInfo pageloadInfo = new PageloadInfo(activity, String.valueOf(activity.hashCode()), activity.getClass().getSimpleName(), "destroyed", time);
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

    void onPageLoaded(Activity activity) {
        long time = System.currentTimeMillis();
        PageloadInfo pageloadInfo = new PageloadInfo(activity, String.valueOf(activity.hashCode()), activity.getClass().getSimpleName(), "loaded", time);
        pageloadInfo.loadTimeInfo = mActivityStack.onLoaded(activity, time);
        mProducer.produce(pageloadInfo);
    }

    public interface OnActivityDidAppearCallback {
        void didAppear();
    }

    private void measureActivityDidAppear(final Activity activity, final OnActivityDidAppearCallback onActivityDidAppearCallback) {
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

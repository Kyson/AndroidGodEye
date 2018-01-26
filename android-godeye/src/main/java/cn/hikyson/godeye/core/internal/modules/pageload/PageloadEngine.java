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
    private Producer<List<PageloadInfo>> mProducer;
    private PageloadContext mPageloadContext;
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;
    private final Handler mHandler;

    public PageloadEngine(Producer<List<PageloadInfo>> producer, PageloadContext config) {
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
                    mActivityStack.push(activity);
                    mProducer.produce(mActivityStack.getActivityPageloads());
                    final long time = System.currentTimeMillis();
                    measureActivityDidAppearOnCreate(activity, new OnActivityDidAppearCallback() {
                        @Override
                        public void didAppear() {
                            mActivityStack.push(activity, new LoadTimeInfo(System.currentTimeMillis() - time));
                            mProducer.produce(mActivityStack.getActivityPageloads());
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
                    mActivityStack.pop(activity);
                    mProducer.produce(mActivityStack.getActivityPageloads());
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

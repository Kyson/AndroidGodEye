package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.hikyson.godeye.core.helper.SimpleActivityLifecycleCallbacks;
import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kysonchao on 2018/1/25.
 */
public class PageloadEngine implements Engine {
    private PageloadActivityStack mActivityStack;
    private Producer<PageloadInfo> mProducer;
    private PageloadContext mPageloadContext;
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;
    private final Handler mHandler;
    private ExecutorService mPageloadExecutor;
    private final Object mPageloadExecutorLock;

    public PageloadEngine(Producer<PageloadInfo> producer, PageloadContext config) {
        mProducer = producer;
        mPageloadContext = config;
        mHandler = new Handler(Looper.getMainLooper());
        mPageloadExecutorLock = new Object();
        mActivityStack = new PageloadActivityStack();
    }

    private void createPageloadExecutor() {
        synchronized (mPageloadExecutorLock) {
            if (mPageloadExecutor == null) {
                mPageloadExecutor = Executors.newSingleThreadExecutor();
            }
        }
    }

    private void executeOnPageloadExecutor(Runnable runnable) {
        synchronized (mPageloadExecutorLock) {
            if (mPageloadExecutor != null) {
                mPageloadExecutor.execute(runnable);
            }
        }
    }

    private void shutdownPageloadExecutor() {
        synchronized (mPageloadExecutorLock) {
            if (mPageloadExecutor != null) {
                mPageloadExecutor.shutdown();
                mPageloadExecutor = null;
            }
        }
    }

    @Override
    public void work() {
        createPageloadExecutor();
        if (mActivityLifecycleCallbacks == null) {
            mActivityLifecycleCallbacks = new SimpleActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
                    executeOnPageloadExecutor(new Runnable() {
                        @Override
                        public void run() {
                            ThreadUtil.ensureWorkThread("PageloadEngine onActivityCreated");
                            final long time = System.currentTimeMillis();
                            PageloadInfo pageloadInfo = new PageloadInfo(activity, String.valueOf(activity.hashCode()), activity.getClass().getSimpleName(), "created", time);
                            if (mActivityStack != null) {
                                pageloadInfo.loadTimeInfo = mActivityStack.onCreate(activity, time);
                                mProducer.produce(pageloadInfo);
                            }
                        }
                    });

                }

                @Override
                public void onActivityStarted(final Activity activity) {
                    measureActivityDidAppear(activity, new OnActivityDidAppearCallback() {
                        @Override
                        public void didAppear() {
                            executeOnPageloadExecutor(new Runnable() {
                                @Override
                                public void run() {
                                    final long time = System.currentTimeMillis();
                                    PageloadInfo pageloadInfo = new PageloadInfo(activity, String.valueOf(activity.hashCode()), activity.getClass().getSimpleName(), "didDraw", time);
                                    if (mActivityStack != null) {
                                        pageloadInfo.loadTimeInfo = mActivityStack.onDidDraw(activity, time);
                                        mProducer.produce(pageloadInfo);
                                    }
                                }
                            });
                        }
                    });
                }

                @Override
                public void onActivityDestroyed(final Activity activity) {
                    executeOnPageloadExecutor(new Runnable() {
                        @Override
                        public void run() {
                            ThreadUtil.ensureWorkThread("PageloadEngine onActivityDestroyed");
                            final long time = System.currentTimeMillis();
                            PageloadInfo pageloadInfo = new PageloadInfo(activity, String.valueOf(activity.hashCode()), activity.getClass().getSimpleName(), "destroyed", time);
                            if (mActivityStack != null) {
                                pageloadInfo.loadTimeInfo = mActivityStack.onDestory(activity);
                                mProducer.produce(pageloadInfo);
                            }
                        }
                    });
                }
            };
        }
        mPageloadContext.application().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    @Override
    public void shutdown() {
        mPageloadContext.application().unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        mActivityLifecycleCallbacks = null;
        shutdownPageloadExecutor();
    }

    void onPageLoaded(final Activity activity) {
        executeOnPageloadExecutor(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                PageloadInfo pageloadInfo = new PageloadInfo(activity, String.valueOf(activity.hashCode()), activity.getClass().getSimpleName(), "loaded", time);
                pageloadInfo.loadTimeInfo = mActivityStack.onLoaded(activity, time);
                mProducer.produce(pageloadInfo);
            }
        });
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

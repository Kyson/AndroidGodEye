package cn.hikyson.godeye.core.internal.modules.pageload;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import java.util.Map;

import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.ViewUtil;

public class FragmentLifecycleCallbacksV4 extends android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks {
    private PageLifecycleRecords mPageLifecycleRecords;
    private PageInfoProvider mPageInfoProvider;
    private Map<Object, PageInfo<?>> mCachePageInfo;
    private Producer<PageLifecycleEventInfo> mProducer;
    private Handler mHandler;

    public FragmentLifecycleCallbacksV4(PageLifecycleRecords pageLifecycleRecords, PageInfoProvider pageInfoProvider, Map<Object, PageInfo<?>> cachePageInfo, Producer<PageLifecycleEventInfo> producer, Handler handler) {
        mPageLifecycleRecords = pageLifecycleRecords;
        mPageInfoProvider = pageInfoProvider;
        mCachePageInfo = cachePageInfo;
        mProducer = producer;
        mHandler = handler;
    }

    @Override
    public void onFragmentAttached(FragmentManager fm, final Fragment f, Context context) {
        final long time = System.currentTimeMillis();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final PageInfo<Fragment> pageInfo = mPageInfoProvider.getInfoByV4Fragment(f);
                mCachePageInfo.put(f, pageInfo);
                PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_ATTACH, time);
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
                Log.d("kyson", "onFragmentAttached:" + pageInfo);
            }
        });
    }

    @Override
    public void onFragmentCreated(FragmentManager fm, final Fragment f, Bundle savedInstanceState) {
        final long time = System.currentTimeMillis();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final PageInfo<Fragment> pageInfo = (PageInfo<Fragment>) mCachePageInfo.get(f);
                if (pageInfo != null) {
                    PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_CREATE, time);
                    mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
                    Log.d("kyson", "onFragmentCreated:" + pageInfo);
                }
            }
        });
    }

    @Override
    public void onFragmentViewCreated(FragmentManager fm, final Fragment f, View v, Bundle savedInstanceState) {
        final long time0 = System.currentTimeMillis();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final PageInfo<Fragment> pageInfo = (PageInfo<Fragment>) mCachePageInfo.get(f);
                if (pageInfo != null) {
                    PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_VIEW_CREATE, time0);
                    mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
                    Log.d("kyson", "onFragmentViewCreated:" + pageInfo);
                }
            }
        });
        ViewUtil.measureFragmentV4DidDraw(f, new ViewUtil.OnDrawCallback() {
            @Override
            public void didDraw() {
                final long time1 = System.currentTimeMillis();
                final PageInfo<Fragment> pageInfo = (PageInfo<Fragment>) mCachePageInfo.get(f);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_DRAW, time1);
                        mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
                        Log.d("kyson", "onFragmentDraw:" + pageInfo);
                    }
                });
            }
        });
    }

    @Override
    public void onFragmentStarted(FragmentManager fm, final Fragment f) {
        final long time = System.currentTimeMillis();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final PageInfo<Fragment> pageInfo = (PageInfo<Fragment>) mCachePageInfo.get(f);
                if (pageInfo != null) {
                    PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_START, time);
                    mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
                    Log.d("kyson", "onFragmentStarted:" + pageInfo);
                }
            }
        });
    }

    @Override
    public void onFragmentResumed(FragmentManager fm, final Fragment f) {
        final long time = System.currentTimeMillis();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final PageInfo<Fragment> pageInfo = (PageInfo<Fragment>) mCachePageInfo.get(f);
                if (pageInfo != null) {
                    PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_RESUME, time);
                    mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
                    Log.d("kyson", "onFragmentResumed:" + pageInfo);
                }
            }
        });
    }

    @Override
    public void onFragmentPaused(FragmentManager fm, final Fragment f) {
        final long time = System.currentTimeMillis();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final PageInfo<Fragment> pageInfo = (PageInfo<Fragment>) mCachePageInfo.get(f);
                if (pageInfo != null) {
                    PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_PAUSE, time);
                    mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
                    Log.d("kyson", "onFragmentPaused:" + pageInfo);
                }
            }
        });
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, final Fragment f) {
        final long time = System.currentTimeMillis();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final PageInfo<Fragment> pageInfo = (PageInfo<Fragment>) mCachePageInfo.get(f);
                if (pageInfo != null) {
                    PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_STOP, time);
                    mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
                    Log.d("kyson", "onFragmentStopped:" + pageInfo);
                }
            }
        });
    }

    @Override
    public void onFragmentViewDestroyed(FragmentManager fm, final Fragment f) {
        final long time = System.currentTimeMillis();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final PageInfo<Fragment> pageInfo = (PageInfo<Fragment>) mCachePageInfo.get(f);
                if (pageInfo != null) {
                    PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_VIEW_DESTROY, time);
                    mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
                    Log.d("kyson", "onFragmentViewDestroyed:" + pageInfo);
                }
            }
        });
    }

    @Override
    public void onFragmentDestroyed(FragmentManager fm, final Fragment f) {
        final long time = System.currentTimeMillis();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final PageInfo<Fragment> pageInfo = (PageInfo<Fragment>) mCachePageInfo.get(f);
                if (pageInfo != null) {
                    PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_DESTROY, time);
                    mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
                    Log.d("kyson", "onFragmentDestroyed:" + pageInfo);
                }
            }
        });
    }

    @Override
    public void onFragmentDetached(FragmentManager fm, final Fragment f) {
        final long time = System.currentTimeMillis();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                PageInfo<Fragment> pageInfo = (PageInfo<Fragment>) mCachePageInfo.remove(f);
                if (pageInfo != null) {
                    PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_DETACH, time);
                    mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
                    Log.d("kyson", "onFragmentDetached:" + pageInfo);
                }
            }
        });
    }

}

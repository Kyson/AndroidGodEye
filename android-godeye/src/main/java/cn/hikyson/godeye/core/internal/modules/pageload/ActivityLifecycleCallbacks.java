package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.ViewUtil;

public class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private PageLifecycleRecords mPageLifecycleRecords;
    private PageInfoProvider mPageInfoProvider;
    private Producer<PageLifecycleEventInfo> mProducer;
    private Map<Object, PageInfo<?>> mCachePageInfo;
    private Handler mHandler;
    private Set<Activity> mStartedActivity;

    ActivityLifecycleCallbacks(PageLifecycleRecords pageLifecycleRecords, PageInfoProvider pageInfoProvider, Producer<PageLifecycleEventInfo> producer, Handler handler) {
        mPageLifecycleRecords = pageLifecycleRecords;
        mPageInfoProvider = pageInfoProvider;
        mProducer = producer;
        mCachePageInfo = new HashMap<>();
        mHandler = handler;
        mStartedActivity = new HashSet<>();
    }

    void onActivityLoad(final Activity activity) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            // noinspection unchecked
            final PageInfo<Activity> pageInfo = (PageInfo<Activity>) mCachePageInfo.get(activity);
            if (pageInfo != null) {
                boolean isPageLoaded = mPageLifecycleRecords.isExistEvent(pageInfo, ActivityLifecycleEvent.ON_LOAD);
                if (isPageLoaded) {
                    return;
                }
                pageInfo.extraInfo = mPageInfoProvider.getInfoByActivity(activity);
                PageLifecycleEventWithTime<Activity> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, ActivityLifecycleEvent.ON_LOAD, time);
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    void onFragmentV4Load(final Fragment f) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            // noinspection unchecked
            final PageInfo<Fragment> pageInfo = (PageInfo<Fragment>) mCachePageInfo.get(f);
            if (pageInfo != null) {
                boolean isPageLoaded = mPageLifecycleRecords.isExistEvent(pageInfo, FragmentLifecycleEvent.ON_LOAD);
                if (isPageLoaded) {
                    return;
                }
                pageInfo.extraInfo = mPageInfoProvider.getInfoByV4Fragment(f);
                PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_LOAD, time);
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    void onFragmentLoad(final android.app.Fragment f) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            // noinspection unchecked
            final PageInfo<android.app.Fragment> pageInfo = (PageInfo<android.app.Fragment>) mCachePageInfo.get(f);
            if (pageInfo != null) {
                boolean isPageLoaded = mPageLifecycleRecords.isExistEvent(pageInfo, FragmentLifecycleEvent.ON_LOAD);
                if (isPageLoaded) {
                    return;
                }
                pageInfo.extraInfo = mPageInfoProvider.getInfoByFragment(f);
                PageLifecycleEventWithTime<android.app.Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_LOAD, time);
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    void onFragmentV4Show(final Fragment f) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            // noinspection unchecked
            final PageInfo<Fragment> pageInfo = (PageInfo<Fragment>) mCachePageInfo.get(f);
            if (pageInfo != null) {
                pageInfo.extraInfo = mPageInfoProvider.getInfoByV4Fragment(f);
                PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_SHOW, time);
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    void onFragmentV4Hide(final Fragment f) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            // noinspection unchecked
            final PageInfo<Fragment> pageInfo = (PageInfo<Fragment>) mCachePageInfo.get(f);
            if (pageInfo != null) {
                pageInfo.extraInfo = mPageInfoProvider.getInfoByV4Fragment(f);
                PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_HIDE, time);
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    void onFragmentShow(final android.app.Fragment f) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            // noinspection unchecked
            final PageInfo<android.app.Fragment> pageInfo = (PageInfo<android.app.Fragment>) mCachePageInfo.get(f);
            if (pageInfo != null) {
                pageInfo.extraInfo = mPageInfoProvider.getInfoByFragment(f);
                PageLifecycleEventWithTime<android.app.Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_SHOW, time);
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    void onFragmentHide(final android.app.Fragment f) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            // noinspection unchecked
            final PageInfo<android.app.Fragment> pageInfo = (PageInfo<android.app.Fragment>) mCachePageInfo.get(f);
            if (pageInfo != null) {
                pageInfo.extraInfo = mPageInfoProvider.getInfoByFragment(f);
                PageLifecycleEventWithTime<android.app.Fragment> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, FragmentLifecycleEvent.ON_HIDE, time);
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.getFragmentManager().registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks(mPageLifecycleRecords, mPageInfoProvider, mCachePageInfo, mProducer, mHandler), true);
        }
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacksV4(mPageLifecycleRecords, mPageInfoProvider, mCachePageInfo, mProducer, mHandler), true);
        }
        final long time0 = System.currentTimeMillis();
        mHandler.post(() -> {
            Map<String, String> extraInfo = mPageInfoProvider.getInfoByActivity(activity);
            PageInfo<Activity> pageInfo = new PageInfo<>(activity, extraInfo);
            mCachePageInfo.put(activity, pageInfo);
            PageLifecycleEventWithTime<Activity> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, ActivityLifecycleEvent.ON_CREATE, time0);
            mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
        });
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            // noinspection unchecked
            PageInfo<Activity> pageInfo = (PageInfo<Activity>) mCachePageInfo.get(activity);
            if (pageInfo != null) {
                pageInfo.extraInfo = mPageInfoProvider.getInfoByActivity(activity);
                PageLifecycleEventWithTime<Activity> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, ActivityLifecycleEvent.ON_START, time);
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
        if (!mStartedActivity.contains(activity)) {
            mStartedActivity.add(activity);
            ViewUtil.measureActivityDidDraw(activity, () -> {
                final long time1 = System.currentTimeMillis();
                mHandler.post(() -> {
                    // noinspection unchecked
                    PageInfo<Activity> pageInfo = (PageInfo<Activity>) mCachePageInfo.get(activity);
                    if (pageInfo != null) {
                        pageInfo.extraInfo = mPageInfoProvider.getInfoByActivity(activity);
                        PageLifecycleEventWithTime<Activity> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, ActivityLifecycleEvent.ON_DRAW, time1);
                        mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
                    }
                });
            });
        }
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            // noinspection unchecked
            PageInfo<Activity> pageInfo = (PageInfo<Activity>) mCachePageInfo.get(activity);
            if (pageInfo != null) {
                pageInfo.extraInfo = mPageInfoProvider.getInfoByActivity(activity);
                PageLifecycleEventWithTime<Activity> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, ActivityLifecycleEvent.ON_RESUME, time);
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            // noinspection unchecked
            PageInfo<Activity> pageInfo = (PageInfo<Activity>) mCachePageInfo.get(activity);
            if (pageInfo != null) {
                pageInfo.extraInfo = mPageInfoProvider.getInfoByActivity(activity);
                PageLifecycleEventWithTime<Activity> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, ActivityLifecycleEvent.ON_PAUSE, time);
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            // noinspection unchecked
            PageInfo<Activity> pageInfo = (PageInfo<Activity>) mCachePageInfo.get(activity);
            if (pageInfo != null) {
                pageInfo.extraInfo = mPageInfoProvider.getInfoByActivity(activity);
                PageLifecycleEventWithTime<Activity> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, ActivityLifecycleEvent.ON_STOP, time);
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(final Activity activity) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            // noinspection unchecked
            PageInfo<Activity> pageInfo = (PageInfo<Activity>) mCachePageInfo.remove(activity);
            if (pageInfo != null) {
                pageInfo.extraInfo = mPageInfoProvider.getInfoByActivity(activity);
                PageLifecycleEventWithTime<Activity> lifecycleEvent = mPageLifecycleRecords.addEvent(pageInfo, ActivityLifecycleEvent.ON_DESTROY, time);
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
        mStartedActivity.remove(activity);
    }
}

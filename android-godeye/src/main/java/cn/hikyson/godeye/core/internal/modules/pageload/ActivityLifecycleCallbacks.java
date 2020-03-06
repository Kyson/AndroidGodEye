package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.HashSet;
import java.util.Set;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.ViewUtil;
import cn.hikyson.methodcanary.lib.MethodCanary;
import cn.hikyson.methodcanary.lib.MethodEvent;
import cn.hikyson.methodcanary.lib.OnPageLifecycleEventCallback;

public class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks, Engine, OnPageLifecycleEventCallback {

    private PageLifecycleRecords mPageLifecycleRecords;
    private PageInfoProvider mPageInfoProvider;
    private Producer<PageLifecycleEventInfo> mProducer;
    private Handler mHandler;
    private Set<Activity> mStartedActivity;

    ActivityLifecycleCallbacks(PageLifecycleRecords pageLifecycleRecords, PageInfoProvider pageInfoProvider, Producer<PageLifecycleEventInfo> producer, Handler handler) {
        mPageLifecycleRecords = pageLifecycleRecords;
        mPageInfoProvider = pageInfoProvider;
        mProducer = producer;
        mHandler = handler;
        mStartedActivity = new HashSet<>();
    }

    void onActivityLoad(final Activity activity) {
        onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_LOAD, true);
    }

    void onFragmentV4Load(final Fragment f) {
        onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_LOAD, true);
    }

    void onFragmentLoad(final android.app.Fragment f) {
        onFragmentLifecycleEvent(f, FragmentLifecycleEvent.ON_LOAD, true);
    }

    void onFragmentV4Show(final Fragment f) {
        onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_SHOW, false);
    }

    void onFragmentV4Hide(final Fragment f) {
        onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_HIDE, false);
    }

    void onFragmentShow(final android.app.Fragment f) {
        onFragmentLifecycleEvent(f, FragmentLifecycleEvent.ON_SHOW, false);
    }

    void onFragmentHide(final android.app.Fragment f) {
        onFragmentLifecycleEvent(f, FragmentLifecycleEvent.ON_HIDE, false);
    }

    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.getFragmentManager().registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks(ActivityLifecycleCallbacks.this), true);
        }
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacksV4(ActivityLifecycleCallbacks.this), true);
        }
        onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_CREATE, false);
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_START, false);
        if (!mStartedActivity.contains(activity)) {
            mStartedActivity.add(activity);
            ViewUtil.measureActivityDidDraw(activity, () -> {
                onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_DRAW, false);
            });
        }
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_RESUME, false);
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_PAUSE, false);
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_STOP, false);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(final Activity activity) {
        onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_DESTROY, false);
        mStartedActivity.remove(activity);
    }

    // method canary callback for lifecycle method cost
    @Override
    public void onLifecycleEvent(MethodEvent lifecycleMethodEvent, Object page) {
        mHandler.post(() -> {
            LifecycleEvent lifecycleEvent = null;
            PageInfo<?> pageInfo = null;
            if (page instanceof Activity) {
                pageInfo = new PageInfo<>(page, mPageInfoProvider.getInfoByActivity((Activity) page));
                lifecycleEvent = PageLifecycleMethodEventTypes.convert(PageType.ACTIVITY, lifecycleMethodEvent);
            } else if (page instanceof Fragment) {
                pageInfo = new PageInfo<>(page, mPageInfoProvider.getInfoByV4Fragment((Fragment) page));
                lifecycleEvent = PageLifecycleMethodEventTypes.convert(PageType.FRAGMENT, lifecycleMethodEvent);
            } else if (page instanceof android.app.Fragment) {
                pageInfo = new PageInfo<>(page, mPageInfoProvider.getInfoByFragment((android.app.Fragment) page));
                lifecycleEvent = PageLifecycleMethodEventTypes.convert(PageType.FRAGMENT, lifecycleMethodEvent);
            }
            if (pageInfo == null || lifecycleEvent == null) {
                return;
            }
            if (lifecycleMethodEvent.isEnter) {
                mPageLifecycleRecords.addMethodStartEvent(pageInfo, lifecycleEvent, lifecycleMethodEvent.eventTimeMillis);
            } else {
                PageLifecycleEventWithTime<?> pageLifecycleEventWithTime = mPageLifecycleRecords.addMethodEndEvent(pageInfo, lifecycleEvent, lifecycleMethodEvent.eventTimeMillis);
                if (pageLifecycleEventWithTime != null) {
                    mProducer.produce(new PageLifecycleEventInfo(pageInfo, pageLifecycleEventWithTime, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
                }
            }
        });
    }

    private void onActivityLifecycleEvent(Activity activity, LifecycleEvent e, boolean canNotRepeat) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            PageInfo<Activity> pageInfo = new PageInfo<>(activity, mPageInfoProvider.getInfoByActivity(activity));
            if (canNotRepeat && mPageLifecycleRecords.isExistEvent(pageInfo, e)) {
                return;
            }
            PageLifecycleEventWithTime<Activity> lifecycleEvent = mPageLifecycleRecords.addLifecycleEvent(pageInfo, e, time);
            if (lifecycleEvent != null) {
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    void onFragmentLifecycleEvent(android.app.Fragment fragment, LifecycleEvent e, boolean canNotRepeat) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            PageInfo<android.app.Fragment> pageInfo = new PageInfo<>(fragment, mPageInfoProvider.getInfoByFragment(fragment));
            if (canNotRepeat && mPageLifecycleRecords.isExistEvent(pageInfo, e)) {
                return;
            }
            PageLifecycleEventWithTime<android.app.Fragment> lifecycleEvent = mPageLifecycleRecords.addLifecycleEvent(pageInfo, e, time);
            if (lifecycleEvent != null) {
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    void onFragmentV4LifecycleEvent(Fragment fragment, LifecycleEvent e, boolean canNotRepeat) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            PageInfo<Fragment> pageInfo = new PageInfo<>(fragment, mPageInfoProvider.getInfoByV4Fragment(fragment));
            if (canNotRepeat && mPageLifecycleRecords.isExistEvent(pageInfo, e)) {
                return;
            }
            PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addLifecycleEvent(pageInfo, e, time);
            if (lifecycleEvent != null) {
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    @Override
    public void work() {
        GodEye.instance().getApplication().registerActivityLifecycleCallbacks(this);
        MethodCanary.get().addOnPageLifecycleEventCallback(this);
    }

    @Override
    public void shutdown() {
        GodEye.instance().getApplication().unregisterActivityLifecycleCallbacks(this);
        MethodCanary.get().removeOnPageLifecycleEventCallback(this);
    }
}

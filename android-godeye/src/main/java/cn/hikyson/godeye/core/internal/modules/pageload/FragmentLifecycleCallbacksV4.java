package cn.hikyson.godeye.core.internal.modules.pageload;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import cn.hikyson.godeye.core.utils.ViewUtil;

public class FragmentLifecycleCallbacksV4 extends FragmentManager.FragmentLifecycleCallbacks {
    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks;

    FragmentLifecycleCallbacksV4(ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        mActivityLifecycleCallbacks = activityLifecycleCallbacks;
    }

    @Override
    public void onFragmentAttached(FragmentManager fm, final Fragment f, Context context) {
        mActivityLifecycleCallbacks.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_ATTACH, false);
    }

    @Override
    public void onFragmentCreated(FragmentManager fm, final Fragment f, Bundle savedInstanceState) {
        mActivityLifecycleCallbacks.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_CREATE, false);
    }

    @Override
    public void onFragmentViewCreated(FragmentManager fm, final Fragment f, View v, Bundle savedInstanceState) {
        mActivityLifecycleCallbacks.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_VIEW_CREATE, false);
        ViewUtil.measureFragmentV4DidDraw(f, () -> {
            mActivityLifecycleCallbacks.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_DRAW, false);
        });
    }

    @Override
    public void onFragmentStarted(FragmentManager fm, final Fragment f) {
        mActivityLifecycleCallbacks.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_START, false);
    }

    @Override
    public void onFragmentResumed(FragmentManager fm, final Fragment f) {
        mActivityLifecycleCallbacks.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_RESUME, false);
    }

    @Override
    public void onFragmentPaused(FragmentManager fm, final Fragment f) {
        mActivityLifecycleCallbacks.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_PAUSE, false);
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, final Fragment f) {
        mActivityLifecycleCallbacks.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_STOP, false);
    }

    @Override
    public void onFragmentViewDestroyed(FragmentManager fm, final Fragment f) {
        mActivityLifecycleCallbacks.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_VIEW_DESTROY, false);
    }

    @Override
    public void onFragmentDestroyed(FragmentManager fm, final Fragment f) {
        mActivityLifecycleCallbacks.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_DESTROY, false);
    }

    @Override
    public void onFragmentDetached(FragmentManager fm, final Fragment f) {
        mActivityLifecycleCallbacks.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_DETACH, false);
    }
}

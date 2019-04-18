package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.DebuggerControl;
import com.squareup.leakcanary.GcTrigger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.leakcanary.internal.LeakCanaryInternals;

import cn.hikyson.godeye.core.helper.SimpleActivityLifecycleCallbacks;
import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.modules.leakdetector.debug.GodEyeDebugHeapDumpListener;
import cn.hikyson.godeye.core.internal.modules.leakdetector.debug.GodEyeDebugHeapDumper;
import cn.hikyson.godeye.core.internal.modules.leakdetector.release.GodEyeReleaseHeapDumpListener;
import cn.hikyson.godeye.core.internal.modules.leakdetector.release.GodEyeReleaseHeapDumper;
import cn.hikyson.godeye.core.internal.modules.leakdetector.watcher.AndroidOFragmentRefWatcher;
import cn.hikyson.godeye.core.internal.modules.leakdetector.watcher.SupportFragmentRefWatcher;

public class LeakEngine implements Engine {

    private LeakContext mConfig;
    private Application.ActivityLifecycleCallbacks lifecycleCallbacks;

    LeakEngine(LeakContext config) {
        mConfig = config;
    }

    private RefWatcher createDebugRefWatcher() {
        return LeakCanary.refWatcher(mConfig.application()).listenerServiceClass(GodEyeDisplayLeakService.class)
                .heapDumper(new GodEyeDebugHeapDumper(LeakCanaryInternals.getLeakDirectoryProvider(mConfig.application())))
                .heapDumpListener(new GodEyeDebugHeapDumpListener(mConfig.application(), mConfig.debugNotification()))
                .excludedRefs(AndroidExcludedRefs.createAppDefaults().build())
                .build();
    }

    private RefWatcher createReleaseRefWatcher() {
        return LeakCanary.refWatcher(mConfig.application()).listenerServiceClass(GodEyeDisplayLeakService.class)
                .debuggerControl(new DebuggerControl() {
                    @Override
                    public boolean isDebuggerAttached() {
                        return false;
                    }
                })
                .gcTrigger(GcTrigger.DEFAULT)
                .heapDumper(new GodEyeReleaseHeapDumper(mConfig.application()))
                .heapDumpListener(new GodEyeReleaseHeapDumpListener())
                .excludedRefs(AndroidExcludedRefs.createAppDefaults().build()).build();
    }


    @Override
    public void work() {
        final RefWatcher watcher = mConfig.debug() ? createDebugRefWatcher() : createReleaseRefWatcher();
        lifecycleCallbacks = new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    new AndroidOFragmentRefWatcher(watcher, mConfig.leakRefNameProvider()).watchFragments(activity);
                }
                new SupportFragmentRefWatcher(watcher, mConfig.leakRefNameProvider()).watchFragments(activity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                watcher.watch(activity, mConfig.leakRefNameProvider().convertActivity(activity));
            }
        };
        mConfig.application().registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    @Override
    public void shutdown() {
        if (lifecycleCallbacks != null) {
            mConfig.application().unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
        }
    }
}

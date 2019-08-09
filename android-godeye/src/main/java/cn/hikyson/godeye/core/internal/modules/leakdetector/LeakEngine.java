package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.DebuggerControl;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.leakcanary.internal.FragmentRefWatcher;
import com.squareup.leakcanary.internal.LeakCanaryInternals;

import cn.hikyson.godeye.core.helper.SimpleActivityLifecycleCallbacks;
import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.modules.leakdetector.debug.DebugHeapDumpListener;
import cn.hikyson.godeye.core.internal.modules.leakdetector.debug.DebugHeapDumper;
import cn.hikyson.godeye.core.internal.modules.leakdetector.release.ReleaseGcTrigger;
import cn.hikyson.godeye.core.internal.modules.leakdetector.release.ReleaseHeapDumpListener;
import cn.hikyson.godeye.core.internal.modules.leakdetector.release.ReleaseHeapDumper;
import cn.hikyson.godeye.core.internal.modules.leakdetector.watcher.AndroidOFragmentRefWatcher;
import cn.hikyson.godeye.core.internal.modules.leakdetector.watcher.SupportFragmentRefWatcher;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;

public class LeakEngine implements Engine {

    private LeakContext mConfig;
    private Application.ActivityLifecycleCallbacks lifecycleCallbacks;

    LeakEngine(LeakContext config) {
        mConfig = config;
    }

    private RefWatcher createDebugRefWatcher() {
        return LeakCanary.refWatcher(mConfig.application()).listenerServiceClass(GodEyeDisplayLeakService.class)
                .heapDumper(new DebugHeapDumper(LeakCanaryInternals.getLeakDirectoryProvider(mConfig.application())))
                .heapDumpListener(new DebugHeapDumpListener(mConfig.application(), mConfig.debugNotification()))
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
                .gcTrigger(new ReleaseGcTrigger())
                .heapDumper(new ReleaseHeapDumper(mConfig.application()))
                .heapDumpListener(new ReleaseHeapDumpListener())
                .excludedRefs(AndroidExcludedRefs.createAppDefaults().build()).build();
    }

    @Override
    public void work() {
        final RefWatcher watcher = mConfig.debug() ? createDebugRefWatcher() : createReleaseRefWatcher();
        FragmentRefWatcher appFragmentWatcher = null;
        if (SDK_INT >= O) {
            appFragmentWatcher = new AndroidOFragmentRefWatcher(watcher, mConfig.leakRefInfoProvider());
        }
        final FragmentRefWatcher finalAppFragmentWatcher = appFragmentWatcher;
        final FragmentRefWatcher supportFragmentWatcher = new SupportFragmentRefWatcher(watcher, mConfig.leakRefInfoProvider());
        lifecycleCallbacks = new SimpleActivityLifecycleCallbacks() {
            @RequiresApi(api = O)
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (finalAppFragmentWatcher != null) {
                    finalAppFragmentWatcher.watchFragments(activity);
                }
                supportFragmentWatcher.watchFragments(activity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LeakRefInfo leakRefInfo = mConfig.leakRefInfoProvider().getInfoByActivity(activity);
                if (!leakRefInfo.isExcludeRef()) {
                    watcher.watch(activity, LeakUtil.serialize(leakRefInfo));
                }
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

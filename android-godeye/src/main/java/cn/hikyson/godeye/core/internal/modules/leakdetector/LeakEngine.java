package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.leakcanary.internal.FragmentRefWatcher;
import com.squareup.leakcanary.internal.LeakCanaryInternals;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.helper.SimpleActivityLifecycleCallbacks;
import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.modules.leakdetector.debug.DebugHeapDumpListener;
import cn.hikyson.godeye.core.internal.modules.leakdetector.debug.DebugHeapDumper;
import cn.hikyson.godeye.core.internal.modules.leakdetector.release.ReleaseGcTrigger;
import cn.hikyson.godeye.core.internal.modules.leakdetector.release.ReleaseHeapDumpListener;
import cn.hikyson.godeye.core.internal.modules.leakdetector.release.ReleaseHeapDumper;
import cn.hikyson.godeye.core.internal.modules.leakdetector.watcher.AndroidOFragmentRefWatcher;
import cn.hikyson.godeye.core.internal.modules.leakdetector.watcher.SupportFragmentRefWatcher;
import cn.hikyson.godeye.core.utils.L;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;

public class LeakEngine implements Engine {

    private LeakConfig mConfig;
    private Application.ActivityLifecycleCallbacks lifecycleCallbacks;

    LeakEngine(LeakConfig config) {
        mConfig = config;
    }

    private RefWatcher createDebugRefWatcher() {
        return LeakCanary.refWatcher(GodEye.instance().getApplication()).listenerServiceClass(GodEyeDisplayLeakService.class)
                .heapDumper(new DebugHeapDumper(LeakCanaryInternals.getLeakDirectoryProvider(GodEye.instance().getApplication())))
                .heapDumpListener(new DebugHeapDumpListener(GodEye.instance().getApplication(), mConfig.debugNotification()))
                .excludedRefs(AndroidExcludedRefs.createAppDefaults().build())
                .build();
    }

    private RefWatcher createReleaseRefWatcher() {
        return LeakCanary.refWatcher(GodEye.instance().getApplication()).listenerServiceClass(GodEyeDisplayLeakService.class)
                .debuggerControl(() -> false)
                .gcTrigger(new ReleaseGcTrigger())
                .heapDumper(new ReleaseHeapDumper(GodEye.instance().getApplication()))
                .heapDumpListener(new ReleaseHeapDumpListener())
                .excludedRefs(AndroidExcludedRefs.createAppDefaults().build()).build();
    }

    @Override
    public void work() {
        LeakRefInfoProvider leakRefInfoProvider = new DefaultLeakRefInfoProvider();
        try {
            leakRefInfoProvider = (LeakRefInfoProvider) Class.forName(this.mConfig.leakRefInfoProvider()).newInstance();
        } catch (Throwable e) {
            L.e("Leak work warning, can not find LeakRefInfoProvider class, use DefaultLeakRefInfoProvider:" + e);
        }
        final RefWatcher watcher = mConfig.debug() ? createDebugRefWatcher() : createReleaseRefWatcher();
        FragmentRefWatcher appFragmentWatcher = null;
        if (SDK_INT >= O) {
            appFragmentWatcher = new AndroidOFragmentRefWatcher(watcher, leakRefInfoProvider);
        }
        final FragmentRefWatcher finalAppFragmentWatcher = appFragmentWatcher;
        final FragmentRefWatcher supportFragmentWatcher = new SupportFragmentRefWatcher(watcher, leakRefInfoProvider);
        LeakRefInfoProvider finalLeakRefInfoProvider = leakRefInfoProvider;
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
                LeakRefInfo leakRefInfo = finalLeakRefInfoProvider.getInfoByActivity(activity);
                if (!leakRefInfo.isExcludeRef()) {
                    watcher.watch(activity, LeakUtil.serialize(leakRefInfo));
                }
            }
        };
        GodEye.instance().getApplication().registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    @Override
    public void shutdown() {
        if (lifecycleCallbacks != null) {
            GodEye.instance().getApplication().unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
        }
    }
}

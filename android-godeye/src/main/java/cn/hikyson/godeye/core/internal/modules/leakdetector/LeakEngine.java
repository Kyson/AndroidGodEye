package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Application;

import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.LeakDirectoryProvider;
import com.squareup.leakcanary.RefWatcher;

import cn.hikyson.godeye.core.internal.Engine;

public class LeakEngine implements Engine {

    private Application mApplication;
    private LeakDirectoryProvider mLeakDirectoryProvider;

    public LeakEngine(LeakDirectoryProvider leakDirectoryProvider, Application application, boolean enableRelease) {
        mApplication = application;
        mLeakDirectoryProvider = leakDirectoryProvider;
    }

    private RefWatcher createDebugRefWatcher() {
        return LeakCanary.refWatcher(mApplication).listenerServiceClass(OutputLeakService.class)
                .heapDumper(new GodEyeHeapDumper(mApplication, mLeakDirectoryProvider))
                .heapDumpListener(new GodEyeHeapDumpListener(mApplication, OutputLeakService.class))
                .excludedRefs(AndroidExcludedRefs.createAppDefaults().build())
                .buildAndInstall();
    }

    @Override
    public void work() {
        createDebugRefWatcher();
    }

    @Override
    public void shutdown() {
    }
}

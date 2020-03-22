package cn.hikyson.android.godeye.leakcanary;

import android.app.Application;

import org.jetbrains.annotations.NotNull;

import cn.hikyson.godeye.core.internal.modules.leakdetector.Leak;
import leakcanary.AppWatcher;
import leakcanary.LeakCanary;
import leakcanary.OnHeapAnalyzedListener;
import shark.HeapAnalysis;

public class GodEyePluginLeakCanary {

    public static void install(Application application, final Leak leak) {
        LeakCanary.setConfig(new LeakCanary.Config().newBuilder()
                .requestWriteExternalStoragePermission(false)
                .dumpHeap(leak.config().debug())
                .onHeapAnalyzedListener(new OnHeapAnalyzedListener() {
                    @Override
                    public void onHeapAnalyzed(@NotNull HeapAnalysis heapAnalysis) {
                        leak.produce(heapAnalysis);
                    }
                }).build());
        AppWatcher.setConfig(new AppWatcher.Config().newBuilder().enabled(true).build());
        AppWatcher.INSTANCE.manualInstall(application);
    }

    public static void uninstall() {
        AppWatcher.setConfig(new AppWatcher.Config().newBuilder().enabled(false).build());
    }
}

package cn.hikyson.android.godeye.leakcanary;

import android.app.Application;

import androidx.annotation.Keep;
import androidx.core.util.Consumer;

import org.jetbrains.annotations.NotNull;

import cn.hikyson.godeye.core.internal.modules.leakdetector.Leak;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakInfo;
import cn.hikyson.godeye.core.utils.IteratorUtil;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import leakcanary.AppWatcher;
import leakcanary.LeakCanary;
import leakcanary.OnHeapAnalyzedListener;
import shark.HeapAnalysis;
import shark.HeapAnalysisFailure;
import shark.HeapAnalysisSuccess;

@Keep
public class GodEyePluginLeakCanary {
    @Keep
    public static void install(final Application application, final Leak leakModule) {
        ThreadUtil.sMain.execute(new Runnable() {
            @Override
            public void run() {
                AppWatcher.INSTANCE.manualInstall(application);
                LeakCanary.INSTANCE.showLeakDisplayActivityLauncherIcon(false);
                LeakCanary.setConfig(new LeakCanary.Config().newBuilder()
                        .requestWriteExternalStoragePermission(false)
                        .dumpHeap(true)
                        .onHeapAnalyzedListener(new OnHeapAnalyzedListener() {
                            @Override
                            public void onHeapAnalyzed(@NotNull HeapAnalysis heapAnalysis) {
                                if (heapAnalysis instanceof HeapAnalysisFailure) {
                                    L.w("GodEyePluginLeakCanary leak analysis failure:" + heapAnalysis.toString());
                                    return;
                                }
                                if (!(heapAnalysis instanceof HeapAnalysisSuccess)) {
                                    L.w("GodEyePluginLeakCanary leak analysis type error: " + heapAnalysis.getClass().getName());
                                    return;
                                }
                                final HeapAnalysisSuccess analysisSuccess = (HeapAnalysisSuccess) heapAnalysis;
                                IteratorUtil.forEach(analysisSuccess.getAllLeaks().iterator(), new Consumer<shark.Leak>() {
                                    @Override
                                    public void accept(shark.Leak leak) {
                                        leakModule.produce(new LeakInfo(analysisSuccess.getCreatedAtTimeMillis(), analysisSuccess.getAnalysisDurationMillis(), leak));
                                    }
                                });
                            }
                        }).build());
                AppWatcher.setConfig(new AppWatcher.Config().newBuilder().enabled(true).build());
            }
        });
    }

    @Keep
    public static void uninstall() {
        ThreadUtil.sMain.execute(new Runnable() {
            @Override
            public void run() {
                AppWatcher.setConfig(new AppWatcher.Config().newBuilder().enabled(false).build());
            }
        });
    }
}

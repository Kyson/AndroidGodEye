package cn.hikyson.godeye.core.internal.modules.leakdetector;

import org.jetbrains.annotations.NotNull;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import leakcanary.AppWatcher;
import leakcanary.LeakCanary;
import leakcanary.OnHeapAnalyzedListener;
import leakcanary.OnObjectRetainedListener;
import shark.HeapAnalysis;

public class Leak extends ProduceableSubject<HeapAnalysis> implements Install<LeakConfig> {
    private boolean mInstalled;
    private LeakConfig mConfig;

    @Override
    public boolean install(LeakConfig config) {
        if (mInstalled) {
            L.d("Leak already installed, ignore.");
            return true;
        }
        mConfig = config;
        LeakCanary.setConfig(new LeakCanary.Config().newBuilder()
                .requestWriteExternalStoragePermission(false)
                .dumpHeap(config.debug())
                .onHeapAnalyzedListener(new OnHeapAnalyzedListener() {
                    @Override
                    public void onHeapAnalyzed(@NotNull HeapAnalysis heapAnalysis) {
                        produce(heapAnalysis);
                    }
                }).build());
        AppWatcher.setConfig(new AppWatcher.Config().newBuilder().enabled(true).build());
        AppWatcher.INSTANCE.manualInstall(GodEye.instance().getApplication());
        AppWatcher.INSTANCE.getObjectWatcher().addOnObjectRetainedListener(new OnObjectRetainedListener() {
            @Override
            public void onObjectRetained() {

            }
        });
        mInstalled = true;
        L.d("Leak installed.");
        return true;
    }

    @Override
    public void uninstall() {
        if (!mInstalled) {
            L.d("Leak already uninstalled, ignore.");
            return;
        }
        AppWatcher.setConfig(new AppWatcher.Config().newBuilder().enabled(false).build());
        mConfig = null;
        mInstalled = false;
        L.d("Leak uninstalled.");
    }

    @Override
    public boolean isInstalled() {
        return this.mInstalled;
    }

    @Override
    public LeakConfig config() {
        return mConfig;
    }

    @Override
    protected Subject<HeapAnalysis> createSubject() {
        return ReplaySubject.create();
    }
}

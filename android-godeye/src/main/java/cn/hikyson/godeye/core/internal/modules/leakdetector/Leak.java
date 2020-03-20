package cn.hikyson.godeye.core.internal.modules.leakdetector;

import org.jetbrains.annotations.NotNull;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import leakcanary.AppWatcher;
import leakcanary.LeakCanary;
import leakcanary.OnHeapAnalyzedListener;
import leakcanary.internal.AppWatcherInstaller;
import shark.HeapAnalysis;

public class Leak extends ProduceableSubject<LeakQueue.LeakMemoryInfo> implements Install<LeakConfig> {
    public static final String LEAK_HANDLER = "godeye-leak";
    private boolean mInstalled;
    private LeakConfig mConfig;

    @Override
    public boolean install(LeakConfig config) {
        LeakCanary.setConfig(new LeakCanary.Config().newBuilder().dumpHeap(config.debug()).onHeapAnalyzedListener(new OnHeapAnalyzedListener() {
            @Override
            public void onHeapAnalyzed(@NotNull HeapAnalysis heapAnalysis) {

            }
        }).build());
        AppWatcher.setConfig(new AppWatcher.Config().newBuilder().enabled(true).build());
        AppWatcher.INSTANCE.manualInstall(GodEye.instance().getApplication());


        if (mInstalled) {
            L.d("Leak already installed, ignore.");
            return true;
        }
        mConfig = config;
        ThreadUtil.createIfNotExistHandler(LEAK_HANDLER);
        mLeakEngine = new LeakEngine(config);
        mLeakEngine.work();
        mInstalled = true;
        L.d("Leak installed.");
        return true;
    }

    @Override
    public void uninstall() {
        if (!mInstalled) {
            L.d("LeakDetector already uninstalled, ignore.");
            return;
        }
        if (mLeakEngine != null) {
            mLeakEngine.shutdown();
        }
        ThreadUtil.destoryHandler(LEAK_HANDLER);
        mConfig = null;
        mInstalled = false;
        L.d("LeakDetector uninstalled.");
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
    protected Subject<LeakQueue.LeakMemoryInfo> createSubject() {
        return ReplaySubject.create();
    }
}

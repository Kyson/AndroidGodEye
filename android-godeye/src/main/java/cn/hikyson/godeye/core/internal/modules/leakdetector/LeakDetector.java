package cn.hikyson.godeye.core.internal.modules.leakdetector;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

public class LeakDetector extends ProduceableSubject<LeakQueue.LeakMemoryInfo> implements Install<LeakContext> {

    private LeakEngine mLeakEngine;
    private boolean mInstalled;
    private LeakDetector() {
    }

    private static class InstanceHolder {
        private static LeakDetector sINSTANCE = new LeakDetector();
    }

    public static LeakDetector instance() {
        return LeakDetector.InstanceHolder.sINSTANCE;
    }

    private ExecutorService sSingleForLeakExecutor;

    @Override
    public synchronized void install(LeakContext config) {
        if (mInstalled) {
            L.d("crash already installed , ignore");
            return;
        }
        sSingleForLeakExecutor = Executors.newSingleThreadExecutor();
        mLeakEngine = new LeakEngine(config);
        mLeakEngine.work();
        mInstalled = true;
    }

    @Override
    public synchronized void uninstall() {
        if (mLeakEngine != null) {
            mLeakEngine.shutdown();
        }
        sSingleForLeakExecutor.shutdown();
        sSingleForLeakExecutor = null;
    }

    public ExecutorService getsSingleForLeakExecutor() {
        return sSingleForLeakExecutor;
    }
}


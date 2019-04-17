package cn.hikyson.godeye.core.internal.modules.leakdetector;

import com.squareup.leakcanary.LeakDirectoryProvider;
import com.squareup.leakcanary.internal.LeakCanaryInternals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;

public class LeakDetector extends ProduceableSubject<LeakQueue.LeakMemoryInfo> implements Install<LeakContext> {

    private LeakEngine mLeakEngine;

    private LeakDirectoryProvider mLeakDirectoryProvider;

    private volatile boolean mHasStarted;

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
        if (mHasStarted) {
            return;
        }
        sSingleForLeakExecutor = Executors.newSingleThreadExecutor();
        mLeakDirectoryProvider = LeakCanaryInternals.getLeakDirectoryProvider(config.application());
        mLeakEngine = new LeakEngine(mLeakDirectoryProvider, config.application(), config.enableRelease());
        mLeakEngine.work();
        mHasStarted = true;
    }

    @Override
    public synchronized void uninstall() {
        if (!mHasStarted) {
            return;
        }
        if (mLeakEngine != null) {
            mLeakEngine.shutdown();
        }
        sSingleForLeakExecutor.shutdown();
        sSingleForLeakExecutor = null;
    }

    ExecutorService getsSingleForLeakExecutor() {
        return sSingleForLeakExecutor;
    }

    LeakDirectoryProvider getLeakDirectoryProvider() {
        return mLeakDirectoryProvider;
    }
}


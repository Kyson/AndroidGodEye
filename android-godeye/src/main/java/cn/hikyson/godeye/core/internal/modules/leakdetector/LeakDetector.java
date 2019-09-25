package cn.hikyson.godeye.core.internal.modules.leakdetector;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

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
            L.d("LeakDetector already installed, ignore.");
            return;
        }
        sSingleForLeakExecutor = Executors.newSingleThreadExecutor(new ThreadUtil.NamedThreadFactory("godeye-leak-"));
        mLeakEngine = new LeakEngine(config);
        mLeakEngine.work();
        mInstalled = true;
        L.d("LeakDetector installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("LeakDetector already uninstalled, ignore.");
            return;
        }
        if (mLeakEngine != null) {
            mLeakEngine.shutdown();
        }
        if (sSingleForLeakExecutor != null) {
            sSingleForLeakExecutor.shutdown();
            sSingleForLeakExecutor = null;
        }
        mInstalled = false;
        L.d("LeakDetector uninstalled.");
    }

    public ExecutorService getsSingleForLeakExecutor() {
        return sSingleForLeakExecutor;
    }

    @Override
    protected Subject<LeakQueue.LeakMemoryInfo> createSubject() {
        return ReplaySubject.create();
    }
}


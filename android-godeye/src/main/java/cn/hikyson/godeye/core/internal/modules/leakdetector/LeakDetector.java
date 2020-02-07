package cn.hikyson.godeye.core.internal.modules.leakdetector;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class LeakDetector extends ProduceableSubject<LeakQueue.LeakMemoryInfo> implements Install<LeakConfig> {

    public static final String LEAK_HANDLER = "godeye-leak";
    private LeakEngine mLeakEngine;
    private boolean mInstalled;
    private LeakConfig mConfig;

    private LeakDetector() {
    }

    private static class InstanceHolder {
        private static LeakDetector sINSTANCE = new LeakDetector();
    }

    public static LeakDetector instance() {
        return LeakDetector.InstanceHolder.sINSTANCE;
    }

    @Override
    public synchronized void install(LeakConfig config) {
        if (mInstalled) {
            L.d("LeakDetector already installed, ignore.");
            return;
        }
        mConfig = config;
        ThreadUtil.createIfNotExistHandler(LEAK_HANDLER);
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
        ThreadUtil.destoryHandler(LEAK_HANDLER);
        mConfig = null;
        mInstalled = false;
        L.d("LeakDetector uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
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


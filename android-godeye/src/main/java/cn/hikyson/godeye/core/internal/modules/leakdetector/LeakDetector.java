package cn.hikyson.godeye.core.internal.modules.leakdetector;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

/**
 * @deprecated use {@link Leak } instead
 */
@Deprecated
public class LeakDetector extends ProduceableSubject<LeakQueue.LeakMemoryInfo> implements Install<LeakConfig> {

    private LeakDetector() {
    }

    private static class InstanceHolder {
        private static LeakDetector sINSTANCE = new LeakDetector();
    }

    public static LeakDetector instance() {
        return LeakDetector.InstanceHolder.sINSTANCE;
    }

    @Override
    public synchronized boolean install(LeakConfig config) {
        return false;
    }

    @Override
    public synchronized void uninstall() {
    }

    @Override
    public synchronized boolean isInstalled() {
        return false;
    }

    @Override
    public LeakConfig config() {
        return null;
    }

    @Override
    protected Subject<LeakQueue.LeakMemoryInfo> createSubject() {
        return ReplaySubject.create();
    }
}


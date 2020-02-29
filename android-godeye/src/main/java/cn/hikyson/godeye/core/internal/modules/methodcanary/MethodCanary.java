package cn.hikyson.godeye.core.internal.modules.methodcanary;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class MethodCanary extends ProduceableSubject<MethodsRecordInfo> implements Install<MethodCanaryConfig> {
    private boolean mInstalled = false;
    private MethodCanaryConfig mMethodCanaryContext;

    @Override
    public synchronized boolean install(final MethodCanaryConfig methodCanaryContext) {
        if (this.mInstalled) {
            L.d("MethodCanary already installed, ignore.");
            return true;
        }
        this.mMethodCanaryContext = methodCanaryContext;
        this.mInstalled = true;
        L.d("MethodCanary installed.");
        return true;
    }

    @Override
    public synchronized void uninstall() {
        if (!this.mInstalled) {
            L.d("MethodCanary already uninstalled, ignore.");
            return;
        }
        this.mMethodCanaryContext = null;
        this.mInstalled = false;
        L.d("MethodCanary uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return this.mInstalled;
    }

    @Override
    public MethodCanaryConfig config() {
        return mMethodCanaryContext;
    }

    public synchronized void startMonitor(String tag) {
        try {
            if (!isInstalled()) {
                L.d("MethodCanary start monitor fail, not installed.");
                return;
            }
            cn.hikyson.methodcanary.lib.MethodCanary.get().start(tag);
            L.d("MethodCanary start monitor success.");
        } catch (Exception e) {
            L.d("MethodCanary start monitor fail:" + e);
        }
    }

    public synchronized void stopMonitor(String tag) {
        try {
            if (!isInstalled()) {
                L.d("MethodCanary stop monitor fail, not installed.");
                return;
            }
            cn.hikyson.methodcanary.lib.MethodCanary.get().stop(tag
                    , new cn.hikyson.methodcanary.lib.MethodCanaryConfig(this.mMethodCanaryContext.lowCostMethodThresholdMillis() * 1000000), (sessionTag, startNanoTime, stopNanoTime, methodEventMap) -> {
                        long start0 = System.currentTimeMillis();
                        MethodsRecordInfo methodsRecordInfo = MethodCanaryConverter.convertToMethodsRecordInfo(startNanoTime, stopNanoTime, methodEventMap);
                        long start1 = System.currentTimeMillis();
                        MethodCanaryConverter.filter(methodsRecordInfo, this.mMethodCanaryContext);
                        long end = System.currentTimeMillis();
                        L.d(String.format("MethodCanary output success! cost %s ms, filter cost %s ms", end - start0, end - start1));
                        produce(methodsRecordInfo);
                    });
            L.d("MethodCanary stopped monitor and output processing...");
        } catch (Exception e) {
            L.d("MethodCanary stop monitor fail:" + e);
        }
    }

    public synchronized boolean isRunning(String tag) {
        return cn.hikyson.methodcanary.lib.MethodCanary.get().isRunning(tag);
    }

    @Override
    protected Subject<MethodsRecordInfo> createSubject() {
        return BehaviorSubject.create();
    }
}

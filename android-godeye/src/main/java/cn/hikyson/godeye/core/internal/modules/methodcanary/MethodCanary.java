package cn.hikyson.godeye.core.internal.modules.methodcanary;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.methodcanary.lib.MethodCanaryCallback;
import cn.hikyson.methodcanary.lib.MethodCanaryConfig;
import cn.hikyson.methodcanary.lib.MethodCanaryInject;
import cn.hikyson.methodcanary.lib.MethodEvent;
import cn.hikyson.methodcanary.lib.ThreadInfo;

public class MethodCanary extends ProduceableSubject<MethodsRecordInfo> implements Install<MethodCanaryContext> {
    private boolean mInstalled = false;
    private MethodCanaryContext mMethodCanaryContext;

    @Override
    public synchronized void install(final MethodCanaryContext methodCanaryContext) {
        if (mInstalled) {
            L.d("method canary already installed, ignore.");
            return;
        }
        MethodCanaryInject.install(MethodCanaryConfig.MethodCanaryConfigBuilder
                .aMethodCanaryConfig()
                .lowCostThreshold(methodCanaryContext.lowCostMethodThresholdMillis())
                .methodCanaryCallback(new MethodCanaryCallback() {
                    @Override
                    public void onStopped(long startTimeNanos, long stopTimeNanos) {

                    }

                    @Override
                    public void outputToMemory(long startTimeNanos, long stopTimeNanos, Map<ThreadInfo, List<MethodEvent>> methodEventMap) {
                        long start0 = System.currentTimeMillis();
                        MethodsRecordInfo methodsRecordInfo = MethodCanaryConverter.convertToMethodsRecordInfo(startTimeNanos, stopTimeNanos, methodEventMap);
                        long start1 = System.currentTimeMillis();
                        MethodCanaryConverter.filter(methodsRecordInfo, methodCanaryContext);
                        long end = System.currentTimeMillis();
                        L.d(String.format("MethodCanary outputToMemory cost %s ms, filter cost %s ms", end - start0, end - start1));
                        produce(methodsRecordInfo);
                    }
                }).build());
        this.mMethodCanaryContext = methodCanaryContext;
        this.mInstalled = true;
        L.d("method canary installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("method canary already uninstalled, ignore.");
            return;
        }
        this.mMethodCanaryContext = null;
        mInstalled = false;
        MethodCanaryInject.uninstall();
        L.d("method canary uninstalled.");
    }

    public synchronized MethodCanaryContext getMethodCanaryContext() {
        return mMethodCanaryContext;
    }

    public synchronized boolean isMonitoring() {
        return MethodCanaryInject.isMonitoring();
    }

    public synchronized boolean isInstalled() {
        return mInstalled;
    }

    public void startMonitor() {
        try {
            MethodCanaryInject.startMonitor();
            L.d("method canary  start monitor success.");
        } catch (Exception e) {
            L.d("method canary  start monitor fail:" + e);
        }
    }

    public void stopMonitor() {
        MethodCanaryInject.stopMonitor();
        L.d("method canary  stop monitor success.");
    }
}

package cn.hikyson.godeye.core.internal.modules.methodcanary;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.methodcanary.lib.MethodCanaryConfig;
import cn.hikyson.methodcanary.lib.MethodCanaryInject;
import cn.hikyson.methodcanary.lib.MethodCanaryOutputCallback;
import cn.hikyson.methodcanary.lib.MethodEvent;
import cn.hikyson.methodcanary.lib.ThreadInfo;

public class MethodCanary extends ProduceableSubject<MethodsRecordInfo> implements Install<MethodCanaryContext> {
    private boolean mInstalled = false;

    @Override
    public void install(final MethodCanaryContext methodCanaryContext) {
        if (mInstalled) {
            L.d("method canary already installed, ignore.");
            return;
        }
        MethodCanaryInject.install(MethodCanaryConfig.MethodCanaryConfigBuilder
                .aMethodCanaryConfig().app(methodCanaryContext.app())
                .methodEventThreshold(methodCanaryContext.methodEventCountThreshold())
                .methodCanaryOutputCallback(new MethodCanaryOutputCallback() {
                    @Override
                    public void output(long startTimeNanos, long stopTimeNanos, File methodEventsFile) {
                        long start = System.currentTimeMillis();
                        MethodsRecordInfo methodsRecordInfo = MethodCanaryConverter.convertToMethodsRecordInfo(startTimeNanos, stopTimeNanos, methodEventsFile);
                        methodsRecordInfo = MethodCanaryConverter.filterMethodsRecordInfo(methodsRecordInfo, methodCanaryContext);
                        L.d("convertToMethodsRecordInfo cost " + (System.currentTimeMillis() - start) + "ms");
                        produce(methodsRecordInfo);
                    }
                }).build());
        this.mInstalled = true;
        L.d("method canary installed.");
    }

    @Override
    public void uninstall() {
        if (!mInstalled) {
            L.d("method canary already uninstalled, ignore.");
            return;
        }
        mInstalled = false;
        MethodCanaryInject.uninstall();
        L.d("method canary uninstalled.");
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

package cn.hikyson.godeye.core.internal.modules.methodcanary;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.methodcanary.lib.MethodCanaryConfig;
import cn.hikyson.methodcanary.lib.MethodCanaryInject;
import cn.hikyson.methodcanary.lib.MethodCanaryOutputCallback;
import cn.hikyson.methodcanary.lib.MethodEvent;
import cn.hikyson.methodcanary.lib.ThreadInfo;

// TODO KYSON PRODUCE STH
public class MethodCanary extends ProduceableSubject implements Install<MethodCanaryContext> {

    @Override
    public void install(MethodCanaryContext methodCanaryContext) {
        MethodCanaryInject.install(MethodCanaryConfig.MethodCanaryConfigBuilder
                .aMethodCanaryConfig().app(methodCanaryContext.app())
                .methodEventThreshold(methodCanaryContext.methodEventCountThreshold())
                .methodCanaryOutputCallback(new MethodCanaryOutputCallback() {
                    @Override
                    public void output(Map<ThreadInfo, List<MethodEvent>> methodEventMap, File record) {
//                        produce();
                    }
                }).build());
    }

    @Override
    public void uninstall() {
        MethodCanaryInject.uninstall();
    }

    public void startMonitor() {
        try {
            MethodCanaryInject.startMonitor();
        } catch (Exception ignore) {
        }
    }

    public void stopMonitor() {
        MethodCanaryInject.stopMonitor();
    }
}

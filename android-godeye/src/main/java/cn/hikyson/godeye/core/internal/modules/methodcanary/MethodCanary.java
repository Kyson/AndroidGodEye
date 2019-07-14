package cn.hikyson.godeye.core.internal.modules.methodcanary;

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
import io.reactivex.annotations.Nullable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class MethodCanary extends ProduceableSubject<MethodsRecordInfo> implements Install<MethodCanaryContext> {
    private boolean mInstalled = false;
    private MethodCanaryContext mMethodCanaryContext;
    private Subject<String> mStatusSubject;

    @Override
    public synchronized void install(final MethodCanaryContext methodCanaryContext) {
        if (this.mInstalled) {
            L.d("method canary already installed, ignore.");
            return;
        }
        this.mStatusSubject = PublishSubject.create();
        MethodCanaryInject.install(MethodCanaryConfig.MethodCanaryConfigBuilder
                .aMethodCanaryConfig()
                .lowCostThreshold(methodCanaryContext.lowCostMethodThresholdMillis() * 1000000)
                .methodCanaryCallback(new MethodCanaryCallback() {
                    @Override
                    public void onStopped(long startTimeNanos, long stopTimeNanos) {
                    }

                    @Override
                    public void outputToMemory(final long startTimeNanos, final long stopTimeNanos, final Map<ThreadInfo, List<MethodEvent>> methodEventMap) {
                        Schedulers.computation().scheduleDirect(new Runnable() {
                            @Override
                            public void run() {
                                long start0 = System.currentTimeMillis();
                                MethodsRecordInfo methodsRecordInfo = MethodCanaryConverter.convertToMethodsRecordInfo(startTimeNanos, stopTimeNanos, methodEventMap);
                                long start1 = System.currentTimeMillis();
                                MethodCanaryConverter.filter(methodsRecordInfo, methodCanaryContext);
                                long end = System.currentTimeMillis();
                                L.d(String.format("MethodCanary outputToMemory cost %s ms, filter cost %s ms", end - start0, end - start1));
                                mStatusSubject.onNext("OUTPUT");
                                produce(methodsRecordInfo);
                            }
                        });
                    }
                }).build());
        this.mMethodCanaryContext = methodCanaryContext;
        this.mStatusSubject.onNext("INSTALLED");
        this.mInstalled = true;
        L.d("method canary installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (!this.mInstalled) {
            L.d("method canary already uninstalled, ignore.");
            return;
        }
        this.mMethodCanaryContext = null;
        MethodCanaryInject.uninstall();
        this.mStatusSubject.onNext("UNINSTALLED");
        this.mStatusSubject.onComplete();
        this.mStatusSubject = null;
        this.mInstalled = false;
        L.d("method canary uninstalled.");
    }

    public synchronized MethodCanaryContext getMethodCanaryContext() {
        return this.mMethodCanaryContext;
    }

    public synchronized boolean isMonitoring() {
        return MethodCanaryInject.isMonitoring();
    }

    public synchronized boolean isInstalled() {
        return this.mInstalled;
    }

    public void startMonitor() {
        try {
            MethodCanaryInject.startMonitor();
            if (this.mStatusSubject != null && !this.mStatusSubject.hasComplete() && !this.mStatusSubject.hasThrowable()) {
                this.mStatusSubject.onNext("STARTED");
            }
            L.d("method canary  start monitor success.");
        } catch (Exception e) {
            L.d("method canary  start monitor fail:" + e);
        }
    }

    public void stopMonitor() {
        MethodCanaryInject.stopMonitor();
        if (this.mStatusSubject != null && !this.mStatusSubject.hasComplete() && !this.mStatusSubject.hasThrowable()) {
            this.mStatusSubject.onNext("STOPPED");
        }
        L.d("method canary  stop monitor success.");
    }

    public @Nullable
    Subject<String> statusSubject() {
        return this.mStatusSubject;
    }
}

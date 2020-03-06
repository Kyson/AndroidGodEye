package cn.hikyson.godeye.core.internal.modules.methodcanary;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.JsonUtil;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.methodcanary.lib.MethodEvent;
import cn.hikyson.methodcanary.lib.ThreadInfo;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import okio.BufferedSink;
import okio.Okio;

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
            cn.hikyson.methodcanary.lib.MethodCanary.get().startMethodTracing(tag);
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
            cn.hikyson.methodcanary.lib.MethodCanary.get().stopMethodTracing(tag
                    , new cn.hikyson.methodcanary.lib.MethodCanaryConfig(this.mMethodCanaryContext.lowCostMethodThresholdMillis()), (sessionTag, startMillis, stopMillis, methodEventMap) -> {
                        long start0 = System.currentTimeMillis();
                        MethodsRecordInfo methodsRecordInfo = MethodCanaryConverter.convertToMethodsRecordInfo(startMillis, stopMillis, methodEventMap);
//                        recordToFile(methodEventMap, methodsRecordInfo);
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

    private static void recordToFile(Map<ThreadInfo, List<MethodEvent>> methodEventMap, MethodsRecordInfo methodsRecordInfo) {
        for (File file : GodEye.instance().getApplication().getExternalCacheDir().listFiles()) {
            file.delete();
        }
        File file = new File(GodEye.instance().getApplication().getExternalCacheDir(), "methodcanary_methodEventMap.txt");
        try {
            BufferedSink buffer = Okio.buffer(Okio.sink(file));
            buffer.writeUtf8(JsonUtil.toJson(methodEventMap)).flush();
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file2 = new File(GodEye.instance().getApplication().getExternalCacheDir(), "methodcanary_methodsRecordInfo.txt");
        try {
            BufferedSink buffer = Okio.buffer(Okio.sink(file2));
            buffer.writeUtf8(JsonUtil.toJson(methodsRecordInfo)).flush();
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized boolean isRunning(String tag) {
        return cn.hikyson.methodcanary.lib.MethodCanary.get().isMethodTraceRunning(tag);
    }

    @Override
    protected Subject<MethodsRecordInfo> createSubject() {
        return BehaviorSubject.create();
    }
}

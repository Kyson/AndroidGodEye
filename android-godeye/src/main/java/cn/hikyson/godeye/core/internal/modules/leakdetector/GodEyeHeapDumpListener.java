package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.AbstractAnalysisResultService;
import com.squareup.leakcanary.HeapDump;

public class GodEyeHeapDumpListener implements HeapDump.Listener {

    private final Context context;
    private final Class<? extends AbstractAnalysisResultService> listenerServiceClass;

    public GodEyeHeapDumpListener(@NonNull final Context context,
                                  @NonNull final Class<? extends AbstractAnalysisResultService> listenerServiceClass) {
        this.listenerServiceClass = listenerServiceClass;
        this.context = context;
    }

    @Override
    public void analyze(HeapDump heapDump) {
        GodEyeCanaryLog.d("开始分析...");
        OutputLeakService.sendOutputBroadcastStart(context, heapDump.referenceKey);
        GodEyeHeapAnalyzerService.runAnalysis(context, heapDump, listenerServiceClass);
    }
}

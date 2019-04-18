package cn.hikyson.godeye.core.internal.modules.leakdetector.debug;

import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.HeapDump;

import cn.hikyson.godeye.core.internal.modules.leakdetector.GodEyeCanaryLog;

public class GodEyeDebugHeapDumpListener implements HeapDump.Listener {

    private final Context context;

    public GodEyeDebugHeapDumpListener(@NonNull final Context context) {
        this.context = context;
    }

    @Override
    public void analyze(HeapDump heapDump) {
        GodEyeCanaryLog.d("开始分析...");
        GodEyeHeapAnalyzerService.runAnalysis(context, heapDump);
    }
}

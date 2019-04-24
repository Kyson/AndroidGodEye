package cn.hikyson.godeye.core.internal.modules.leakdetector.debug;

import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.HeapDump;

import cn.hikyson.godeye.core.internal.modules.leakdetector.GodEyeCanaryLog;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakDetector;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakQueue;

public class DebugHeapDumpListener implements HeapDump.Listener {

    private final Context context;
    private final boolean showNotification;

    public DebugHeapDumpListener(@NonNull final Context context, boolean showNotification) {
        this.context = context;
        this.showNotification = showNotification;
    }

    @Override
    public void analyze(HeapDump heapDump) {
        GodEyeCanaryLog.d("%s发生内存泄漏", heapDump.referenceName);
        LeakQueue.LeakMemoryInfo memoryInfo = new LeakQueue.LeakMemoryInfo(heapDump.referenceKey, heapDump.referenceName);
        memoryInfo.status = LeakQueue.LeakMemoryInfo.Status.STATUS_DETECT;
        memoryInfo.statusSummary = "LEAK_DETECTED";
        LeakDetector.instance().produce(memoryInfo);
        GodEyeCanaryLog.d("开始分析...");
        DebugHeapAnalyzerService.runAnalysis(context, heapDump, showNotification);
    }
}

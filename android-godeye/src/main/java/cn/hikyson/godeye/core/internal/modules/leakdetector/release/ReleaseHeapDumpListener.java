package cn.hikyson.godeye.core.internal.modules.leakdetector.release;

import com.squareup.leakcanary.HeapDump;

import cn.hikyson.godeye.core.internal.modules.leakdetector.GodEyeCanaryLog;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakDetector;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakQueue;

public class ReleaseHeapDumpListener implements HeapDump.Listener {
    @Override
    public void analyze(HeapDump heapDump) {
        GodEyeCanaryLog.d("%s发生内存泄漏", heapDump.referenceName);
        LeakQueue.LeakMemoryInfo memoryInfo = new LeakQueue.LeakMemoryInfo(heapDump.referenceKey);
        memoryInfo.extraInfo = heapDump.referenceName; // referenceName作为extraInfo承载变量，因为只能拿到referenceName
        memoryInfo.status = LeakQueue.LeakMemoryInfo.Status.STATUS_DETECT;
        memoryInfo.statusSummary = "LEAK_DETECTED";
        LeakDetector.instance().produce(memoryInfo);
    }
}

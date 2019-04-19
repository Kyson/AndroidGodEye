package cn.hikyson.godeye.core.internal.modules.leakdetector.release;

import com.squareup.leakcanary.HeapDump;

import cn.hikyson.godeye.core.internal.modules.leakdetector.GodEyeCanaryLog;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakDetector;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakQueue;

public class ReleaseHeapDumpListener implements HeapDump.Listener {
    @Override
    public void analyze(HeapDump heapDump) {
        GodEyeCanaryLog.d("%s发生内存泄漏", heapDump.referenceName);
        LeakQueue.LeakMemoryInfo memoryInfo = new LeakQueue.LeakMemoryInfo(heapDump.referenceName, heapDump.referenceName);
        memoryInfo.status = LeakQueue.LeakMemoryInfo.Status.STATUS_DETECT;
        LeakDetector.instance().produce(memoryInfo);
    }
}

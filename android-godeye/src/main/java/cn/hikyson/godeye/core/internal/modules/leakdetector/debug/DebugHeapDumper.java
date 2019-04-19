package cn.hikyson.godeye.core.internal.modules.leakdetector.debug;

import android.os.Debug;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.HeapDumper;
import com.squareup.leakcanary.LeakDirectoryProvider;

import java.io.File;

import cn.hikyson.godeye.core.internal.modules.leakdetector.GodEyeCanaryLog;

public class DebugHeapDumper implements HeapDumper {

    private final LeakDirectoryProvider leakDirectoryProvider;

    public DebugHeapDumper(
            @NonNull LeakDirectoryProvider leakDirectoryProvider) {
        this.leakDirectoryProvider = leakDirectoryProvider;
    }

    @Override
    public File dumpHeap() {
        GodEyeCanaryLog.d("发生泄漏");
        File heapDumpFile = leakDirectoryProvider.newHeapDumpFile();

        if (heapDumpFile == RETRY_LATER) {
            GodEyeCanaryLog.d("创建新的dump文件失败，RETRY_LATER");
            return RETRY_LATER;
        }

        GodEyeCanaryLog.d("创建了新的dump文件：" + heapDumpFile.getAbsolutePath());

        try {
            GodEyeCanaryLog.d("开始写入dump信息");
            Debug.dumpHprofData(heapDumpFile.getAbsolutePath());
            GodEyeCanaryLog.d("写入dump信息完成");
            return heapDumpFile;
        } catch (Exception e) {
            GodEyeCanaryLog.d(e, "Could not dump heap");
            return RETRY_LATER;
        }
    }
}

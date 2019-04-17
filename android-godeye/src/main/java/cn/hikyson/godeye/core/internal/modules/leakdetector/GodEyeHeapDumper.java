package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.content.Context;
import android.os.Debug;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.HeapDumper;
import com.squareup.leakcanary.LeakDirectoryProvider;

import java.io.File;

public class GodEyeHeapDumper implements HeapDumper {

    private final Context context;
    private final LeakDirectoryProvider leakDirectoryProvider;

    public GodEyeHeapDumper(@NonNull Context context,
                            @NonNull LeakDirectoryProvider leakDirectoryProvider) {
        this.leakDirectoryProvider = leakDirectoryProvider;
        this.context = context.getApplicationContext();
    }

    @Override
    public File dumpHeap() {
        GodEyeCanaryLog.d("发生泄漏");
        File heapDumpFile = leakDirectoryProvider.newHeapDumpFile();

        if (heapDumpFile == RETRY_LATER) {
            GodEyeCanaryLog.d("创建新的dump文件失败，RETRY_LATER");
            OutputLeakService.sendOutputBroadcastRetry(context);
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
            OutputLeakService.sendOutputBroadcastRetry(context);
            return RETRY_LATER;
        }
    }
}

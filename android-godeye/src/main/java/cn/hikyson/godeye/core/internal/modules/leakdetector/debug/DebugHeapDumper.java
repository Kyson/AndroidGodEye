package cn.hikyson.godeye.core.internal.modules.leakdetector.debug;

import android.os.Debug;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.HeapDumper;
import com.squareup.leakcanary.LeakDirectoryProvider;

import java.io.File;

import cn.hikyson.godeye.core.utils.L;

public class DebugHeapDumper implements HeapDumper {

    private final LeakDirectoryProvider leakDirectoryProvider;

    public DebugHeapDumper(
            @NonNull LeakDirectoryProvider leakDirectoryProvider) {
        this.leakDirectoryProvider = leakDirectoryProvider;
    }

    @Override
    public File dumpHeap() {
        L.d("LeakDetector dumpHeap starting.");
        File heapDumpFile = leakDirectoryProvider.newHeapDumpFile();

        if (heapDumpFile == RETRY_LATER) {
            L.d("LeakDetector dumpHeap create file fail，RETRY_LATER.");
            return RETRY_LATER;
        }
        try {
            Debug.dumpHprofData(heapDumpFile.getAbsolutePath());
            L.d("LeakDetector dumpHeap success.");
            return heapDumpFile;
        } catch (Exception e) {
            L.d("LeakDetector dumpHeap fail, RETRY_LATER.");
            return RETRY_LATER;
        }
    }
}

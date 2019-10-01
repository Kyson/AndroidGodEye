package cn.hikyson.godeye.core.internal.modules.leakdetector.release;

import android.content.Context;

import com.squareup.leakcanary.HeapDumper;

import java.io.File;

import cn.hikyson.godeye.core.utils.L;

public class ReleaseHeapDumper implements HeapDumper {

    private File mFile;

    public ReleaseHeapDumper(Context context) {
        mFile = new File(context.getCacheDir(), "leakcanary");
    }

    @Override
    public File dumpHeap() {
        //只返回空的file，不dump文件
        L.d("LeakDetector release dumpHeap done.");
        return mFile;
    }
}

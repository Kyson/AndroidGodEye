package cn.hikyson.godeye.core.internal.modules.leakdetector.release;

import android.content.Context;

import com.squareup.leakcanary.HeapDumper;

import java.io.File;

public class ReleaseHeapDumper implements HeapDumper {

    private File mFile;

    public ReleaseHeapDumper(Context context) {
        mFile = new File(context.getCacheDir(), "leakcanary");
    }

    @Override
    public File dumpHeap() {
        //只返回空的file，不dump文件
        return mFile;
    }
}

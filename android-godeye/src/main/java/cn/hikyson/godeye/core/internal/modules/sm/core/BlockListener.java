package cn.hikyson.godeye.core.internal.modules.sm.core;

import android.content.Context;
import androidx.annotation.WorkerThread;


public interface BlockListener {
    void onStart(Context context);

    void onStop(Context context);

    /**
     * 短卡顿
     *
     * @param context
     * @param shortBlockInfo
     */
    @WorkerThread
    void onShortBlock(Context context, ShortBlockInfo shortBlockInfo);

    /**
     * 长卡顿 当认为block的时候调用 在线程obtain-dump中
     *
     * @param context
     * @param blockInfo
     */
    @WorkerThread
    void onLongBlock(Context context, LongBlockInfo blockInfo);
}

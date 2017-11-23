package cn.hikyson.godeye.internal.modules.sm.core;

import android.content.Context;
import android.support.annotation.WorkerThread;


public interface BlockInterceptor {
    //TODO KYSON 暴露出去
    void onStart(Context context);

    //TODO KYSON 暴露出去
    void onStop(Context context);

    /**
     * 短卡顿
     *
     * @param context
     * @param blockTimeMillis
     */
    @WorkerThread
    void onShortBlock(Context context, long blockTimeMillis);

    /**
     * 长卡顿 当认为block的时候调用 在线程obtain-dump中
     *
     * @param context
     * @param blockInfo
     */
    @WorkerThread
    void onLongBlock(Context context, LongBlockInfo blockInfo);
}

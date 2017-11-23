package cn.hikyson.godeye.internal.modules.memory;

import java.util.concurrent.Callable;

import io.reactivex.Observable;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class Heap implements Snapshotable<HeapInfo> {
    /**
     * 获取应用dalvik内存信息
     * 耗时忽略不计
     *
     * @return dalvik堆内存KB
     */
    private static HeapInfo getAppHeapInfo() {
        Runtime runtime = Runtime.getRuntime();
        HeapInfo heapInfo = new HeapInfo();
        heapInfo.freeMemKb = runtime.freeMemory() / 1024;
        heapInfo.maxMemKb = Runtime.getRuntime().maxMemory() / 1024;
        heapInfo.allocatedKb = (Runtime.getRuntime().totalMemory() - runtime.freeMemory()) / 1024;
        return heapInfo;
    }

    @Override
    public Observable<HeapInfo> snapshot() {
        return Observable.fromCallable(new Callable<HeapInfo>() {
            @Override
            public HeapInfo call() throws Exception {
                return getAppHeapInfo();
            }
        });
    }
}

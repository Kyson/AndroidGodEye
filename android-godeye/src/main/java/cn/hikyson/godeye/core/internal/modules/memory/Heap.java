package cn.hikyson.godeye.core.internal.modules.memory;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * heap模块
 * 安装卸载可以任意线程
 * 发射数据在子线程
 * Created by kysonchao on 2017/11/22.
 */
public class Heap extends ProduceableSubject<HeapInfo> implements Install<HeapConfig> {
    private HeapEngine mHeapEngine;
    private HeapConfig mConfig;

    @Override
    public synchronized boolean install(HeapConfig heapContext) {
        if (mHeapEngine != null) {
            L.d("Heap already installed, ignore.");
            return true;
        }
        mConfig = heapContext;
        mHeapEngine = new HeapEngine(this, heapContext.intervalMillis());
        mHeapEngine.work();
        L.d("Heap installed.");
        return true;
    }

    @Override
    public synchronized void uninstall() {
        if (mHeapEngine == null) {
            L.d("Heap already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        mHeapEngine.shutdown();
        mHeapEngine = null;
        L.d("Heap uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mHeapEngine != null;
    }

    @Override
    public HeapConfig config() {
        return mConfig;
    }
}

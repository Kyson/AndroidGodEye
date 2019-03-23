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
public class Heap extends ProduceableSubject<HeapInfo> implements Install<HeapContext> {
    private HeapEngine mHeapEngine;

    @Override
    public synchronized void install(HeapContext heapContext) {
        if (mHeapEngine != null) {
            L.d("heap already installed, ignore.");
            return;
        }
        mHeapEngine = new HeapEngine(this, heapContext.intervalMillis());
        mHeapEngine.work();
        L.d("heap installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (mHeapEngine == null) {
            L.d("heap already uninstalled, ignore.");
            return;
        }
        mHeapEngine.shutdown();
        mHeapEngine = null;
        L.d("heap uninstalled.");
    }
}

package cn.hikyson.godeye.internal.modules.memory;

import cn.hikyson.godeye.internal.Install;
import cn.hikyson.godeye.internal.ProduceableConsumer;
import cn.hikyson.godeye.utils.L;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class Heap extends ProduceableConsumer<HeapInfo> implements Install<Long> {
    private HeapEngine mHeapEngine;

    public synchronized void install() {
        install(1000L);
    }

    @Override
    public synchronized void install(Long intervalMillis) {
        if (mHeapEngine != null) {
            L.d("heap already installed, ignore.");
            return;
        }
        mHeapEngine = new HeapEngine(this, intervalMillis);
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

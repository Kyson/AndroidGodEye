package cn.hikyson.godeye.core.internal.modules.memory;


import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * ram模块
 * 安装卸载可以任意线程
 * 发射数据在子线程
 * Created by kysonchao on 2017/11/22.
 */
public class Ram extends ProduceableSubject<RamInfo> implements Install<RamContext> {
    private RamEngine mRamEngine;

    @Override
    public synchronized void install(RamContext config) {
        if (mRamEngine != null) {
            L.d("ram already installed, ignore.");
            return;
        }
        mRamEngine = new RamEngine(config.context(), this, config.intervalMillis());
        mRamEngine.work();
        L.d("ram installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (mRamEngine == null) {
            L.d("ram already uninstalled, ignore.");
            return;
        }
        mRamEngine.shutdown();
        mRamEngine = null;
        L.d("ram uninstalled.");
    }
}

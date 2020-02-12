package cn.hikyson.godeye.core.internal.modules.memory;


import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * ram模块
 * 安装卸载可以任意线程
 * 发射数据在子线程
 * Created by kysonchao on 2017/11/22.
 */
public class Ram extends ProduceableSubject<RamInfo> implements Install<RamConfig> {
    private RamEngine mRamEngine;
    private RamConfig mConfig;

    @Override
    public synchronized boolean install(RamConfig config) {
        if (mRamEngine != null) {
            L.d("Ram already installed, ignore.");
            return true;
        }
        mConfig = config;
        mRamEngine = new RamEngine(GodEye.instance().getApplication(), this, config.intervalMillis());
        mRamEngine.work();
        L.d("Ram installed.");
        return true;
    }

    @Override
    public synchronized void uninstall() {
        if (mRamEngine == null) {
            L.d("Ram already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        mRamEngine.shutdown();
        mRamEngine = null;
        L.d("Ram uninstalled.");
    }


    @Override
    public synchronized boolean isInstalled() {
        return mRamEngine != null;
    }

    @Override
    public RamConfig config() {
        return mConfig;
    }
}

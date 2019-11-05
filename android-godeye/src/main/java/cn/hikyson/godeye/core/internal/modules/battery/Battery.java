package cn.hikyson.godeye.core.internal.modules.battery;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * 电池模块
 * 安装卸载可以任意线程
 * 发射数据子线程和主线程都有可能
 */
public class Battery extends ProduceableSubject<BatteryInfo> implements Install<BatteryContext> {
    private BatteryEngine mBatteryEngine;
    private BatteryContext mConfig;

    /**
     * 安装电池模块，任意线程
     *
     * @param config
     */
    @Override
    public synchronized void install(BatteryContext config) {
        if (mBatteryEngine != null) {
            L.d("Battery already installed, ignore.");
            return;
        }
        mConfig = config;
        mBatteryEngine = new BatteryEngine(config.context(), this);
        mBatteryEngine.work();
        L.d("Battery installed.");
    }

    /**
     * 卸载电池模块，任意线程
     */
    @Override
    public synchronized void uninstall() {
        if (mBatteryEngine == null) {
            L.d("Battery already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        mBatteryEngine.shutdown();
        mBatteryEngine = null;
        L.d("Battery uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mBatteryEngine != null;
    }

    @Override
    public BatteryContext config() {
        return mConfig;
    }

    @Override
    protected Subject<BatteryInfo> createSubject() {
        return BehaviorSubject.create();
    }
}

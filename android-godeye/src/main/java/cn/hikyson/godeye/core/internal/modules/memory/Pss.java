package cn.hikyson.godeye.core.internal.modules.memory;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * pss模块
 * 安装卸载可以任意线程
 * 发射数据在子线程
 * Created by kysonchao on 2017/11/22.
 */
public class Pss extends ProduceableSubject<PssInfo> implements Install<PssConfig> {
    private PssEngine mPssEngine;
    private PssConfig mConfig;

    @Override
    public synchronized boolean install(PssConfig config) {
        if (mPssEngine != null) {
            L.d("Pss already installed, ignore.");
            return true;
        }
        mConfig = config;
        mPssEngine = new PssEngine(GodEye.instance().getApplication(), this, config.intervalMillis());
        mPssEngine.work();
        L.d("Pss installed.");
        return true;
    }

    @Override
    public synchronized void uninstall() {
        if (mPssEngine == null) {
            L.d("Pss already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        mPssEngine.shutdown();
        mPssEngine = null;
        L.d("Pss uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mPssEngine != null;
    }

    @Override
    public PssConfig config() {
        return mConfig;
    }
}
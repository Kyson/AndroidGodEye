package cn.hikyson.godeye.core.internal.modules.memory;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * pss模块
 * 安装卸载可以任意线程
 * 发射数据在子线程
 * Created by kysonchao on 2017/11/22.
 */
public class Pss extends ProduceableSubject<PssInfo> implements Install<PssContext> {
    private PssEngine mPssEngine;
    private PssContext mConfig;

    @Override
    public synchronized void install(PssContext config) {
        if (mPssEngine != null) {
            L.d("Pss already installed, ignore.");
            return;
        }
        mConfig = config;
        mPssEngine = new PssEngine(config.context(), this, config.intervalMillis());
        mPssEngine.work();
        L.d("Pss installed.");
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
    public PssContext config() {
        return mConfig;
    }
}
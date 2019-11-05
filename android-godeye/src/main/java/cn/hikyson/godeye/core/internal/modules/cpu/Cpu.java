package cn.hikyson.godeye.core.internal.modules.cpu;


import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * cpu模块
 * 安装卸载可以任意线程
 * 发射数据在子线程
 * Created by kysonchao on 2017/5/19.
 */
public class Cpu extends ProduceableSubject<CpuInfo> implements Install<CpuContext> {
    private CpuEngine mCpuEngine;
    private CpuContext mConfig;

    @Override
    public synchronized void install(CpuContext config) {
        if (mCpuEngine != null) {
            L.d("Cpu already installed, ignore.");
            return;
        }
        mConfig = config;
        mCpuEngine = new CpuEngine(this, config.intervalMillis());
        mCpuEngine.work();
        L.d("Cpu installed");
    }

    @Override
    public synchronized void uninstall() {
        if (mCpuEngine == null) {
            L.d("Cpu already uninstalled , ignore.");
            return;
        }
        mConfig= null;
        mCpuEngine.shutdown();
        mCpuEngine = null;
        L.d("Cpu uninstalled");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mCpuEngine != null;
    }

    @Override
    public CpuContext config() {
        return mConfig;
    }
}

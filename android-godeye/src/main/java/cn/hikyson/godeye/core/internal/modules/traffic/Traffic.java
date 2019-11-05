package cn.hikyson.godeye.core.internal.modules.traffic;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * 流量模块
 * 安装卸载可以在任意模块
 * <p>
 * Created by kysonchao on 2017/5/22.
 */
public class Traffic extends ProduceableSubject<TrafficInfo> implements Install<TrafficContext> {
    private TrafficEngine mTrafficEngine;
    private TrafficContext mConfig;

    @Override
    public synchronized void install(TrafficContext config) {
        if (mTrafficEngine != null) {
            L.d("Traffic already installed, ignore.");
            return;
        }
        mConfig = config;
        mTrafficEngine = new TrafficEngine(this, config.intervalMillis(), config.sampleMillis());
        mTrafficEngine.work();
        L.d("Traffic installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (mTrafficEngine == null) {
            L.d("Traffic already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        mTrafficEngine.shutdown();
        mTrafficEngine = null;
        L.d("Traffic uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mTrafficEngine != null;
    }

    @Override
    public TrafficContext config() {
        return mConfig;
    }
}

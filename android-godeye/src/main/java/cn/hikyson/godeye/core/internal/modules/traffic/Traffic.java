package cn.hikyson.godeye.core.internal.modules.traffic;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * 流量模块
 * 安装卸载可以在任意模块
 *
 * Created by kysonchao on 2017/5/22.
 */
public class Traffic extends ProduceableSubject<TrafficInfo> implements Install<TrafficContext> {
    private TrafficEngine mTrafficEngine;

    @Override
    public synchronized void install(TrafficContext config) {
        if (mTrafficEngine != null) {
            L.d("Traffic already installed, ignore.");
            return;
        }
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
        mTrafficEngine.shutdown();
        mTrafficEngine = null;
        L.d("Traffic uninstalled.");
    }
}

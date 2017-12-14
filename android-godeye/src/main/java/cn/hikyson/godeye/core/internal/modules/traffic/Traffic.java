package cn.hikyson.godeye.core.internal.modules.traffic;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * 流量信息获取
 * Created by kysonchao on 2017/5/22.
 */
public class Traffic extends ProduceableSubject<TrafficInfo> implements Install<TrafficContext> {
    private TrafficEngine mTrafficEngine;

    public synchronized void install() {
        install(new TrafficContextImpl());
    }

    @Override
    public synchronized void install(TrafficContext config) {
        if (mTrafficEngine != null) {
            L.d("traffic already installed , ignore.");
            return;
        }
        mTrafficEngine = new TrafficEngine(this, config.intervalMillis(), config.sampleMillis());
        mTrafficEngine.work();
        L.d("traffic installed");
    }

    @Override
    public synchronized void uninstall() {
        if (mTrafficEngine == null) {
            L.d("traffic already uninstalled , ignore.");
            return;
        }
        mTrafficEngine.shutdown();
        mTrafficEngine = null;
        L.d("traffic uninstalled");
    }
}

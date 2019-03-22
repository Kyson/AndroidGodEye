package cn.hikyson.godeye.core.internal.modules.fps;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * fps模块可能会影响ui性能
 * Created by kysonchao on 2017/11/22.
 */
public class Fps extends ProduceableSubject<FpsInfo> implements Install<FpsContext> {
    private FpsEngine mFpsEngine;

    @Override
    public synchronized void install(FpsContext config) {
        if (mFpsEngine != null) {
            L.d("fps already installed, ignore.");
            return;
        }
        mFpsEngine = new FpsEngine(config.context(), this, config.intervalMillis());
        mFpsEngine.work();
        L.d("fps installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (mFpsEngine == null) {
            L.d("fps already uninstalled, ignore.");
            return;
        }
        mFpsEngine.shutdown();
        mFpsEngine = null;
        L.d("fps uninstalled.");
    }
}

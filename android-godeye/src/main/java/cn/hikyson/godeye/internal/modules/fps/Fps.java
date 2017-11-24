package cn.hikyson.godeye.internal.modules.fps;

import android.content.Context;

import cn.hikyson.godeye.internal.Engine;
import cn.hikyson.godeye.internal.Install;
import cn.hikyson.godeye.internal.ProduceableConsumer;
import cn.hikyson.godeye.utils.L;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class Fps extends ProduceableConsumer<FpsInfo> implements Install<FpsContext> {
    private FpsEngine mFpsEngine;

    public synchronized void install(Context context) {
        install(new FpsContextImpl(context));
    }

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

package cn.hikyson.godeye.core.internal.modules.memory;

import android.content.Context;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableConsumer;
import cn.hikyson.godeye.core.utils.L;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class Pss extends ProduceableConsumer<PssInfo> implements Install<PssContext> {
    private PssEngine mPssEngine;

    public synchronized void install(Context context) {
        install(new PssContextImpl(context));
    }

    @Override
    public synchronized void install(PssContext config) {
        if (mPssEngine != null) {
            L.d("pss already installed, ignore.");
            return;
        }
        mPssEngine = new PssEngine(config.context(), this, config.intervalMillis());
        mPssEngine.work();
        L.d("pss installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (mPssEngine == null) {
            L.d("pss already uninstalled, ignore.");
            return;
        }
        mPssEngine.shutdown();
        mPssEngine = null;
        L.d("pss uninstalled.");
    }
}

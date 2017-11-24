package cn.hikyson.godeye.internal.modules.memory;

import android.content.Context;

import cn.hikyson.godeye.internal.Install;
import cn.hikyson.godeye.internal.ProduceableConsumer;
import cn.hikyson.godeye.utils.L;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class Ram extends ProduceableConsumer<RamInfo> implements Install<RamContext> {
    private RamEngine mRamEngine;

    public synchronized void install(Context context) {
        install(new RamContextImpl(context));
    }

    @Override
    public synchronized void install(RamContext config) {
        if (mRamEngine != null) {
            L.d("ram already installed, ignore.");
            return;
        }
        mRamEngine = new RamEngine(config.context(), this, config.intervalMillis());
        mRamEngine.work();
        L.d("ram installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (mRamEngine == null) {
            L.d("ram already uninstalled, ignore.");
            return;
        }
        mRamEngine.shutdown();
        mRamEngine = null;
        L.d("ram uninstalled.");
    }
}

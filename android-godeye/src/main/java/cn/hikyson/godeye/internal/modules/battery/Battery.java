package cn.hikyson.godeye.internal.modules.battery;

import android.content.Context;

import cn.hikyson.godeye.internal.Install;
import cn.hikyson.godeye.internal.ProduceableConsumer;
import cn.hikyson.godeye.utils.L;


public class Battery extends ProduceableConsumer<BatteryInfo> implements Install<BatteryContext> {
    private BatteryEngine mBatteryEngine;

    public synchronized void install(Context context) {
        install(new BatteryContextImpl(context));
    }

    @Override
    public synchronized void install(BatteryContext config) {
        if (mBatteryEngine != null) {
            L.d("battery already installed, ignore.");
            return;
        }
        mBatteryEngine = new BatteryEngine(config.context(), this, config.intervalMillis());
        mBatteryEngine.work();
        L.d("battery installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (mBatteryEngine == null) {
            L.d("battery already uninstalled, ignore.");
            return;
        }
        mBatteryEngine.shutdown();
        mBatteryEngine = null;
        L.d("battery uninstalled.");
    }
}

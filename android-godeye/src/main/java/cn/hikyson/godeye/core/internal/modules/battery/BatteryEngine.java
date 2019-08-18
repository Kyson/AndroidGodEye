package cn.hikyson.godeye.core.internal.modules.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class BatteryEngine implements Engine {
    private Context mContext;
    private Producer<BatteryInfo> mProducer;
    private BatteryChangeReceiver mBatteryChangeReceiver;

    public BatteryEngine(Context context, Producer<BatteryInfo> producer) {
        this.mContext = context;
        mProducer = producer;
    }

    /**
     * 任意线程
     * 不保证线程安全，由外部保证
     */
    @Override
    public void work() {
        if (mBatteryChangeReceiver == null) {
            mBatteryChangeReceiver = new BatteryChangeReceiver();
            mBatteryChangeReceiver.setBatteryInfoProducer(mProducer);
            mContext.registerReceiver(mBatteryChangeReceiver, BatteryIntentFilterHolder.BATTERY_INTENT_FILTER);
        }
    }

    /**
     * 任意线程
     * 不保证线程安全，由外部保证
     */
    @Override
    public void shutdown() {
        mContext.unregisterReceiver(mBatteryChangeReceiver);
        mBatteryChangeReceiver = null;
    }

    private static final class BatteryIntentFilterHolder {
        private static final IntentFilter BATTERY_INTENT_FILTER = new IntentFilter();

        static {
            BATTERY_INTENT_FILTER.addAction(Intent.ACTION_BATTERY_CHANGED);
            BATTERY_INTENT_FILTER.addAction(Intent.ACTION_BATTERY_LOW);
            BATTERY_INTENT_FILTER.addAction(Intent.ACTION_BATTERY_OKAY);
        }
    }
}

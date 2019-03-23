package cn.hikyson.godeye.core.internal.modules.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;

public class BatteryChangeReceiver extends BroadcastReceiver {
    private Producer<BatteryInfo> mBatteryInfoProducer;

    public void setBatteryInfoProducer(Producer<BatteryInfo> batteryInfoProducer) {
        mBatteryInfoProducer = batteryInfoProducer;
    }

    @Override
    public void onReceive(Context context, final Intent batteryInfoIntent) {
        ThreadUtil.sComputationScheduler.scheduleDirect(new Runnable() {
            @Override
            public void run() {
                try {
                    BatteryInfo batteryInfo = new BatteryInfo();
                    batteryInfo.status = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager
                            .BATTERY_STATUS_UNKNOWN);
                    batteryInfo.health = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager
                            .BATTERY_HEALTH_UNKNOWN);
                    batteryInfo.present = batteryInfoIntent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
                    batteryInfo.level = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                    batteryInfo.scale = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                    batteryInfo.plugged = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
                    batteryInfo.voltage = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
                    batteryInfo.temperature = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
                    batteryInfo.technology = batteryInfoIntent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
                    mBatteryInfoProducer.produce(batteryInfo);
                } catch (Throwable e) {
                    L.e(String.valueOf(e));
                }
            }
        });

    }
}

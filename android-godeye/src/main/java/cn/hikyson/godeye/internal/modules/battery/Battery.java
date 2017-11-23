package cn.hikyson.godeye.internal.modules.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import cn.hikyson.godeye.internal.ProduceableConsumer;


public class Battery extends ProduceableConsumer<BatteryInfo> {

    public Battery() {
    }

    private static final class BatteryIntentFilterHolder {
        private static final IntentFilter BATTERY_INTENT_FILTER = new IntentFilter();

        static {
            BATTERY_INTENT_FILTER.addAction(Intent.ACTION_BATTERY_CHANGED);
            BATTERY_INTENT_FILTER.addAction(Intent.ACTION_BATTERY_LOW);
            BATTERY_INTENT_FILTER.addAction(Intent.ACTION_BATTERY_OKAY);
        }
    }

    /**
     * 获取电池信息
     *
     * @param context
     * @return 获取电池信息，或者invalid如果获取失败
     */
    private static BatteryInfo getBatteryInfo(Context context) {
        try {
            Intent batteryInfoIntent = context.registerReceiver(null, BatteryIntentFilterHolder.BATTERY_INTENT_FILTER);
            if (batteryInfoIntent == null) {
                return BatteryInfo.INVALID;
            }
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
            return batteryInfo;
        } catch (Throwable e) {
            return BatteryInfo.INVALID;
        }
    }
}

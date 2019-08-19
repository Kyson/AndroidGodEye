package cn.hikyson.godeye.monitor.modules.battery;

import android.os.BatteryManager;
import android.os.Build;
import android.support.v4.util.ArrayMap;

import java.util.Map;

import cn.hikyson.godeye.core.internal.modules.battery.BatteryInfo;
import io.reactivex.functions.Function;

public class BatteryInfoFactory {
    private static final Map<Integer, String> STATUS_MAPPING;
    private static final Map<Integer, String> HEALTH_MAPPING;
    private static final Map<Integer, String> PLUGGED_MAPPING;

    static {
        STATUS_MAPPING = new ArrayMap<>();
        STATUS_MAPPING.put(BatteryManager.BATTERY_STATUS_CHARGING, "Charging(充电中)");
        STATUS_MAPPING.put(BatteryManager.BATTERY_STATUS_DISCHARGING, "Discharging(放电中)");
        STATUS_MAPPING.put(BatteryManager.BATTERY_STATUS_FULL, "Full(已充满)");
        STATUS_MAPPING.put(BatteryManager.BATTERY_STATUS_NOT_CHARGING, "Not Charging(未充电)");
        STATUS_MAPPING.put(BatteryManager.BATTERY_STATUS_UNKNOWN, "Unknown(无电池)");

        HEALTH_MAPPING = new ArrayMap<>();
        HEALTH_MAPPING.put(BatteryManager.BATTERY_HEALTH_UNKNOWN, "UNKNOWN");
        HEALTH_MAPPING.put(BatteryManager.BATTERY_HEALTH_GOOD, "GOOD");
        HEALTH_MAPPING.put(BatteryManager.BATTERY_HEALTH_OVERHEAT, "OVERHEAT");
        HEALTH_MAPPING.put(BatteryManager.BATTERY_HEALTH_DEAD, "DEAD");
        HEALTH_MAPPING.put(BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE, "OVER_VOLTAGE");
        HEALTH_MAPPING.put(BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE, "UNSPECIFIED_FAILUR");
        HEALTH_MAPPING.put(BatteryManager.BATTERY_HEALTH_COLD, "COLD");

        PLUGGED_MAPPING = new ArrayMap<>();
        PLUGGED_MAPPING.put(BatteryManager.BATTERY_PLUGGED_AC, "AC");
        PLUGGED_MAPPING.put(BatteryManager.BATTERY_PLUGGED_USB, "USB");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            PLUGGED_MAPPING.put(BatteryManager.BATTERY_PLUGGED_WIRELESS, "WIRELESS");
        }
    }

    private static BatterySummaryInfo convert(BatteryInfo batteryInfo) {
        BatterySummaryInfo batterySummaryInfo = new BatterySummaryInfo();
        batterySummaryInfo.status = STATUS_MAPPING.get(batteryInfo.status);
        batterySummaryInfo.health = HEALTH_MAPPING.get(batteryInfo.health);
        batterySummaryInfo.level = batteryInfo.level;
        batterySummaryInfo.plugged = PLUGGED_MAPPING.get(batteryInfo.plugged);
        batterySummaryInfo.present = batteryInfo.present;
        batterySummaryInfo.scale = batteryInfo.scale;
        batterySummaryInfo.technology = batteryInfo.technology;
        batterySummaryInfo.temperature = batteryInfo.temperature;
        batterySummaryInfo.voltage = batteryInfo.voltage;
        return batterySummaryInfo;
    }

    public static Function<BatteryInfo, BatterySummaryInfo> converter() {
        return BatteryInfoFactory::convert;
    }
}

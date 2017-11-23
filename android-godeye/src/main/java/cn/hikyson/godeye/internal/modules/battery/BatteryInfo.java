package cn.hikyson.godeye.internal.modules.battery;

import android.os.BatteryManager;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class BatteryInfo {
    public static final BatteryInfo INVALID = new BatteryInfo();

    // int类型，状态，定义值是BatteryManager.BATTERY_STATUS_XXX / 正在充电、充满等等
    public int status;
    // int类型，健康，定义值是BatteryManager.BATTERY_HEALTH_XXX。
    public int health;
    // boolean类型
    public boolean present;
    // int类型，电池剩余容量
    public int level;
    //int类型，电池最大值。通常为100。
    public int scale;
    //int类型，连接的电源插座，定义值是BatteryManager.BATTERY_PLUGGED_XXX。
    public int plugged;
    //int类型，电压 mV
    public int voltage;
    //int类型，温度，0.1度单位。例如 表示197的时候，意思为19.7度。
    public int temperature;
    //String类型，电池类型，例如，Li-ion等等。
    public String technology;

    public static final String SPILT = "\r\n";

    @Override
    public String toString() {
        return new StringBuilder().append("statusSummary: ").append(getDisplayStatus()).append(SPILT)
                .append("health: ").append(getDisplayHealth()).append(SPILT)
                .append("present: ").append(present).append(SPILT)
                .append("level: ").append(level).append(SPILT)
                .append("scale: ").append(scale).append(SPILT)
                .append("plugged: ").append(getDisplayPlugged()).append(SPILT)
                .append("voltage: ").append(voltage / 1000.0).append(SPILT)
                .append("temperature: ").append(temperature / 10.0).append(SPILT)
                .append("technology: ").append(technology)
                .toString();
    }

    public String getDisplayStatus() {
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                // 充电状态
                return "charging";
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                // 放电中
                return "discharging";
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                // 未充电
                return "not charging";
            case BatteryManager.BATTERY_STATUS_FULL:
                // 电池满
                return "full";
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
            default:
                return "unknown";
        }
    }

    public String getDisplayHealth() {
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_GOOD:
                // 良好
                return "good";
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                //过热
                return "overheat";
            case BatteryManager.BATTERY_HEALTH_DEAD:
                //没电
                return "dead";
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                //过电压
                return "voltage";
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                return "unspecified failure";
            case BatteryManager.BATTERY_HEALTH_COLD:
                return "cold";
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
            default:
                return "unknown";
        }
    }

    public String getDisplayPlugged() {
        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                // 充电器
                return "ac";
            case BatteryManager.BATTERY_PLUGGED_USB:
                // USB
                return "usb";
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                // wireless
                return "wireless";
            default:
                return "unknown";
        }
    }
}

package cn.hikyson.godeye.core.internal.modules.battery;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class BatteryInfo {
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

    public BatteryInfo() {
    }

    public BatteryInfo(int status, int health, boolean present, int level, int scale, int plugged, int voltage, int temperature, String technology) {
        this.status = status;
        this.health = health;
        this.present = present;
        this.level = level;
        this.scale = scale;
        this.plugged = plugged;
        this.voltage = voltage;
        this.temperature = temperature;
        this.technology = technology;
    }

    @Override
    public String toString() {
        return "BatteryInfo{" +
                "status=" + status +
                ", health=" + health +
                ", present=" + present +
                ", level=" + level +
                ", scale=" + scale +
                ", plugged=" + plugged +
                ", voltage=" + voltage +
                ", temperature=" + temperature +
                ", technology='" + technology + '\'' +
                '}';
    }
}

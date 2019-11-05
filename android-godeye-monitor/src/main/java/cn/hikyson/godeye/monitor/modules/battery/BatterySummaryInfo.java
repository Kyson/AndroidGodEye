package cn.hikyson.godeye.monitor.modules.battery;

import android.support.annotation.Keep;

import java.io.Serializable;

@Keep
public class BatterySummaryInfo implements Serializable {
    public String status;
    public String health;
    // boolean类型
    public boolean present;
    // int类型，电池剩余容量
    public int level;
    //int类型，电池最大值。通常为100。
    public int scale;
    //int类型，连接的电源插座，定义值是BatteryManager.BATTERY_PLUGGED_XXX。
    public String plugged;
    //int类型，电压 mV
    public int voltage;
    //int类型，温度，0.1度单位。例如 表示197的时候，意思为19.7度。
    public int temperature;
    //String类型，电池类型，例如，Li-ion等等。
    public String technology;

    @Override
    public String toString() {
        return "BatterySummaryInfo{" +
                "status='" + status + '\'' +
                ", health='" + health + '\'' +
                ", present=" + present +
                ", level=" + level +
                ", scale=" + scale +
                ", plugged='" + plugged + '\'' +
                ", voltage=" + voltage +
                ", temperature=" + temperature +
                ", technology='" + technology + '\'' +
                '}';
    }
}

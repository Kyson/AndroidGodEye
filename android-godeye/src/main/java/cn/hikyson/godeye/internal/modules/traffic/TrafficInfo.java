package cn.hikyson.godeye.internal.modules.traffic;

import java.util.Locale;

/**
 * 流量消耗情况，单位kb/秒
 * Created by kysonchao on 2017/5/22.
 */
public class TrafficInfo {
    public float rxTotalRate;
    public float txTotalRate;
    //应用下载流量速度
    public float rxUidRate;
    //应用上传流量速度
    public float txUidRate;

    public TrafficInfo(float rxTotalRate, float txTotalRate, float rxUidRate, float txUidRate) {
        this.rxTotalRate = rxTotalRate;
        this.txTotalRate = txTotalRate;
        this.rxUidRate = rxUidRate;
        this.txUidRate = txUidRate;
    }

    public TrafficInfo() {
    }

    @Override
    public String toString() {
        return "rxUidRate=" + String.format(Locale.US, "%.3f kb/s", rxUidRate) +
                ", txUidRate=" + String.format(Locale.US, "%.3f kb/s", txUidRate) +
                ", rxTotalRate=" + String.format(Locale.US, "%.3f kb/s", rxTotalRate) +
                ", txTotalRate=" + String.format(Locale.US, "%.3f kb/s", txTotalRate);
    }
}

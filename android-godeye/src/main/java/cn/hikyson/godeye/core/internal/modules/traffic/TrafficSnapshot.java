package cn.hikyson.godeye.core.internal.modules.traffic;

import android.net.TrafficStats;
import android.support.annotation.WorkerThread;

import java.io.Serializable;

/**
 * 流量快照
 * 还有其他一些比如区分Wi-Fi和移动流量的数据没有包含进来
 * 单位kb
 * Created by kysonchao on 2017/5/22.
 */
public class TrafficSnapshot implements Serializable {
    //下行总字节数
    public float rxTotalKB;
    //上行总字节数
    public float txTotalKB;
    //下行应用总字节数
    public float rxUidKB;
    //上行应用总字节数
    public float txUidKB;

    @WorkerThread
    public static TrafficSnapshot snapshot() {
        TrafficSnapshot snapshot = new TrafficSnapshot();
        snapshot.rxTotalKB = TrafficStats.getTotalRxBytes() / 1024f;
        snapshot.txTotalKB = TrafficStats.getTotalTxBytes() / 1024f;
        snapshot.rxUidKB = TrafficStats.getUidRxBytes(android.os.Process.myUid()) / 1024f;
        snapshot.txUidKB = TrafficStats.getUidTxBytes(android.os.Process.myUid()) / 1024f;
        return snapshot;
    }


}

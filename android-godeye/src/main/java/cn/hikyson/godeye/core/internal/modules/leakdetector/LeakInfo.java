package cn.hikyson.godeye.core.internal.modules.leakdetector;

import androidx.annotation.Keep;

import java.io.Serializable;

import shark.Leak;

@Keep
public class LeakInfo implements Serializable {
    public long createdTimeMillis;
    public long durationTimeMillis;
    public Leak info;

    public LeakInfo(long createdTimeMillis, long durationTimeMillis, Leak info) {
        this.createdTimeMillis = createdTimeMillis;
        this.durationTimeMillis = durationTimeMillis;
        this.info = info;
    }

    @Override
    public String toString() {
        return "LeakInfo{" +
                "createdTimeMillis=" + createdTimeMillis +
                ", durationTimeMillis=" + durationTimeMillis +
                ", info=" + info +
                '}';
    }
}

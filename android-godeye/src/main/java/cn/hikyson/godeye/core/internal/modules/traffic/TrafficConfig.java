package cn.hikyson.godeye.core.internal.modules.traffic;


import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class TrafficConfig implements Serializable {
    public long intervalMillis;
    public long sampleMillis;

    public TrafficConfig(long intervalMillis, long sampleMillis) {
        this.intervalMillis = intervalMillis;
        this.sampleMillis = sampleMillis;
    }

    public TrafficConfig() {
        this.intervalMillis = 2000;
        this.sampleMillis = 1000;
    }

    public long intervalMillis() {
        return intervalMillis;
    }

    public long sampleMillis() {
        return sampleMillis;
    }

    @Override
    public String toString() {
        return "TrafficConfig{" +
                "intervalMillis=" + intervalMillis +
                ", sampleMillis=" + sampleMillis +
                '}';
    }
}
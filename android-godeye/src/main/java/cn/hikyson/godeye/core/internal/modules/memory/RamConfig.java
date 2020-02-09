package cn.hikyson.godeye.core.internal.modules.memory;


import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class RamConfig implements Serializable {
    public long intervalMillis;

    public RamConfig(long intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    public RamConfig() {
        this.intervalMillis = 3000;
    }

    public long intervalMillis() {
        return intervalMillis;
    }

    @Override
    public String toString() {
        return "RamConfig{" +
                "intervalMillis=" + intervalMillis +
                '}';
    }
}
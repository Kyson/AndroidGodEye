package cn.hikyson.godeye.core.internal.modules.cpu;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class CpuConfig implements Serializable {
    public long intervalMillis;

    public CpuConfig(long intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    public CpuConfig() {
        this.intervalMillis = 2000;
    }

    public long intervalMillis() {
        return intervalMillis;
    }

    @Override
    public String toString() {
        return "CpuConfig{" +
                "intervalMillis=" + intervalMillis +
                '}';
    }
}

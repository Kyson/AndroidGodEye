package cn.hikyson.godeye.core.internal.modules.memory;


import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class PssConfig implements Serializable {
    public long intervalMillis;

    public PssConfig(long intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    public PssConfig() {
        this.intervalMillis = 2000;
    }

    public long intervalMillis() {
        return intervalMillis;
    }

    @Override
    public String toString() {
        return "PssConfig{" +
                "intervalMillis=" + intervalMillis +
                '}';
    }
}
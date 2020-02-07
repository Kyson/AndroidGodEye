package cn.hikyson.godeye.core.internal.modules.memory;


import android.content.Context;

import androidx.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.core.GodEye;

@Keep
public class RamConfig implements Serializable {
    public long intervalMillis;

    public RamConfig(long intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    public RamConfig() {
        this.intervalMillis = 3000;
    }

    public Context context() {
        return GodEye.instance().getApplication();
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
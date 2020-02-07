package cn.hikyson.godeye.core.internal.modules.memory;


import android.content.Context;

import androidx.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.core.GodEye;

@Keep
public class PssConfig implements Serializable {
    public long intervalMillis;

    public PssConfig(long intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    public PssConfig() {
        this.intervalMillis = 2000;
    }

    public Context context() {
        return GodEye.instance().getApplication();
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
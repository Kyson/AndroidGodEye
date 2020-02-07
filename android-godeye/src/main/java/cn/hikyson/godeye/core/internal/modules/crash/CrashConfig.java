package cn.hikyson.godeye.core.internal.modules.crash;


import android.content.Context;

import androidx.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.core.GodEye;

@Keep
public class CrashConfig implements Serializable {
    public boolean immediate = false;

    public CrashConfig() {
    }

    public CrashConfig(boolean immediate) {
        this.immediate = immediate;
    }

    public Context context() {
        return GodEye.instance().getApplication();
    }

    public boolean immediate() {
        return immediate;
    }

    @Override
    public String toString() {
        return "CrashConfig{" +
                "immediate=" + immediate +
                '}';
    }
}

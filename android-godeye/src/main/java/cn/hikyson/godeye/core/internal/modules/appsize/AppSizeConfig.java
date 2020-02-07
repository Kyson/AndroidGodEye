package cn.hikyson.godeye.core.internal.modules.appsize;


import android.content.Context;

import androidx.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.core.GodEye;

@Keep
public class AppSizeConfig implements Serializable {
    public long delayMillis;

    public AppSizeConfig(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    public AppSizeConfig() {
        this.delayMillis = 0;
    }

    public Context context() {
        return GodEye.instance().getApplication();
    }

    public long delayMillis() {
        return delayMillis;
    }

    @Override
    public String toString() {
        return "AppSizeConfig{" +
                "delayMillis=" + delayMillis +
                '}';
    }
}

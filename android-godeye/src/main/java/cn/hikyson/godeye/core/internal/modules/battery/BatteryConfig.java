package cn.hikyson.godeye.core.internal.modules.battery;

import android.content.Context;

import androidx.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.core.GodEye;

@Keep
public class BatteryConfig implements Serializable {

    public Context context() {
        return GodEye.instance().getApplication();
    }

    @Override
    public String toString() {
        return "BatteryConfig{}";
    }
}

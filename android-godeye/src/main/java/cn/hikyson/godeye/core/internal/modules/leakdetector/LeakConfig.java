package cn.hikyson.godeye.core.internal.modules.leakdetector;


import android.app.Application;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.io.Serializable;

import cn.hikyson.godeye.core.GodEye;

@Keep
public class LeakConfig implements Serializable {
    // if you want leak module work in production,set debug false
    public boolean debug;
    public boolean debugNotification;
    //LeakRefInfoProvider
    public String leakRefInfoProvider;

    public LeakConfig(boolean debug, boolean debugNotification, String leakRefInfoProvider) {
        this.debug = debug;
        this.debugNotification = debugNotification;
        this.leakRefInfoProvider = leakRefInfoProvider;
    }

    public LeakConfig() {
        this.debug = true;
        this.debugNotification = true;
        this.leakRefInfoProvider = DefaultLeakRefInfoProvider.class.getName();
    }

    @NonNull
    public Application application() {
        return GodEye.instance().getApplication();
    }

    public boolean debug() {
        return debug;
    }

    public boolean debugNotification() {
        return debugNotification;
    }

    // LeakRefInfoProvider
    @NonNull
    public String leakRefInfoProvider() {
        return leakRefInfoProvider;
    }

    @Override
    public String toString() {
        return "LeakConfig{" +
                "debug=" + debug +
                ", debugNotification=" + debugNotification +
                ", leakRefInfoProvider=" + leakRefInfoProvider +
                '}';
    }
}
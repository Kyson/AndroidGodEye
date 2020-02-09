package cn.hikyson.godeye.core.internal.modules.leakdetector;


import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.io.Serializable;

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
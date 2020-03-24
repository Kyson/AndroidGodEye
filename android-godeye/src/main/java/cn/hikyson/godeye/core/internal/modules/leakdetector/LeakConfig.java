package cn.hikyson.godeye.core.internal.modules.leakdetector;


import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.io.Serializable;

@Keep
public class LeakConfig implements Serializable {
    // if you want leak module work in production,set debug false
    public boolean debug;
    /**
     * @deprecated
     */
    @Deprecated
    public boolean debugNotification;
    //LeakRefInfoProvider
    /**
     * @deprecated
     */
    @Deprecated
    public String leakRefInfoProvider;

    /**
     * @deprecated
     */
    @Deprecated
    public LeakConfig(boolean debug, boolean debugNotification, String leakRefInfoProvider) {
        this.debug = debug;
    }

    public LeakConfig() {
        this.debug = true;
    }

    public LeakConfig(boolean debug) {
        this.debug = debug;
    }

    public boolean debug() {
        return debug;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public boolean debugNotification() {
        return debugNotification;
    }

    // LeakRefInfoProvider

    /**
     * @deprecated
     */
    @Deprecated
    @NonNull
    public String leakRefInfoProvider() {
        return leakRefInfoProvider;
    }

    @Override
    public String toString() {
        return "LeakConfig{" +
                "debug=" + debug +
                '}';
    }
}
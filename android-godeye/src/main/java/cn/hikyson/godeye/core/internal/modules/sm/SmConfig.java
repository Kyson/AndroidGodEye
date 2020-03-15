package cn.hikyson.godeye.core.internal.modules.sm;


import androidx.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.core.internal.notification.NotificationConfig;

@Keep
public class SmConfig implements Serializable {
    /**
     * @deprecated use {@link cn.hikyson.godeye.core.GodEye#installNotification(NotificationConfig)}
     */
    @Deprecated
    public boolean debugNotification;
    public long longBlockThresholdMillis;
    public long shortBlockThresholdMillis;
    public long dumpIntervalMillis;

    public SmConfig(boolean debugNotification, long longBlockThresholdMillis, long shortBlockThresholdMillis, long dumpIntervalMillis) {
        this.debugNotification = debugNotification;
        this.longBlockThresholdMillis = longBlockThresholdMillis;
        this.shortBlockThresholdMillis = shortBlockThresholdMillis;
        this.dumpIntervalMillis = dumpIntervalMillis;
    }

    public SmConfig() {
        this.debugNotification = true;
        this.longBlockThresholdMillis = 500;
        this.shortBlockThresholdMillis = 500;
        this.dumpIntervalMillis = 1000;
    }

    public SmConfig(SmConfig smConfig) {
        this.debugNotification = smConfig.debugNotification;
        this.longBlockThresholdMillis = smConfig.longBlockThresholdMillis;
        this.shortBlockThresholdMillis = smConfig.shortBlockThresholdMillis;
        this.dumpIntervalMillis = smConfig.dumpIntervalMillis;
    }

    /**
     * @deprecated use {@link cn.hikyson.godeye.core.GodEye#installNotification(NotificationConfig)}
     */
    @Deprecated
    public boolean debugNotification() {
        return debugNotification;
    }

    public long longBlockThreshold() {
        return longBlockThresholdMillis;
    }

    public long shortBlockThreshold() {
        return shortBlockThresholdMillis;
    }

    public long dumpInterval() {
        return dumpIntervalMillis;
    }

    public boolean isValid() {
        return longBlockThresholdMillis > 0 && shortBlockThresholdMillis > 0 && dumpIntervalMillis > 0;
    }

    @Override
    public String toString() {
        return "SmConfig{" +
                "debugNotification=" + debugNotification +
                ", longBlockThresholdMillis=" + longBlockThresholdMillis +
                ", shortBlockThresholdMillis=" + shortBlockThresholdMillis +
                ", dumpIntervalMillis=" + dumpIntervalMillis +
                '}';
    }


}
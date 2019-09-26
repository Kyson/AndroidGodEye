package cn.hikyson.godeye.core.internal.modules.startup;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class StartupInfo {
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({StartUpType.COLD, StartUpType.HOT})
    public @interface StartUpType {
        public static final String COLD = "cold";
        public static final String HOT = "hot";
    }

    public @StartUpType
    String startupType;
    public long startupTime;

    public StartupInfo(@StartUpType String startupType, long startupTime) {
        this.startupType = startupType;
        this.startupTime = startupTime;
    }

    @Override
    public String toString() {
        return "StartupInfo{" +
                "startupType='" + startupType + '\'' +
                ", startupTime=" + startupTime +
                '}';
    }
}

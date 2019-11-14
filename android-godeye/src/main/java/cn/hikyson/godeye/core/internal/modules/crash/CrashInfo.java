package cn.hikyson.godeye.core.internal.modules.crash;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Map;

@Keep
public class CrashInfo implements Serializable {
    public String startTime;
    public String crashTime;
    public String crashMessage;
    public String processId;
    public String processName;
    public String threadId;
    public String threadName;
    public String nativeCrashCode;
    public String nativeCrashSignal;
    public String nativeCrashBacktrace;
    public String nativeCrashStack;
    public String javaCrashStacktrace;
    public Map<String, String> extras;

    public CrashInfo() {
    }

    @Override
    public String toString() {
        return "CrashInfo{" +
                "startTime='" + startTime + '\'' +
                ", crashTime='" + crashTime + '\'' +
                ", crashMessage='" + crashMessage + '\'' +
                ", processId='" + processId + '\'' +
                ", processName='" + processName + '\'' +
                ", threadId='" + threadId + '\'' +
                ", threadName='" + threadName + '\'' +
                ", nativeCrashCode='" + nativeCrashCode + '\'' +
                ", nativeCrashSignal='" + nativeCrashSignal + '\'' +
                ", nativeCrashBacktrace='" + nativeCrashBacktrace + '\'' +
                ", nativeCrashStack='" + nativeCrashStack + '\'' +
                ", javaCrashStacktrace='" + javaCrashStacktrace + '\'' +
                ", extras=" + extras +
                '}';
    }
}

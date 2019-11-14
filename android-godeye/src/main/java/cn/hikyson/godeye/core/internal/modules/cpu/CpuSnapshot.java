package cn.hikyson.godeye.core.internal.modules.cpu;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * 1. adb shell dumpsys cpuinfo |grep packagename
 * 2. adb shell top -m 10 -s cpu
 * <p>
 * Created by kysonchao on 2017/5/22.
 */
@Keep
public class CpuSnapshot implements Serializable {
    public long user = 0;
    public long system = 0;
    public long idle = 0;
    public long ioWait = 0;
    public long total = 0;
    public long app = 0;

    public CpuSnapshot(long user, long system, long idle, long ioWait, long total, long app) {
        this.user = user;
        this.system = system;
        this.idle = idle;
        this.ioWait = ioWait;
        this.total = total;
        this.app = app;
    }

    public CpuSnapshot() {
    }
}

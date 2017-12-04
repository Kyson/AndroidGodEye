package cn.hikyson.godeye.core.internal.modules.memory;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class RamInfo {
    //可用RAM
    public long availMemKb;
    //手机总RAM
    public long totalMemKb;
    //内存占用满的阀值，超过即认为低内存运行状态，可能会Kill process
    public long lowMemThresholdKb;
    //是否低内存状态运行
    public boolean isLowMemory;

    @Override
    public String toString() {
        return "RamMemoryInfo{" +
                "availMem=" + availMemKb +
                ", totalMem=" + totalMemKb +
                ", lowMemThreshold=" + lowMemThresholdKb +
                ", isLowMemory=" + isLowMemory +
                '}';
    }
}

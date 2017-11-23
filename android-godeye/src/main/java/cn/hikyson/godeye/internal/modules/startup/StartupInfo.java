package cn.hikyson.godeye.internal.modules.startup;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class StartupInfo {
    //冷启动时间开始点
    public long coldLaunchTimeMillis;
    //热启动时间开始点
    public long hotLaunchTimeMillis;
    //启动完成时间点
    public long endLaunchTimeMillis;

    public StartupInfo(long coldLaunchTimeMillis, long hotLaunchTimeMillis, long endLaunchTimeMillis) {
        this.coldLaunchTimeMillis = coldLaunchTimeMillis;
        this.hotLaunchTimeMillis = hotLaunchTimeMillis;
        this.endLaunchTimeMillis = endLaunchTimeMillis;
    }

    @Override
    public String toString() {
        return "StartupInfo{" +
                "coldLaunchTimeMillis=" + coldLaunchTimeMillis +
                ", hotLaunchTimeMillis=" + hotLaunchTimeMillis +
                ", endLaunchTimeMillis=" + endLaunchTimeMillis +
                '}';
    }
}

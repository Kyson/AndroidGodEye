package cn.hikyson.godeye.core.internal.modules.pageload;

/**
 * Created by kysonchao on 2018/1/25.
 */
public class LoadTimeInfo {
    public long didDrawTime;

    public LoadTimeInfo(long didDrawTime) {
        this.didDrawTime = didDrawTime;
    }

    @Override
    public String toString() {
        return "LoadTimeInfo{" +
                "didDrawTime=" + didDrawTime +
                '}';
    }
}

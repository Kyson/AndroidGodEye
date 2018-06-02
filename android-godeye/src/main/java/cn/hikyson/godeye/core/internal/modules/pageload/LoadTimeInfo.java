package cn.hikyson.godeye.core.internal.modules.pageload;

/**
 * Created by kysonchao on 2018/1/25.
 */
public class LoadTimeInfo {
    public long createTime;
    public long didDrawTime;
    public long loadTime;

    public LoadTimeInfo(long createTime, long didDrawTime, long loadTime) {
        this.createTime = createTime;
        this.didDrawTime = didDrawTime;
        this.loadTime = loadTime;
    }

    public LoadTimeInfo() {
    }

    @Override
    public String toString() {
        return "LoadTimeInfo{" +
                "createTime=" + createTime +
                ", didDrawTime=" + didDrawTime +
                ", loadTime=" + loadTime +
                '}';
    }
}

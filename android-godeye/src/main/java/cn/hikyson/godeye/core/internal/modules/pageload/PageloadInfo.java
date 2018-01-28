package cn.hikyson.godeye.core.internal.modules.pageload;

/**
 * Created by kysonchao on 2018/1/25.
 */
public class PageloadInfo {
    public String pageName;
    public LoadTimeInfo loadTimeInfo;

    public PageloadInfo(String pageName, LoadTimeInfo loadTimeInfo) {
        this.pageName = pageName;
        this.loadTimeInfo = loadTimeInfo;
    }

    @Override
    public String toString() {
        return "PageloadInfo{" +
                "pageName='" + pageName + '\'' +
                ", loadTimeInfo=" + loadTimeInfo +
                '}';
    }
}

package cn.hikyson.godeye.core.internal.modules.pageload;

/**
 * Created by kysonchao on 2018/1/25.
 */
public class PageloadInfo {
    public String pageId;
    public String pageName;
    public String pageStatus;
    public long pageStatusTime;
    public LoadTimeInfo loadTimeInfo;

    public PageloadInfo(String pageId, String pageName, String pageStatus, long pageStatusTime) {
        this.pageId = pageId;
        this.pageName = pageName;
        this.pageStatus = pageStatus;
        this.pageStatusTime = pageStatusTime;
    }

    @Override
    public String toString() {
        return "PageloadInfo{" +
                "pageId='" + pageId + '\'' +
                ", pageName='" + pageName + '\'' +
                ", pageStatus='" + pageStatus + '\'' +
                ", pageStatusTime='" + pageStatusTime + '\'' +
                ", loadTimeInfo=" + loadTimeInfo +
                '}';
    }
}

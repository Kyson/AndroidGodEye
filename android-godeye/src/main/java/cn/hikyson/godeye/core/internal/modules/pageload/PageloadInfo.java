package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by kysonchao on 2018/1/25.
 */
public class PageloadInfo {
    public String pageId;
    public String pageName;
    public String pageStatus;
    public long pageStatusTime;
    public LoadTimeInfo loadTimeInfo;
    public transient WeakReference<Activity> activityReference;

    public PageloadInfo(Activity activity, String pageId, String pageName, String pageStatus, long pageStatusTime) {
        this.activityReference = new WeakReference<>(activity);
        this.pageId = pageId;
        this.pageName = pageName;
        this.pageStatus = pageStatus;
        this.pageStatusTime = pageStatusTime;
    }

    @Override
    public String toString() {
        return "PageloadInfo{" +
                "activity='" + (activityReference.get() != null ? activityReference.get().toString() : "recycled") + '\'' +
                ", pageId='" + pageId + '\'' +
                ", pageName='" + pageName + '\'' +
                ", pageStatus='" + pageStatus + '\'' +
                ", pageStatusTime='" + pageStatusTime + '\'' +
                ", loadTimeInfo=" + loadTimeInfo +
                '}';
    }
}

package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;

import java.util.LinkedHashMap;

/**
 * Created by kysonchao on 2018/1/25.
 */
public class PageloadActivityStack {
    private LinkedHashMap<Activity, LoadTimeInfo> mActivityPageloadInfoLinkedHashMap;

    public synchronized LoadTimeInfo onCreate(Activity activity, long time) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            mActivityPageloadInfoLinkedHashMap = new LinkedHashMap<>();
        }
        LoadTimeInfo loadTimeInfo = new LoadTimeInfo();
        loadTimeInfo.createTime = time;
        mActivityPageloadInfoLinkedHashMap.put(activity, loadTimeInfo);
        return loadTimeInfo;
    }

    public synchronized LoadTimeInfo onDidDraw(Activity activity, long time) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            mActivityPageloadInfoLinkedHashMap = new LinkedHashMap<>();
        }
        if (mActivityPageloadInfoLinkedHashMap.containsKey(activity)) {
            LoadTimeInfo loadTimeInfo = mActivityPageloadInfoLinkedHashMap.get(activity);
            if (loadTimeInfo != null) {
                loadTimeInfo.didDrawTime = time;
                return loadTimeInfo;
            }
        }
        return null;
    }

    public synchronized LoadTimeInfo onLoaded(Activity activity, long time) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            mActivityPageloadInfoLinkedHashMap = new LinkedHashMap<>();
        }
        if (mActivityPageloadInfoLinkedHashMap.containsKey(activity)) {
            LoadTimeInfo loadTimeInfo = mActivityPageloadInfoLinkedHashMap.get(activity);
            if (loadTimeInfo != null) {
                loadTimeInfo.loadTime = time;
                return loadTimeInfo;
            }
        }
        return null;
    }

    public synchronized LoadTimeInfo onDestory(Activity activity) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            return null;
        }
        return mActivityPageloadInfoLinkedHashMap.remove(activity);
    }
}

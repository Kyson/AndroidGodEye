package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.support.annotation.Nullable;

import java.util.LinkedHashMap;

/**
 * Created by kysonchao on 2018/1/25.
 */
public class PageloadActivityStack {
    private LinkedHashMap<Activity, LoadTimeInfo> mActivityPageloadInfoLinkedHashMap;

    public synchronized @Nullable
    LoadTimeInfo onCreate(Activity activity, long time) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            mActivityPageloadInfoLinkedHashMap = new LinkedHashMap<>();
        }
        if (mActivityPageloadInfoLinkedHashMap.containsKey(activity)) {//if exist,ignore
            return null;
        }
        LoadTimeInfo loadTimeInfo = new LoadTimeInfo();
        if (loadTimeInfo.createTime == 0) {//if not created
            loadTimeInfo.createTime = time;
        }
        mActivityPageloadInfoLinkedHashMap.put(activity, loadTimeInfo);
        return loadTimeInfo;
    }

    public synchronized @Nullable
    LoadTimeInfo onDidDraw(Activity activity, long time) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            mActivityPageloadInfoLinkedHashMap = new LinkedHashMap<>();
        }
        if (mActivityPageloadInfoLinkedHashMap.containsKey(activity)) {
            LoadTimeInfo loadTimeInfo = mActivityPageloadInfoLinkedHashMap.get(activity);
            if (loadTimeInfo != null) {
                if (loadTimeInfo.didDrawTime == 0) {//if not DidDraw
                    loadTimeInfo.didDrawTime = time;
                }
                return loadTimeInfo;
            }
        }
        return null;
    }

    public synchronized @Nullable
    LoadTimeInfo onLoaded(Activity activity, long time) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            mActivityPageloadInfoLinkedHashMap = new LinkedHashMap<>();
        }
        if (mActivityPageloadInfoLinkedHashMap.containsKey(activity)) {
            LoadTimeInfo loadTimeInfo = mActivityPageloadInfoLinkedHashMap.get(activity);
            if (loadTimeInfo != null) {
                if (loadTimeInfo.loadTime == 0) {//if not loaded
                    loadTimeInfo.loadTime = time;
                }
                return loadTimeInfo;
            }
        }
        return null;
    }

    public synchronized @Nullable
    LoadTimeInfo onDestory(Activity activity) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            return null;
        }
        return mActivityPageloadInfoLinkedHashMap.remove(activity);
    }
}

package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kysonchao on 2018/1/25.
 */
public class ActivityStack {
    private LinkedHashMap<Activity, LoadTimeInfo> mActivityPageloadInfoLinkedHashMap;

    public synchronized void onCreate(Activity activity, long time) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            mActivityPageloadInfoLinkedHashMap = new LinkedHashMap<>();
        }
        LoadTimeInfo loadTimeInfo = new LoadTimeInfo();
        loadTimeInfo.createTime = time;
        mActivityPageloadInfoLinkedHashMap.put(activity, loadTimeInfo);
    }

    public synchronized void onDidDraw(Activity activity, long time) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            mActivityPageloadInfoLinkedHashMap = new LinkedHashMap<>();
        }
        if (mActivityPageloadInfoLinkedHashMap.containsKey(activity)) {
            LoadTimeInfo loadTimeInfo = mActivityPageloadInfoLinkedHashMap.get(activity);
            if (loadTimeInfo != null) {
                loadTimeInfo.didDrawTime = time;
            }
        }
    }

    public synchronized void onLoaded(Activity activity, long time) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            mActivityPageloadInfoLinkedHashMap = new LinkedHashMap<>();
        }
        if (mActivityPageloadInfoLinkedHashMap.containsKey(activity)) {
            LoadTimeInfo loadTimeInfo = mActivityPageloadInfoLinkedHashMap.get(activity);
            if (loadTimeInfo != null) {
                loadTimeInfo.loadTime = time;
            }
        }
    }

    public synchronized LoadTimeInfo onDestory(Activity activity) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            return null;
        }
        return mActivityPageloadInfoLinkedHashMap.remove(activity);
    }
}

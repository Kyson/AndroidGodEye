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

    public synchronized void push(Activity activity) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            mActivityPageloadInfoLinkedHashMap = new LinkedHashMap<>();
        }
        mActivityPageloadInfoLinkedHashMap.put(activity, new LoadTimeInfo(0));
    }

    public synchronized void push(Activity activity, LoadTimeInfo loadTimeInfo) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            mActivityPageloadInfoLinkedHashMap = new LinkedHashMap<>();
        }
        if (mActivityPageloadInfoLinkedHashMap.containsKey(activity)) {
            mActivityPageloadInfoLinkedHashMap.put(activity, loadTimeInfo);
        }
    }

    @Nullable
    public synchronized LoadTimeInfo pop(Activity activity) {
        if (mActivityPageloadInfoLinkedHashMap == null) {
            return null;
        }
        return mActivityPageloadInfoLinkedHashMap.remove(activity);
    }

    public synchronized List<PageloadInfo> getActivityPageloads() {
        List<PageloadInfo> pageloadInfos = new ArrayList<>();
        if (mActivityPageloadInfoLinkedHashMap == null || mActivityPageloadInfoLinkedHashMap.isEmpty()) {
            return pageloadInfos;
        }
        for (Map.Entry<Activity, LoadTimeInfo> entry : mActivityPageloadInfoLinkedHashMap.entrySet()) {
            pageloadInfos.add(new PageloadInfo(entry.getKey().getClass().getSimpleName() + entry.getKey().hashCode(), entry.getValue()));
        }
        return pageloadInfos;
    }
}

package cn.hikyson.android.godeye.sample;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import java.util.Map;

import cn.hikyson.godeye.monitor.GodEyeMonitor;

/**
 * Created by kysonchao on 2017/12/9.
 */

public class AppInfoProxyImpl implements GodEyeMonitor.AppInfoConext {
    private Context mContext;

    public AppInfoProxyImpl(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public Map<String, Object> getAppInfo() {
        Map<String, Object> map = new ArrayMap<>();
        map.put("VersionName", BuildConfig.VERSION_NAME);
        map.put("VersionCode", BuildConfig.VERSION_CODE);
        map.put("BuildType", BuildConfig.BUILD_TYPE);
        map.put("Debuggable", BuildConfig.DEBUG);
        map.put("Email", "kysonchao@gmail.com");
        map.put("ProjectUrl", "https://github.com/Kyson/AndroidGodEye");
        map.put("Blog", "tech.hikyson.cn");
        return map;
    }
}

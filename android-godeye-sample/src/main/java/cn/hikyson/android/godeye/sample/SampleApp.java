package cn.hikyson.android.godeye.sample;

import android.app.Application;
import android.content.Context;
import android.support.v4.util.ArrayMap;

import java.util.Map;

import cn.hikyson.android.godeye.BuildConfig;
import cn.hikyson.godeye.monitor.GodEyeMonitor;
import cn.hikyson.godeye.monitor.modules.AppInfoModule;

/**
 * Created by kysonchao on 2017/11/30.
 */
public class SampleApp extends Application {

    private static class AppInfoProxyImpl implements AppInfoModule.AppInfo.AppInfoProxy {
        private Context mContext;


        public AppInfoProxyImpl(Context context) {
            mContext = context;
        }

        @Override
        public AppInfoModule.AppInfo getAppInfo() {
            Map<String, Object> map = new ArrayMap<>();
            map.put("versionName", BuildConfig.VERSION_NAME);
            map.put("versionCode", BuildConfig.VERSION_CODE);
            map.put("buildType", BuildConfig.BUILD_TYPE);
            map.put("debuggable", BuildConfig.DEBUG);
            map.put("channel", "channel");
            map.put("clientid", "clientid");
            map.put("deviceid", "deviceid");
            map.put("uid", "uid");
            map.put("email", "email");
            return new AppInfoModule.AppInfo(mContext, map);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppInfoModule.AppInfo.AppInfoProxy sAppInfoProxy = new AppInfoProxyImpl(getApplicationContext());
        GodEyeMonitor.injectAppInfoProxy(sAppInfoProxy);
    }
}

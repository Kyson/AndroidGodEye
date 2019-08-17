package cn.hikyson.android.godeye.sample;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.hikyson.godeye.monitor.GodEyeMonitor;
import cn.hikyson.godeye.monitor.modules.AppInfoLabel;

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
    public List<AppInfoLabel> getAppInfo() {
        List<AppInfoLabel> appInfoLabels = new ArrayList<>();
        appInfoLabels.add(new AppInfoLabel("VersionName:" + BuildConfig.VERSION_NAME, ""));
        appInfoLabels.add(new AppInfoLabel("VersionCode:" + BuildConfig.VERSION_CODE, ""));
        appInfoLabels.add(new AppInfoLabel("BuildType:" + BuildConfig.BUILD_TYPE, ""));
        appInfoLabels.add(new AppInfoLabel("Debuggable:" + BuildConfig.DEBUG, ""));
        appInfoLabels.add(new AppInfoLabel("Email:kysonchao@gmail.com", "mailto:kysonchao@gmail.com"));
        appInfoLabels.add(new AppInfoLabel("ProjectUrl:https://github.com/Kyson/AndroidGodEye", "https://github.com/Kyson/AndroidGodEye"));
        appInfoLabels.add(new AppInfoLabel("Blog:tech.hikyson.cn", "https://tech.hikyson.cn"));
        return appInfoLabels;
    }
}

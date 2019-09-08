package cn.hikyson.android.godeye.sample;

import java.util.ArrayList;
import java.util.List;

import cn.hikyson.godeye.monitor.GodEyeMonitor;
import cn.hikyson.godeye.monitor.modules.appinfo.AppInfoLabel;

/**
 * Created by kysonchao on 2017/12/9.
 */
public class AppInfoProxyImpl implements GodEyeMonitor.AppInfoConext {

    @Override
    public List<AppInfoLabel> getAppInfo() {
        List<AppInfoLabel> appInfoLabels = new ArrayList<>();
        appInfoLabels.add(new AppInfoLabel("Email", "kysonchao@gmail.com", "mailto:kysonchao@gmail.com"));
        appInfoLabels.add(new AppInfoLabel("ProjectUrl", "https://github.com/Kyson/AndroidGodEye", "https://github.com/Kyson/AndroidGodEye"));
        appInfoLabels.add(new AppInfoLabel("Blog", "tech.hikyson.cn", "https://tech.hikyson.cn"));
        return appInfoLabels;
    }
}

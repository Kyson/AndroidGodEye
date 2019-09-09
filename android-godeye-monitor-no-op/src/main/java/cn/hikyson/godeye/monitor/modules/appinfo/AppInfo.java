package cn.hikyson.godeye.monitor.modules.appinfo;

import java.util.List;

import cn.hikyson.godeye.monitor.GodEyeMonitor;

public class AppInfo {

    public static void injectAppInfoConext(GodEyeMonitor.AppInfoConext appInfoConext) {
    }

    public List<AppInfoLabel> labels;
    public String appName;

    public static class Factory {

        public static AppInfo create() {
            return new AppInfo();
        }
    }
}

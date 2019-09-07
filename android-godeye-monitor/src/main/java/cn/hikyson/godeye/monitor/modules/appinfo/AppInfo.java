package cn.hikyson.godeye.monitor.modules.appinfo;

import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import cn.hikyson.godeye.monitor.GodEyeMonitor;

public class AppInfo {
    private static GodEyeMonitor.AppInfoConext sAppInfoConext;

    public static void injectAppInfoConext(GodEyeMonitor.AppInfoConext appInfoConext) {
        sAppInfoConext = appInfoConext;
    }

    public String appName;
    public List<AppInfoLabel> labels;

    public AppInfo() {
        if (sAppInfoConext == null) {
            return;
        }
        Context context = GodEyeMonitor.getContext();
        if (context == null) {
            return;
        }
        PackageManager pm = context.getPackageManager();
        this.appName = context.getApplicationInfo().loadLabel(pm).toString();
        this.labels = new ArrayList<>();
        if (sAppInfoConext.getAppInfo() != null) {
            this.labels = sAppInfoConext.getAppInfo();
        }
    }
}

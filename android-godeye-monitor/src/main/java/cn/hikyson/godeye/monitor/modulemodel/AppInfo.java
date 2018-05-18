package cn.hikyson.godeye.monitor.modulemodel;

import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.monitor.GodEyeMonitor;

public class AppInfo {
    private static GodEyeMonitor.AppInfoConext sAppInfoConext;

    public static void injectAppInfoConext(GodEyeMonitor.AppInfoConext appInfoConext) {
        sAppInfoConext = appInfoConext;
    }

    public String appName;
    public List<String> labels;

    public AppInfo() {
        if (sAppInfoConext == null) {
            return;
        }
        Context context = sAppInfoConext.getContext();
        PackageManager pm = context.getPackageManager();
        this.appName = context.getApplicationInfo().loadLabel(pm).toString();
        this.labels = new ArrayList<>();
        if (sAppInfoConext.getAppInfo() != null && !sAppInfoConext.getAppInfo().isEmpty()) {
            for (Map.Entry<String, Object> entry : sAppInfoConext.getAppInfo().entrySet()) {
                this.labels.add(entry.getKey() + " : " + entry.getValue());
            }
        }
    }
}

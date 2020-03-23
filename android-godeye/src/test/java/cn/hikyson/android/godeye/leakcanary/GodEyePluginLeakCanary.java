package cn.hikyson.android.godeye.leakcanary;

import android.app.Application;

import androidx.annotation.Keep;

import cn.hikyson.godeye.core.internal.modules.leakdetector.Leak;

@Keep
public class GodEyePluginLeakCanary {
    @Keep
    public static void install(Application application, final Leak leak) {
    }

    @Keep
    public static void uninstall() {
    }
}

package cn.hikyson.godeye.monitor;

import android.app.Application;
import android.content.Context;

import java.util.Map;

import cn.hikyson.godeye.monitor.modules.AppInfoModule;

/**
 * Created by kysonchao on 2017/11/27.
 */
public class GodEyeMonitor {

    public interface AppInfoConext {
        Context getContext();

        Map<String, Object> getAppInfo();
    }

    public static void work(Context context) {
        //no op
    }

    public static void work(Context context, int port) {
        //no op
    }

    public static void shutDown() {
        //no op
    }

    public static void injectAppInfoConext(AppInfoConext appInfoConext) {
        //no op
    }
}

package cn.hikyson.godeye.monitor;

import android.content.Context;

import java.util.List;

import cn.hikyson.godeye.monitor.modules.appinfo.AppInfoLabel;
import cn.hikyson.godeye.monitor.modules.thread.ThreadRunningProcessClassifier;

/**
 * Created by kysonchao on 2017/11/27.
 */
public class GodEyeMonitor {

    public interface AppInfoConext {
        List<AppInfoLabel> getAppInfo();
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

    @Deprecated
    public static void setClassPrefixOfAppProcess(List<String> classPathPrefixes) {
        // no op
    }

    @Deprecated
    public static void setThreadRunningProcessClassifier(ThreadRunningProcessClassifier threadRunningProcessClassifier) {
        // no op
    }

    public static Context getContext() {
        // no op
        return null;
    }
}

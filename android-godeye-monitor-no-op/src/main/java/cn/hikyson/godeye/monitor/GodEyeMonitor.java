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

    /**
     * set app informations,it will show on the top of dashboard
     *
     * @param appInfoConext
     */
    public static void injectAppInfoConext(AppInfoConext appInfoConext) {
    }

    public static synchronized void work(Context context) {
    }

    /**
     * monitor start work
     */
    public static synchronized void work(Context context, int port) {
    }

    /**
     * monitor stop work
     */
    public static synchronized void shutDown() {
    }

    public static Context getContext() {
        return null;
    }

    @Deprecated
    public static void setClassPrefixOfAppProcess(List<String> classPathPrefixes) {
    }

    @Deprecated
    public static void setThreadRunningProcessClassifier(ThreadRunningProcessClassifier threadRunningProcessClassifier) {
    }
}

package cn.hikyson.godeye.monitor;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;

import java.util.Map;

import cn.hikyson.godeye.monitor.driver.Watcher;
import cn.hikyson.godeye.monitor.modules.AppInfoModule;
import cn.hikyson.godeye.monitor.server.ClientServer;
import cn.hikyson.godeye.monitor.server.Router;
import cn.hikyson.godeye.core.utils.L;

/**
 * Created by kysonchao on 2017/11/27.
 */
public class GodEyeMonitor {
    private static boolean sIsWorking = false;
    private static final int DEFAULT_PORT = 5390;
    private static ClientServer sClientServer;
    private static Watcher sWatcher;

    public interface AppInfoConext {
        Context getContext();

        Map<String, Object> getAppInfo();
    }

    /**
     * @param appInfoConext
     */
    public static synchronized void injectAppInfoConext(AppInfoConext appInfoConext) {
        AppInfoModule.injectAppInfoConext(appInfoConext);
    }

    public static synchronized void work(Context context) {
        work(context, DEFAULT_PORT);
    }

    /**
     * monitor开始工作
     */
    public static synchronized void work(Context context, int port) {
        if (sIsWorking) {
            return;
        }
        sIsWorking = true;
        if (context == null) {
            throw new IllegalStateException("context can not be null.");
        }
        Context applicationContext = context.getApplicationContext();
        sWatcher = new Watcher();
        sWatcher.observeAll();
        Router.get().init(applicationContext);
        initServer(applicationContext, port);
    }

    /**
     * monitor停止工作
     */
    public static synchronized void shutDown() {
        if (sClientServer != null) {
            sClientServer.stop();
            sClientServer = null;
        }
        if (sWatcher != null) {
            sWatcher.cancelAllObserve();
            sWatcher = null;
        }
        sIsWorking = false;
    }

    private static void initServer(Context context, int port) {
        sClientServer = new ClientServer(port);
        sClientServer.start();
        L.d(getAddressLog(context, port));
        L.d("Leak dump files are in /storage/download/leakcanary-" + context.getPackageName());
    }

    private static String getAddressLog(Context context, int port) {
        @SuppressLint("WifiManagerPotentialLeak")
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager != null ? wifiManager.getConnectionInfo().getIpAddress() : 0;
        @SuppressLint("DefaultLocale") final String formattedIpAddress = String.format("%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
        return "Open AndroidGodEye dashboard [ http://" + formattedIpAddress + ":" + port + " ] in your browser , if can not open it , make sure device and pc are on the same network segment";
    }
}

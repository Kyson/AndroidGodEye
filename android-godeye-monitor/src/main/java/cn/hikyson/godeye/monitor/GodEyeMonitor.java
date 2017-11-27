package cn.hikyson.godeye.monitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;

import cn.hikyson.godeye.monitor.driver.Watcher;
import cn.hikyson.godeye.monitor.server.ClientServer;
import cn.hikyson.godeye.monitor.server.Router;
import cn.hikyson.godeye.utils.L;

/**
 * Created by kysonchao on 2017/11/27.
 */
public class GodEyeMonitor {
    private static boolean hasInit = false;
    private static final int PORT = 5389;
    private static ClientServer sClientServer;

    public static synchronized void work(Context context) {
        if (hasInit) {
            return;
        }
        hasInit = true;
        Context applicationContext = context.getApplicationContext();
        new Watcher().watchAll();
        Router.get().init(applicationContext);
        initServer(applicationContext);
    }

    public static synchronized void shutDown() {
        if (sClientServer != null) {
            sClientServer.stop();
            sClientServer = null;
        }
        //TODO KYSON watch cancel
        // Watcher
    }

    private static void initServer(Context context) {
        sClientServer = new ClientServer(PORT);
        sClientServer.start();
        L.d(getAddressLog(context, PORT));
        L.d("Leak dump files are in /storage/download/leakcanary-" + context.getPackageName());
    }

    private static String getAddressLog(Context context, int port) {
        @SuppressLint("WifiManagerPotentialLeak")
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        @SuppressLint("DefaultLocale") final String formattedIpAddress = String.format("%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
        return "Open performance dashboard [ http://" + formattedIpAddress + ":" + port + " ] in your browser , if can not open it , make sure device and pc are on the same network segment";
    }
}

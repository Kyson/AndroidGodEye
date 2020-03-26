package cn.hikyson.godeye.monitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.Keep;

import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import java.util.List;
import java.util.Locale;

import cn.hikyson.godeye.core.internal.notification.NotificationObserverManager;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import cn.hikyson.godeye.monitor.notification.MonitorNotificationListener;
import cn.hikyson.godeye.monitor.server.GodEyeMonitorServer;
import cn.hikyson.godeye.monitor.server.HttpStaticProcessor;
import cn.hikyson.godeye.monitor.server.ModuleDriver;
import cn.hikyson.godeye.monitor.server.WebSocketBizProcessor;

/**
 * Created by kysonchao on 2017/11/27.
 */
@Keep
public class GodEyeMonitor {
    private static boolean sIsWorking = false;
    private static final String MONITOR_LOGCAT = "AndroidGodEye monitor is running at port [%s]";
    private static GodEyeMonitorServer sGodEyeMonitorServer;

    /**
     * monitor start work
     */
    @Keep
    static synchronized void work(Context context, int port) {
        if (sIsWorking) {
            return;
        }
        sIsWorking = true;
        if (context == null) {
            throw new IllegalStateException("context can not be null.");
        }
        Context applicationContext = context.getApplicationContext();
        sGodEyeMonitorServer = new GodEyeMonitorServer(port);
        final HttpStaticProcessor httpStaticProcessor = new HttpStaticProcessor(applicationContext);
        final WebSocketBizProcessor webSocketBizProcessor = new WebSocketBizProcessor();
        sGodEyeMonitorServer.setMonitorServerCallback(new GodEyeMonitorServer.MonitorServerCallback() {
            @Override
            public void onClientAdded(List<WebSocket> webSockets, WebSocket added) {
                ModuleDriver.instance().start(sGodEyeMonitorServer);
                NotificationObserverManager.installNotificationListener("MONITOR", new MonitorNotificationListener(sGodEyeMonitorServer));
            }

            @Override
            public void onClientRemoved(List<WebSocket> webSockets, WebSocket removed) {
                if (webSockets == null || webSockets.isEmpty()) {
                    ModuleDriver.instance().stop();
                    NotificationObserverManager.uninstallNotificationListener("MONITOR");
                }
            }

            @Override
            public void onHttpRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                ThreadUtil.ensureWorkThread("AndroidGodEyeOnHttpRequest");
                try {
                    httpStaticProcessor.process(request, response);
                } catch (Throwable throwable) {
                    L.e(String.valueOf(throwable));
                }
            }

            @Override
            public void onWebSocketRequest(WebSocket webSocket, String messageFromClient) {
                ThreadUtil.ensureWorkThread("AndroidGodEyeOnWebSocketRequest");
                webSocketBizProcessor.process(webSocket, messageFromClient);
            }
        });
        sGodEyeMonitorServer.start();
        Log.d(L.DEFAULT_TAG, String.format(MONITOR_LOGCAT, port));
        L.d(getAddressLog(context, port));
    }

    /**
     * monitor stop work
     */
    @Keep
    static synchronized void shutDown() {
        if (sGodEyeMonitorServer != null) {
            sGodEyeMonitorServer.stop();
            sGodEyeMonitorServer = null;
        }
        ModuleDriver.instance().stop();
        NotificationObserverManager.uninstallNotificationListener("MONITOR");
        sIsWorking = false;
        L.d("GodEye monitor stopped.");
    }

    private static String getAddressLog(Context context, int port) {
        @SuppressLint("WifiManagerPotentialLeak")
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager != null ? wifiManager.getConnectionInfo().getIpAddress() : 0;
        final String formattedIpAddress = String.format(Locale.US, "%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
        return "Open AndroidGodEye dashboard [ http://localhost:" + port + "/index.html ] or [ http://" + formattedIpAddress + ":" + port + "/index.html ] in your browser";
    }
}

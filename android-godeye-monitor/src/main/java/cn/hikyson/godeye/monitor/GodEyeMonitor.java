package cn.hikyson.godeye.monitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;

import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import java.util.Map;

import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.monitor.driver.Watcher;
import cn.hikyson.godeye.monitor.modulemodel.AppInfo;
import cn.hikyson.godeye.monitor.processors.StaticProcessor;
import cn.hikyson.godeye.monitor.processors.WebSocketProcessor;
import cn.hikyson.godeye.monitor.server.GodEyeMonitorServer;

/**
 * Created by kysonchao on 2017/11/27.
 */
public class GodEyeMonitor {
    private static boolean sIsWorking = false;
    private static final int DEFAULT_PORT = 5390;
    //    private static ClientServer sClientServer;
    private static GodEyeMonitorServer sGodEyeMonitorServer;
    private static Watcher sWatcher;

    public interface AppInfoConext {
        Context getContext();

        Map<String, Object> getAppInfo();
    }

    /**
     * @param appInfoConext
     */
    public static synchronized void injectAppInfoConext(AppInfoConext appInfoConext) {
        AppInfo.injectAppInfoConext(appInfoConext);
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
//        Router.get().init(applicationContext);
        sGodEyeMonitorServer = new GodEyeMonitorServer(port);
        sGodEyeMonitorServer.start();
        final StaticProcessor assetsModule = new StaticProcessor(applicationContext);
        final WebSocketProcessor webSocketProcessor = new WebSocketProcessor(sGodEyeMonitorServer);
        sGodEyeMonitorServer.setMonitorServerCallback(new GodEyeMonitorServer.MonitorServerCallback() {
            @Override
            public void onHttpRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    StaticProcessor.StaticResource fileResult = assetsModule.process(request.getPath());
                    response.send(fileResult.contentType, fileResult.payload);
                } catch (Throwable throwable) {
                    L.e(String.valueOf(throwable));
                }
            }

            @Override
            public void onWebSocketRequest(WebSocket webSocket, String messageFromClient) {
                //解析客户端消息
                webSocket.send(webSocketProcessor.process(messageFromClient));
            }
        });
        sWatcher = new Watcher(webSocketProcessor);
        sWatcher.observeAll();
        L.d(getAddressLog(context, port));
        L.d("Leak dump files are in /storage/download/leakcanary-" + context.getPackageName());
        L.d("GodEye monitor is working...");
    }

    /**
     * monitor停止工作
     */
    public static synchronized void shutDown() {
        if (sGodEyeMonitorServer != null) {
            sGodEyeMonitorServer.stop();
            sGodEyeMonitorServer = null;
        }
        if (sWatcher != null) {
            sWatcher.cancelAllObserve();
            sWatcher = null;
        }
        sIsWorking = false;
        L.d("GodEye monitor stopped.");
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

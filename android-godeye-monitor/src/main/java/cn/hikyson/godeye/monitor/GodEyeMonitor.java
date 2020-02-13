package cn.hikyson.godeye.monitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import java.util.List;
import java.util.Locale;

import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import cn.hikyson.godeye.monitor.modules.appinfo.AppInfo;
import cn.hikyson.godeye.monitor.modules.appinfo.AppInfoLabel;
import cn.hikyson.godeye.monitor.modules.thread.ThreadRunningProcessClassifier;
import cn.hikyson.godeye.monitor.server.GodEyeMonitorServer;
import cn.hikyson.godeye.monitor.server.HttpStaticProcessor;
import cn.hikyson.godeye.monitor.server.ModuleDriver;
import cn.hikyson.godeye.monitor.server.WebSocketBizProcessor;

/**
 * Created by kysonchao on 2017/11/27.
 */
public class GodEyeMonitor {
    private static boolean sIsWorking = false;
    private static final int DEFAULT_PORT = 5390;
    private static final String MONITOR_LOGCAT = "AndroidGodEye monitor is running at port [%s]";
    private static GodEyeMonitorServer sGodEyeMonitorServer;
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public interface AppInfoConext {
        List<AppInfoLabel> getAppInfo();
    }

    /**
     * set app informations,it will show on the top of dashboard
     *
     * @param appInfoConext
     */
    public static void injectAppInfoConext(AppInfoConext appInfoConext) {
        AppInfo.injectAppInfoConext(appInfoConext);
    }

    public static synchronized void work(Context context) {
        work(context, DEFAULT_PORT);
    }

    /**
     * monitor start work
     */
    public static synchronized void work(Context context, int port) {
        if (sIsWorking) {
            return;
        }
        sIsWorking = true;
        if (context == null) {
            throw new IllegalStateException("context can not be null.");
        }
        sContext = context.getApplicationContext();
        sGodEyeMonitorServer = new GodEyeMonitorServer(port);
        final HttpStaticProcessor httpStaticProcessor = new HttpStaticProcessor(sContext);
        final WebSocketBizProcessor webSocketBizProcessor = new WebSocketBizProcessor();
        sGodEyeMonitorServer.setMonitorServerCallback(new GodEyeMonitorServer.MonitorServerCallback() {
            @Override
            public void onClientAdded(List<WebSocket> webSockets, WebSocket added) {
                ModuleDriver.instance().start(sGodEyeMonitorServer);
            }

            @Override
            public void onClientRemoved(List<WebSocket> webSockets, WebSocket removed) {
                if (webSockets == null || webSockets.isEmpty()) {
                    ModuleDriver.instance().stop();
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
    public static synchronized void shutDown() {
        if (sGodEyeMonitorServer != null) {
            sGodEyeMonitorServer.stop();
            sGodEyeMonitorServer = null;
        }
        ModuleDriver.instance().stop();
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

    public static Context getContext() {
        return sContext;
    }

    /**
     * set the class path of app process, indicate whether code is running in app process or system process
     * it will show in thread info list module of AndroidGodEye dashboard
     *
     * @param classPathPrefixes
     * @deprecated use {@link cn.hikyson.godeye.core.internal.modules.thread.ThreadTagger} to {@link cn.hikyson.godeye.core.internal.modules.thread.ThreadConfig#threadTagger}
     */
    @Deprecated
    public static void setClassPrefixOfAppProcess(List<String> classPathPrefixes) {
    }

    /**
     * set the ThreadRunningProcessClassifier of app process, indicate whether code is running in app process or system process
     * it will show in thread info list module of AndroidGodEye dashboard
     *
     * @param threadRunningProcessClassifier
     * @deprecated use {@link cn.hikyson.godeye.core.internal.modules.thread.ThreadTagger} to {@link cn.hikyson.godeye.core.internal.modules.thread.ThreadConfig#threadTagger}
     */
    @Deprecated
    public static void setThreadRunningProcessClassifier(ThreadRunningProcessClassifier threadRunningProcessClassifier) {
    }
}

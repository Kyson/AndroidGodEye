package cn.hikyson.godeye.monitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;

import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import java.util.List;
import java.util.Locale;

import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import cn.hikyson.godeye.monitor.modules.thread.ThreadInfoConverter;
import cn.hikyson.godeye.monitor.modules.thread.ThreadRunningProcessSorterClassPathPrefixImpl;
import cn.hikyson.godeye.monitor.server.Watcher;
import cn.hikyson.godeye.monitor.modules.AppInfo;
import cn.hikyson.godeye.monitor.modules.AppInfoLabel;
import cn.hikyson.godeye.monitor.server.HttpStaticProcessor;
import cn.hikyson.godeye.monitor.server.WebSocketBizProcessor;
import cn.hikyson.godeye.monitor.server.GodEyeMonitorServer;

/**
 * Created by kysonchao on 2017/11/27.
 */
public class GodEyeMonitor {
    private static boolean sIsWorking = false;
    private static final int DEFAULT_PORT = 5390;
    private static GodEyeMonitorServer sGodEyeMonitorServer;

    public interface AppInfoConext {
        Context getContext();

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

    /**
     * set the code class path ,it will show if a thread is running in app process for thread info list in dashboard
     *
     * @param classPathPrefixes
     */
    public static void setClassPathPrefixOfThreadRunningProcess(List<String> classPathPrefixes) {
        ThreadInfoConverter.setThreadRunningProcessSorter(new ThreadRunningProcessSorterClassPathPrefixImpl(classPathPrefixes));
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
        sGodEyeMonitorServer = new GodEyeMonitorServer(port);
        final HttpStaticProcessor httpStaticProcessor = new HttpStaticProcessor(applicationContext);
        final WebSocketBizProcessor webSocketBizProcessor = new WebSocketBizProcessor();
        sGodEyeMonitorServer.setMonitorServerCallback(new GodEyeMonitorServer.MonitorServerCallback() {
            @Override
            public void onHttpRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                ThreadUtil.ensureWorkThread("AndroidGodEyeOnHttpRequest");
                try {
                    boolean processed = httpStaticProcessor.process(request, response);
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
        Watcher.instance().setMessager(sGodEyeMonitorServer);
        Watcher.instance().start();
        L.d(getAddressLog(context, port));
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
        Watcher.instance().stop();
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

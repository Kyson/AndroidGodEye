package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hikyson.godeye.core.utils.L;

public class GodEyeMonitorServer implements Messager {

    private final int mPort;
    private AsyncHttpServer mServer;
    private final List<WebSocket> mWebSockets;
    private MonitorServerCallback mMonitorServerCallback;

    /**
     * server的消息回调
     */
    public interface MonitorServerCallback {
        void onHttpRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response);

        void onWebSocketRequest(WebSocket webSocket, String messageFromClient);
    }

    public GodEyeMonitorServer(int port) {
        mPort = port;
        mServer = new AsyncHttpServer();
        mWebSockets = Collections.synchronizedList(new ArrayList<>());
        mServer.websocket("/refresh", (webSocket, request) -> {
            mWebSockets.add(webSocket);
            L.d("connection build. current count:" + mWebSockets.size());
            webSocket.setClosedCallback(new CompletedCallback() {
                @Override
                public void onCompleted(Exception ex) {
                    mWebSockets.remove(webSocket);
                    L.d("connection released. current count:" + mWebSockets.size());
                }
            });
            webSocket.setStringCallback(s -> {
                if (mMonitorServerCallback != null) {
                    mMonitorServerCallback.onWebSocketRequest(webSocket, s);
                }
            });
        });
        mServer.get("/.*[(.html)|(.css)|(.js)|(.png)|(.jpg)|(.jpeg)|(.ico)]", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                if (mMonitorServerCallback != null) {
                    mMonitorServerCallback.onHttpRequest(request, response);
                }
            }
        });
    }

    public void setMonitorServerCallback(MonitorServerCallback monitorServerCallback) {
        mMonitorServerCallback = monitorServerCallback;
    }

    public void start() {
        mServer.listen(mPort);
    }

    public void stop() {
        mServer.stop();
        AsyncServer.getDefault().stop();
    }

    @Override
    public void sendMessage(String message) {
        Object[] wss = mWebSockets.toArray();
        if (wss != null) {
            for (Object s : wss) {
                WebSocket webSocket = (WebSocket) s;
                if (webSocket.isOpen()) {
                    webSocket.send(message);
                }
            }
        }
    }
}

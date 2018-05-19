package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import java.util.ArrayList;
import java.util.List;

public class GodEyeMonitorServer {

    private final int mPort;
    private AsyncHttpServer mServer;
    private List<WebSocket> mWebSockets;
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
        mWebSockets = new ArrayList<WebSocket>();
        mServer.websocket("/refresh", new AsyncHttpServer.WebSocketRequestCallback() {
            @Override
            public void onConnected(final WebSocket webSocket, AsyncHttpServerRequest request) {
                mWebSockets.add(webSocket);
                webSocket.setClosedCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception ex) {
                        mWebSockets.remove(webSocket);
                    }
                });
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
                        if (mMonitorServerCallback != null) {
                            mMonitorServerCallback.onWebSocketRequest(webSocket, s);
                        }
                    }
                });
            }
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
    }

    public void sendMessage(String message) {
        for (WebSocket socket : mWebSockets) {
            socket.send(message);
        }
    }
}

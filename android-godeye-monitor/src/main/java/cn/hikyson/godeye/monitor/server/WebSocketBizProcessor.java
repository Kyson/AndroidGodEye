package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

import cn.hikyson.godeye.core.utils.L;

public class WebSocketBizProcessor {
    private WebSocketBizRouter mWebSocketBizRouter;

    public WebSocketBizProcessor() {
        mWebSocketBizRouter = new WebSocketBizRouter();
    }

    public void process(WebSocket webSocket, String msg) {
        try {
            final JSONObject msgJSONObject = new JSONObject(msg);
            mWebSocketBizRouter.process(webSocket, msgJSONObject);
        } catch (Exception e) {
            L.e(String.valueOf(e));
        }
    }
}

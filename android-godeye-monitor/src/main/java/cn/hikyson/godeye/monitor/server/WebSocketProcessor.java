package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

public interface WebSocketProcessor {
    void process(WebSocket webSocket, JSONObject msgJSONObject) throws Exception;
}

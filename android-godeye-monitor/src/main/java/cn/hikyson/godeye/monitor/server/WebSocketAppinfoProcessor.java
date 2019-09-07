package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

import cn.hikyson.godeye.monitor.modules.appinfo.AppInfo;

public class WebSocketAppinfoProcessor implements WebSocketProcessor {
    @Override
    public void process(WebSocket webSocket, JSONObject msgJSONObject) {
        webSocket.send(new ServerMessage("appInfo", new AppInfo()).toString());
    }
}

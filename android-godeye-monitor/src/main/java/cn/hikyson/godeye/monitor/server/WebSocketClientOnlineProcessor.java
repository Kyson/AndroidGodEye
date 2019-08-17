package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

import java.util.Map;

import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.internal.modules.sm.Sm;

public class WebSocketClientOnlineProcessor implements WebSocketProcessor {

    @Override
    public void process(WebSocket webSocket, JSONObject msgJSONObject) throws Exception {
        for (Map.Entry<String, Object> entry : ModuleDriver.instance().messageCacheCopy().entrySet()) {
            webSocket.send(new ServerMessage(entry.getKey(), entry.getValue()).toString());
        }
        webSocket.send(new ServerMessage("reinstallBlock", GodEyeConfig.SmConfig.Factory.convert(Sm.instance().getSmContext())).toString());
    }
}

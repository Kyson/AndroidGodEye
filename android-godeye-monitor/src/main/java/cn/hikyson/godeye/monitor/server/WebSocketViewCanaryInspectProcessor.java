package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

import cn.hikyson.godeye.core.GodEyeHelper;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.utils.L;

public class WebSocketViewCanaryInspectProcessor implements WebSocketProcessor {
    @Override
    public void process(WebSocket webSocket, JSONObject msgJSONObject) {
        try {
            if ("inspect".equals(msgJSONObject.optString("payload"))) {
                GodEyeHelper.inspectView();
            }
        } catch (UninstallException e) {
            L.e(String.valueOf(e));
        }
    }
}

package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanary;
import cn.hikyson.godeye.core.utils.L;

public class WebSocketMethodCanaryProcessor implements WebSocketProcessor {
    @Override
    public void process(WebSocket webSocket, JSONObject msgJSONObject) {
        try {
            final MethodCanary methodCanary = GodEye.instance().getModule(GodEye.ModuleName.METHOD_CANARY);
            if ("start".equals(msgJSONObject.optString("payload"))) {
                methodCanary.startMonitor();
            } else if ("stop".equals(msgJSONObject.optString("payload"))) {
                methodCanary.stopMonitor();
            }
        } catch (UninstallException e) {
            L.e(String.valueOf(e));
        }
    }
}

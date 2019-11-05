package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanary;
import cn.hikyson.godeye.core.internal.modules.viewcanary.ViewCanary;
import cn.hikyson.godeye.core.utils.L;

public class WebSocketViewCanaryInspectProcessor implements WebSocketProcessor {
    @Override
    public void process(WebSocket webSocket, JSONObject msgJSONObject) {
        try {
            final ViewCanary viewCanary = GodEye.instance().getModule(GodEye.ModuleName.VIEW_CANARY);
            if ("inspect".equals(msgJSONObject.optString("payload"))) {
                viewCanary.inspect();
            }
        } catch (UninstallException e) {
            L.e(String.valueOf(e));
        }
    }
}

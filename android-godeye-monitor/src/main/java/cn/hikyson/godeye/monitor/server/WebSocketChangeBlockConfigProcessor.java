package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.internal.modules.sm.Sm;
import cn.hikyson.godeye.core.internal.modules.sm.SmConfig;
import cn.hikyson.godeye.core.utils.L;

public class WebSocketChangeBlockConfigProcessor implements WebSocketProcessor {
    @Override
    public void process(WebSocket webSocket, JSONObject msgJSONObject) {
        final JSONObject payloadJSONObject = msgJSONObject.optJSONObject("payload");
        try {
            Sm sm = GodEye.instance().getModule(GodEye.ModuleName.SM);
            if (payloadJSONObject == null) {
                return;
            }
            String type = payloadJSONObject.optString("type");
            if ("reset".equalsIgnoreCase(type)) {
                sm.clearSmConfigCache();
            } else {
                long longBlockThreshold = payloadJSONObject.optLong("longBlockThreshold");
                long shortBlockThreshold = payloadJSONObject.optLong("shortBlockThreshold");
                SmConfig newSmContext = new SmConfig(sm.config());
                if (longBlockThreshold > 0) {
                    newSmContext.longBlockThresholdMillis = longBlockThreshold;
                }
                if (shortBlockThreshold > 0) {
                    newSmContext.shortBlockThresholdMillis = shortBlockThreshold;
                }
                sm.setSmConfigCache(newSmContext);
            }
            SmConfig installConfig = sm.installConfig();
            sm.uninstall();
            sm.install(installConfig);
            webSocket.send(new ServerMessage("blockConfig", sm.config()).toString());
        } catch (UninstallException e) {
            L.e(String.valueOf(e));
        }
    }
}

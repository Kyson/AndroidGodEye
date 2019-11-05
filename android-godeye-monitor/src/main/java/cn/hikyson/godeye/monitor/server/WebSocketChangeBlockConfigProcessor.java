package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.internal.modules.sm.Sm;
import cn.hikyson.godeye.core.internal.modules.sm.SmContext;
import cn.hikyson.godeye.core.utils.L;

public class WebSocketChangeBlockConfigProcessor implements WebSocketProcessor {
    @Override
    public void process(WebSocket webSocket, JSONObject msgJSONObject) {
        final JSONObject payloadJSONObject = msgJSONObject.optJSONObject("payload");
        try {
            Sm sm = GodEye.instance().getModule(GodEye.ModuleName.SM);
            SmContext smContext = sm.getSmContext();
            GodEyeConfig.SmConfig newSmConfig;
            if (smContext == null) {
                newSmConfig = new GodEyeConfig.SmConfig();
            } else {
                newSmConfig = GodEyeConfig.SmConfig.Factory.convert(smContext);
            }
            long longBlockThreshold = payloadJSONObject.optLong("longBlockThreshold");
            long shortBlockThreshold = payloadJSONObject.optLong("shortBlockThreshold");
            if (longBlockThreshold > 0) {
                newSmConfig.longBlockThresholdMillis = longBlockThreshold;
            }
            if (shortBlockThreshold > 0) {
                newSmConfig.shortBlockThresholdMillis = shortBlockThreshold;
            }
            sm.uninstall();
            sm.install(newSmConfig);
            webSocket.send(new ServerMessage("reinstallBlock", newSmConfig).toString());
        } catch (UninstallException e) {
            L.e(String.valueOf(e));
        }
    }
}

package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.internal.modules.sm.Sm;
import cn.hikyson.godeye.core.internal.modules.sm.SmContext;

public class WebSocketChangeBlockConfigProcessor implements WebSocketProcessor {
    @Override
    public void process(WebSocket webSocket, JSONObject msgJSONObject) {
        final JSONObject payloadJSONObject = msgJSONObject.optJSONObject("payload");
        SmContext smContext = Sm.instance().getSmContext();
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
        Sm.instance().uninstall();
        Sm.instance().install(newSmConfig);
        webSocket.send(new ServerMessage("reinstallBlock", newSmConfig).toString());
    }
}

package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanary;
import io.reactivex.subjects.Subject;

public class WebSocketMethodCanaryStatusProcessor implements WebSocketProcessor {
    @Override
    public void process(WebSocket webSocket, JSONObject msgJSONObject) {
        Subject<String> methodCanaryStatusSubject = GodEye.instance().<MethodCanary>getModule(GodEye.ModuleName.METHOD_CANARY).statusSubject();
        if (methodCanaryStatusSubject != null && !methodCanaryStatusSubject.hasComplete() && !methodCanaryStatusSubject.hasThrowable()) {
            methodCanaryStatusSubject.onNext("GET");
        }
    }
}

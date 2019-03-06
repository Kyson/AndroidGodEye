package cn.hikyson.godeye.monitor.processors;

import com.koushikdutta.async.http.WebSocket;

public interface Processor {
    void process(WebSocket webSocket, String msg);
}

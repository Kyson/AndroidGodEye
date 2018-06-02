package cn.hikyson.godeye.monitor.processors;

import cn.hikyson.godeye.monitor.server.GodEyeMonitorServer;

public class WebSocketProcessor implements Messager {
    private GodEyeMonitorServer mGodEyeMonitorServer;

    public WebSocketProcessor(GodEyeMonitorServer godEyeMonitorServer) {
        mGodEyeMonitorServer = godEyeMonitorServer;
    }

    @Override
    public void sendMessage(String message) {
        mGodEyeMonitorServer.sendMessage(message);
    }

    public String process(String messageFromClient) {
        return "";
    }
}

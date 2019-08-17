package cn.hikyson.godeye.monitor.server;

import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.functions.Consumer;

public class SendMessageConsumer implements Consumer<ServerMessage> {
    private Messager mMessager;

    public SendMessageConsumer(Messager messager) {
        mMessager = messager;
    }

    @Override
    public void accept(ServerMessage serverMessage) throws Exception {
        ThreadUtil.ensureWorkThread("SendMessageConsumer accept");
        if (mMessager != null) {
            mMessager.sendMessage(serverMessage.toString());
        }
    }
}

package cn.hikyson.godeye.monitor.driver;

import cn.hikyson.godeye.core.utils.ThreadUtil;
import cn.hikyson.godeye.monitor.processors.Messager;
import io.reactivex.functions.Consumer;

public class SendMessageConsumer implements Consumer<ServerMessage> {
    private Messager mMessager;

    public SendMessageConsumer(Messager messager) {
        mMessager = messager;
    }

    @Override
    public void accept(ServerMessage serverMessage) throws Exception {
        ThreadUtil.ensureWorkThread("SendMessageConsumer accept");
        mMessager.sendMessage(serverMessage.toString());
    }
}

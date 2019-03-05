package cn.hikyson.godeye.monitor.driver;

import cn.hikyson.godeye.monitor.utils.GsonUtil;

/**
 * 客户端端发出的消息体
 */
public class ClientMessage {
    public String moduleName;
    public Object payload;

    public ClientMessage() {
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}

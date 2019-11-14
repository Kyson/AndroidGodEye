package cn.hikyson.godeye.monitor.server;

import androidx.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.monitor.utils.GsonUtil;

/**
 * 服务端发出的消息体
 */
@Keep
public class ServerMessage implements Serializable {

    public static final int SUCCESS = 1;
    public static final int DEFAULT_FAIL = 0;
    public int code;
    public String message;
    public DataWithName data;

    public ServerMessage(String errorMessage) {
        this.code = DEFAULT_FAIL;
        this.message = errorMessage;
        this.data = null;
    }

    public ServerMessage(String moduleName, Object data) {
        this.code = SUCCESS;
        this.message = "success";
        this.data = new DataWithName(moduleName, data);
    }

    @Keep
    public static class DataWithName implements Serializable {
        public String moduleName;
        public Object payload;

        public DataWithName(String moduleName, Object payload) {
            this.moduleName = moduleName;
            this.payload = payload;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public Object getPayload() {
            return payload;
        }

        public void setPayload(Object payload) {
            this.payload = payload;
        }
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

}

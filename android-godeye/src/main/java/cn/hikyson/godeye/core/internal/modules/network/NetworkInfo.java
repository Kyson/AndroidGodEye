package cn.hikyson.godeye.core.internal.modules.network;

import java.util.Map;

public class NetworkInfo<T extends NetworkContent> {
    public String summary;
    public boolean isSuccessful;
    public String message;
    public NetworkTime networkTime;
    public T networkContent;
    public Map<String, Object> extraInfo;

    @Override
    public String toString() {
        return "NetworkInfo{" +
                "summary='" + summary + '\'' +
                ", isSuccessful=" + isSuccessful +
                ", message='" + message + '\'' +
                ", networkTime=" + networkTime +
                ", networkContent=" + networkContent +
                ", extraInfo=" + extraInfo +
                '}';
    }
}

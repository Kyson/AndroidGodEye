package cn.hikyson.godeye.core.internal.modules.network;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Map;

@Keep
public class NetworkInfo<T extends NetworkContent> implements Serializable {
    public boolean isSuccessful;
    public String message;
    public String summary;
    public NetworkTime networkTime;
    public T networkContent;
    public Map<String, Object> extraInfo;

    public String toSummaryString() {
        return "NetworkInfo{" +
                "isSuccessful=" + isSuccessful +
                ", message='" + message + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return "NetworkInfo{" +
                "isSuccessful=" + isSuccessful +
                ", message='" + message + '\'' +
                ", summary='" + summary + '\'' +
                ", networkTime=" + networkTime +
                ", networkContent=" + networkContent +
                ", extraInfo=" + extraInfo +
                '}';
    }
}

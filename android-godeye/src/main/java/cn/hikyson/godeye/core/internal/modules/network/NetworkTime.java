package cn.hikyson.godeye.core.internal.modules.network;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.LinkedHashMap;

@Keep
public class NetworkTime implements Serializable {
    public long totalTimeMillis;
    public LinkedHashMap<String, Long> networkTimeMillisMap;

    public NetworkTime() {
        networkTimeMillisMap = new LinkedHashMap<>();
    }

    @Override
    public String toString() {
        return "NetworkTime{" +
                "totalTimeMillis=" + totalTimeMillis +
                ", networkTimeMillisMap=" + networkTimeMillisMap +
                '}';
    }
}

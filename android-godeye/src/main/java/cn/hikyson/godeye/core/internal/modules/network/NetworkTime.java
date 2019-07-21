package cn.hikyson.godeye.core.internal.modules.network;

import java.util.LinkedHashMap;

public class NetworkTime {
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

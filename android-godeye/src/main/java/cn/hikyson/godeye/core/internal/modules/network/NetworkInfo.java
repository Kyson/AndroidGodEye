package cn.hikyson.godeye.core.internal.modules.network;

import java.util.LinkedHashMap;
import java.util.Map;

public class NetworkInfo<T extends NetworkContent> {
    public boolean isSuccessful;
    public String message;
    public LinkedHashMap<String, Long> networkTimeMillisMap;
    public T networkContent;
    public Map<String, Object> extraInfo;
}

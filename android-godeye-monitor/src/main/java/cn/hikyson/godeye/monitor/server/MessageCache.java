package cn.hikyson.godeye.monitor.server;

import java.util.HashMap;
import java.util.Map;

public class MessageCache {
    private final Map<String, Object> mCachedMessage;

    MessageCache() {
        mCachedMessage = new HashMap<>();
    }

    public void put(String module, Object payload) {
        synchronized (mCachedMessage) {
            mCachedMessage.put(module, payload);
        }
    }

    public Map<String, Object> copy() {
        synchronized (mCachedMessage) {
            return new HashMap<>(mCachedMessage);
        }
    }
}

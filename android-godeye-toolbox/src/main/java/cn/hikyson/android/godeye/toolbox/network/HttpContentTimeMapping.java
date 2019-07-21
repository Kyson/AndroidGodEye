package cn.hikyson.android.godeye.toolbox.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;

public class HttpContentTimeMapping {
    private Map<Call, HttpContent> mCallHttpContentMap = new ConcurrentHashMap<>();

    public void addRecord(Call call, HttpContent httpContent) {
        mCallHttpContentMap.put(call, httpContent);
    }

    public HttpContent removeAndGetRecord(Call call) {
        HttpContent httpContent = mCallHttpContentMap.remove(call);
        if (httpContent == null) {
            return new HttpContent();
        }
        return httpContent;
    }
}

package cn.hikyson.android.godeye.okhttp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;

class HttpContentTimeMapping {
    private Map<Call, HttpContent> mCallHttpContentMap = new ConcurrentHashMap<>();

    void addRecord(Call call, HttpContent httpContent) {
        mCallHttpContentMap.put(call, httpContent);
    }

    HttpContent removeAndGetRecord(Call call) {
        HttpContent httpContent = mCallHttpContentMap.remove(call);
        if (httpContent == null) {
            return new HttpContent();
        }
        return httpContent;
    }
}

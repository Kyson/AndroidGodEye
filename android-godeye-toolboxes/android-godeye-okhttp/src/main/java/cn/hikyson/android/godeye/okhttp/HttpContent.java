package cn.hikyson.android.godeye.okhttp;

import androidx.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.core.internal.modules.network.NetworkContent;

@Keep
public class HttpContent implements NetworkContent, Serializable {
    public HttpRequest httpRequest;
    public HttpResponse httpResponse;

    public HttpContent() {
        httpRequest = new HttpRequest();
        httpResponse = new HttpResponse();
    }

    @Override
    public String requestToString() {
        return httpRequest == null ? "NULL" : httpRequest.getStandardFormat();
    }

    @Override
    public String responseToString() {
        return httpResponse == null ? "NULL" : httpResponse.getStandardFormat();
    }

    @Override
    public String toString() {
        return "HttpContent{" +
                "httpRequest=" + httpRequest +
                ", httpResponse=" + httpResponse +
                '}';
    }
}

package cn.hikyson.android.godeye.toolbox.network;

import android.support.annotation.Keep;

import cn.hikyson.godeye.core.internal.modules.network.NetworkContent;

@Keep
public class HttpContent implements NetworkContent {
    public HttpRequest httpRequest;
    public HttpResponse httpResponse;

    public HttpContent() {
        httpRequest = new HttpRequest();
        httpResponse = new HttpResponse();
    }

    @Override
    public String toString() {
        return "HttpContent{" +
                "httpRequest=" + httpRequest +
                ", httpResponse=" + httpResponse +
                '}';
    }
}

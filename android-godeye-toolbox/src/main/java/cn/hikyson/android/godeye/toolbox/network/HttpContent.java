package cn.hikyson.android.godeye.toolbox.network;

import cn.hikyson.godeye.core.internal.modules.network.NetworkContent;

public class HttpContent implements NetworkContent {
    public HttpRequest httpRequest;
    public HttpResponse httpResponse;

    public HttpContent() {
        httpRequest = new HttpRequest();
        httpResponse = new HttpResponse();
    }
}

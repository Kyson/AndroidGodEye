package cn.hikyson.godeye.core.internal.modules.network;

public class NetworkInfoRequest {
    public String method;
    public String url;

    public NetworkInfoRequest(String method, String url) {
        this.method = method;
        this.url = url;
    }

    @Override
    public String toString() {
        return "NetworkRequestConfig{" +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

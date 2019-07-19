package cn.hikyson.android.godeye.toolbox.network;

import java.util.Map;

public class HttpResponse {
    public String protocol;
    public int code;
    public String message;
    public Map<String, String> headers;
    public String payload;
}

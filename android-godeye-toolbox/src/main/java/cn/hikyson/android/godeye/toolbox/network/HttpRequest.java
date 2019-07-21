package cn.hikyson.android.godeye.toolbox.network;

import java.util.Map;

public class HttpRequest {
    public String method;
    public String url;
    public String protocol;
    public Map<String, String> headers;
    public String payload;

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", protocol='" + protocol + '\'' +
                ", headers=" + headers +
                ", payload='" + payload + '\'' +
                '}';
    }

    public String getStandardFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append(method).append(" ")
                .append(url).append(" ")
                .append(protocol).append("\n");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        sb.append("\n").append(payload);
        return String.valueOf(sb);
    }
}

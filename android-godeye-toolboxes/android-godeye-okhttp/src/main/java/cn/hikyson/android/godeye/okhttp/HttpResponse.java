package cn.hikyson.android.godeye.okhttp;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Map;

@Keep
public class HttpResponse implements Serializable {
    public String protocol;
    public int code;
    public String message;
    public Map<String, String> headers;
    public String payload;

    @Override
    public String toString() {
        return "HttpResponse{" +
                "protocol='" + protocol + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", headers=" + headers +
                ", payload='" + payload + '\'' +
                '}';
    }

    public String getStandardFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol).append(" ")
                .append(code).append(" ")
                .append(message).append("\n");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        sb.append("\n").append(payload);
        return String.valueOf(sb);
    }
}

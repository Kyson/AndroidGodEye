package cn.hikyson.godeye.core.internal.notification;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Map;

@Keep
public class NotificationContent implements Serializable {
    public String message;
    public Map<String, Object> extras;

    public NotificationContent(String message, Map<String, Object> extras) {
        this.message = message;
        this.extras = extras;
    }
}

package cn.hikyson.godeye.core.monitor;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class AppInfoLabel implements Serializable {
    public String name;
    public String value;
    public String url;

    public AppInfoLabel(String name, String value, String url) {
        this.name = name;
        this.value = value;
        this.url = url;
    }

    @Override
    public String toString() {
        return "AppInfoLabel{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

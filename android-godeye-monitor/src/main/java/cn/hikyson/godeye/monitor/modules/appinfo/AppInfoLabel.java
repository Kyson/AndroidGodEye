package cn.hikyson.godeye.monitor.modules.appinfo;

import android.support.annotation.Keep;

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
}

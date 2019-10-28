package cn.hikyson.godeye.core.internal.modules.appsize;

import java.io.Serializable;

public class AppSizeInfo implements Serializable {
    public static AppSizeInfo INVALID = new AppSizeInfo();
    public long cacheSize;
    public long dataSize;
    public long codeSize;

    @Override
    public String toString() {
        return "AppSizeInfo{" +
                "cacheSize=" + cacheSize +
                ", dataSize=" + dataSize +
                ", codeSize=" + codeSize +
                '}';
    }
}

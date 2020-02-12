package cn.hikyson.godeye.core.internal.modules.appsize;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * unit：byte
 */
@Keep
public class AppSizeInfo implements Serializable {
    public static AppSizeInfo INVALID = new AppSizeInfo();
    // byte
    public long cacheSize;
    // byte
    public long dataSize;
    // byte
    public long codeSize;

    public AppSizeInfo(long cacheSize, long dataSize, long codeSize) {
        this.cacheSize = cacheSize;
        this.dataSize = dataSize;
        this.codeSize = codeSize;
    }

    public AppSizeInfo() {
    }

    @Override
    public String toString() {
        return "AppSizeInfo{" +
                "cacheSize=" + cacheSize +
                ", dataSize=" + dataSize +
                ", codeSize=" + codeSize +
                '}';
    }
}

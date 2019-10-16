package cn.hikyson.godeye.core.internal.modules.appsize;

public class AppSizeInfo {
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

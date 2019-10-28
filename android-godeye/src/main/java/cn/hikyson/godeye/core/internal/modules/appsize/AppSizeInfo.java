package cn.hikyson.godeye.core.internal.modules.appsize;

public class AppSizeInfo {
    public long cacheSize;
    public long dataSize;
    public long codeSize;
    public Throwable error;

    @Override
    public String toString() {
        if (error != null) {
            return "AppSizeInfo{" +
                    "error=" + error.toString() +
                    '}';
        } else  {
            return "AppSizeInfo{" +
                    "cacheSize=" + cacheSize +
                    ", dataSize=" + dataSize +
                    ", codeSize=" + codeSize +
                    '}';
        }
    }
}

package cn.hikyson.godeye.core.internal.modules.imagecanary;

public class BitmapInfo {
    public int bitmapWidth;
    public int bitmapHeight;

    public boolean isValid() {
        return bitmapHeight > 0 && bitmapWidth > 0;
    }

    public int getSize() {
        return bitmapHeight * bitmapWidth;
    }

    @Override
    public String toString() {
        return "BitmapInfo{" +
                "bitmapWidth=" + bitmapWidth +
                ", bitmapHeight=" + bitmapHeight +
                ", bitmapSize=" + getSize() +
                '}';
    }
}

package cn.hikyson.godeye.core.internal.modules.imagecanary;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class BitmapInfo implements Serializable {
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

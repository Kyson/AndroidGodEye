package cn.hikyson.godeye.core.internal.modules.imagecanary;

import androidx.annotation.Keep;

@Keep
public interface ImageCanaryConfigProvider {
    boolean isBitmapQualityTooHigh(int bitmapWidth, int bitmapHeight, int imageViewWidth, int imageViewHeight);

    boolean isBitmapQualityTooLow(int bitmapWidth, int bitmapHeight, int imageViewWidth, int imageViewHeight);
}

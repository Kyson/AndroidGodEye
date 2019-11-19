package cn.hikyson.godeye.core.internal.modules.imagecanary;

import androidx.annotation.Keep;

import java.util.List;

import io.reactivex.annotations.NonNull;

@Keep
public interface ImageCanaryConfigProvider {
    boolean isBitmapQualityTooHigh(int bitmapWidth, int bitmapHeight, int imageViewWidth, int imageViewHeight);
    boolean isBitmapQualityTooLow(int bitmapWidth, int bitmapHeight, int imageViewWidth, int imageViewHeight);
    @NonNull
    List<BitmapInfoAnalyzer> getExtraBitmapInfoAnalyzers();
}

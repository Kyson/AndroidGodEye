package cn.hikyson.godeye.core.internal.modules.imagecanary;

import java.util.List;

import io.reactivex.annotations.NonNull;

public interface ImageCanaryConfigProvider {
    boolean isBitmapQualityTooHigh(int bitmapWidth, int bitmapHeight, int imageViewWidth, int imageViewHeight);
    boolean isBitmapQualityTooLow(int bitmapWidth, int bitmapHeight, int imageViewWidth, int imageViewHeight);
    @NonNull
    List<BitmapInfoAnalyzer> getExtraBitmapInfoAnalyzers();
}

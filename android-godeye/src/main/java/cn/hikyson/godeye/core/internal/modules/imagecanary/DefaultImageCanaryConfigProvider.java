package cn.hikyson.godeye.core.internal.modules.imagecanary;

import java.util.List;

public class DefaultImageCanaryConfigProvider implements ImageCanaryConfigProvider {

    @Override
    public boolean isBitmapQualityTooHigh(int bitmapWidth, int bitmapHeight, int imageViewWidth, int imageViewHeight) {
        return bitmapWidth * bitmapHeight > imageViewWidth * imageViewHeight * 1.5;
    }

    @Override
    public boolean isBitmapQualityTooLow(int bitmapWidth, int bitmapHeight, int imageViewWidth, int imageViewHeight) {
        return bitmapWidth * bitmapHeight * 2 < imageViewWidth * imageViewHeight;
    }

    @Override
    public List<BitmapInfoAnalyzer> getExtraBitmapInfoAnalyzers() {
        return null;
    }
}

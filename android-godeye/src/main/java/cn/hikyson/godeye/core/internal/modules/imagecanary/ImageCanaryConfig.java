package cn.hikyson.godeye.core.internal.modules.imagecanary;


import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class ImageCanaryConfig implements Serializable {

    // ImageCanaryConfigProvider
    public String imageCanaryConfigProvider;

    public ImageCanaryConfig() {
        this.imageCanaryConfigProvider = DefaultImageCanaryConfigProvider.class.getName();
    }

    public ImageCanaryConfig(String imageCanaryConfigProvider) {
        this.imageCanaryConfigProvider = imageCanaryConfigProvider;
    }

    // ImageCanaryConfigProvider
    public String getImageCanaryConfigProvider() {
        return imageCanaryConfigProvider;
    }

    @Override
    public String toString() {
        return "ImageCanaryConfig{" +
                "imageCanaryConfigProvider='" + imageCanaryConfigProvider + '\'' +
                '}';
    }
}
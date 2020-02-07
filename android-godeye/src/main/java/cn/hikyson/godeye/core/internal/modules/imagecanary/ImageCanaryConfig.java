package cn.hikyson.godeye.core.internal.modules.imagecanary;


import android.app.Application;

import androidx.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.core.GodEye;

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

    public Application getApplication() {
        return GodEye.instance().getApplication();
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
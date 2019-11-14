package cn.hikyson.godeye.core.internal.modules.imagecanary;

import android.app.Application;

public interface ImageCanaryContext {
    Application getApplication();
    ImageCanaryConfigProvider getImageCanaryConfigProvider();
}

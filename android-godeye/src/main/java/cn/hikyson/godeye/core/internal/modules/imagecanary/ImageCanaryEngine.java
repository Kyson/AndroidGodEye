package cn.hikyson.godeye.core.internal.modules.imagecanary;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;

public class ImageCanaryEngine extends ProduceableSubject<ImageIssue> implements Install<ImageCanaryContext> {

    private boolean mInstalled = false;
    private ImageCanaryContext mConfig;

    @Override
    public synchronized void install(ImageCanaryContext config) {
        if (config.getImageCanaryConfigProvider() == null) {
           return;
        }
        ImageCanary.addAnalyzers(config.getImageCanaryConfigProvider().getExtraBitmapInfoAnalyzers());
        mConfig = config;
        ImageCanary.start(config.getApplication(), this);
        mInstalled = true;
    }

    @Override
    public synchronized void uninstall() {
        ImageCanary.stop(config().getApplication());
        mInstalled = false;
    }

    @Override
    public boolean isInstalled() {
        return mInstalled;
    }

    @Override
    public ImageCanaryContext config() {
        return mConfig;
    }
}

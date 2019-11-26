package cn.hikyson.godeye.core.internal.modules.imagecanary;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class ImageCanary extends ProduceableSubject<ImageIssue> implements Install<ImageCanaryContext> {

    private boolean mInstalled = false;
    private ImageCanaryContext mConfig;
    private ImageCanaryInternal mImageCanaryInternal;

    @Override
    public synchronized void install(ImageCanaryContext config) {
        if (mInstalled) {
            L.d("ImageCanary already installed, ignore.");
            return;
        }
        mConfig = config;
        ImageCanaryConfigProvider imageCanaryConfigProvider = new DefaultImageCanaryConfigProvider();
        try {
            imageCanaryConfigProvider = (ImageCanaryConfigProvider) Class.forName(mConfig.getImageCanaryConfigProvider()).newInstance();
        } catch (Throwable e) {
            L.e("ImageCanary install warning, can not find imageCanaryConfigProvider class. use DefaultImageCanaryConfigProvider:" + e);
        }
        mImageCanaryInternal = new ImageCanaryInternal(imageCanaryConfigProvider);
        mImageCanaryInternal.start(mConfig.getApplication(), this);
        mInstalled = true;
        L.d("ImageCanary installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("ImageCanary already uninstalled, ignore.");
            return;
        }
        if (mImageCanaryInternal != null) {
            mImageCanaryInternal.stop(config().getApplication());
            mImageCanaryInternal = null;
        }
        mInstalled = false;
        L.d("ImageCanary uninstalled.");
    }

    @Override
    public boolean isInstalled() {
        return mInstalled;
    }

    @Override
    public ImageCanaryContext config() {
        return mConfig;
    }

    @Override
    protected Subject<ImageIssue> createSubject() {
        return ReplaySubject.create();
    }
}

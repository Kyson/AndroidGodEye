package cn.hikyson.godeye.core.internal.modules.imagecanary;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class ImageCanary extends ProduceableSubject<ImageIssue> implements Install<ImageCanaryConfig> {

    private boolean mInstalled = false;
    private ImageCanaryConfig mConfig;
    private ImageCanaryInternal mImageCanaryInternal;

    @Override
    public synchronized boolean install(ImageCanaryConfig config) {
        if (mInstalled) {
            L.d("ImageCanary already installed, ignore.");
            return true;
        }
        mConfig = config;
        ImageCanaryConfigProvider imageCanaryConfigProvider = new DefaultImageCanaryConfigProvider();
        try {
            imageCanaryConfigProvider = (ImageCanaryConfigProvider) Class.forName(mConfig.getImageCanaryConfigProvider()).newInstance();
        } catch (Throwable e) {
            L.e("ImageCanary install warning, can not find imageCanaryConfigProvider class. use DefaultImageCanaryConfigProvider:" + e);
        }
        mImageCanaryInternal = new ImageCanaryInternal(imageCanaryConfigProvider);
        mImageCanaryInternal.start(GodEye.instance().getApplication(), this);
        mInstalled = true;
        L.d("ImageCanary installed.");
        return true;
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("ImageCanary already uninstalled, ignore.");
            return;
        }
        if (mImageCanaryInternal != null) {
            mImageCanaryInternal.stop(GodEye.instance().getApplication());
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
    public ImageCanaryConfig config() {
        return mConfig;
    }

    @Override
    protected Subject<ImageIssue> createSubject() {
        return ReplaySubject.create();
    }
}

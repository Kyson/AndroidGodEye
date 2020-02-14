package cn.hikyson.godeye.core.internal.modules.appsize;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class AppSize extends ProduceableSubject<AppSizeInfo> implements Install<AppSizeConfig> {
    private Disposable disposable;
    private boolean mInstalled = false;
    private AppSizeConfig mConfig;

    @Override
    public synchronized boolean install(AppSizeConfig config) {
        if (mInstalled) {
            L.d("AppSize already installed, ignore.");
            return true;
        }
        mInstalled = true;
        mConfig = config;
        disposable = ThreadUtil.computationScheduler().scheduleDirect(() -> AppSizeUtil.getAppSize(GodEye.instance().getApplication(), new AppSizeUtil.OnGetSizeListener() {
            @Override
            public void onGetSize(AppSizeInfo appSizeInfo) {
                L.d("AppSize onGetSize: cache size: %s, data size: %s, codeSize: %s", AppSizeUtil.formatSize(appSizeInfo.cacheSize),
                        AppSizeUtil.formatSize(appSizeInfo.dataSize), AppSizeUtil.formatSize(appSizeInfo.codeSize));
                produce(appSizeInfo);
            }

            @Override
            public void onError(Throwable t) {
                L.d("AppSize onGetError: %s", String.valueOf(t));
                produce(AppSizeInfo.INVALID);
            }
        }), config.delayMillis(), TimeUnit.MILLISECONDS);
        L.d("AppSize installed.");
        return true;
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("AppSize already uninstalled, ignore.");
            return;
        }
        if (disposable != null) {
            disposable.dispose();
        }
        mConfig = null;
        mInstalled = false;
        L.d("AppSize uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return this.mInstalled;
    }

    @Override
    public AppSizeConfig config() {
        return mConfig;
    }

    @Override
    protected Subject<AppSizeInfo> createSubject() {
        return BehaviorSubject.create();
    }
}

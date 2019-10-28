package cn.hikyson.godeye.core.internal.modules.appsize;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.internal.modules.leakdetector.GodEyeCanaryLog;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AppSize extends ProduceableSubject<AppSizeInfo> implements Install<AppSizeContext> {

    private Disposable disposable;
    private boolean mInstalled = false;

    @Override
    public void install(AppSizeContext config) {
        if (mInstalled) {
            L.d("pageload already installed, ignore.");
            return;
        }
        L.d("app size installed.");
        mInstalled = true;
        disposable = Schedulers.single().scheduleDirect(() -> AppSizeUtil.getAppSize(config.context(), new AppSizeUtil.OnGetSizeListener() {
            @Override
            public void onGetSize(AppSizeInfo appSizeInfo) {
                GodEyeCanaryLog.d(String.format("Godeye app size report: cache size: %s, data size: %s, codeSize: %s", AppSizeUtil.formatSize(appSizeInfo.cacheSize),
                        AppSizeUtil.formatSize(appSizeInfo.dataSize), AppSizeUtil.formatSize(appSizeInfo.codeSize)));
                produce(appSizeInfo);
            }

            @Override
            public void onError(Throwable t) {
                AppSizeInfo appSizeInfo = new AppSizeInfo();
                appSizeInfo.error = t;
                produce(appSizeInfo);
            }
        }), config.delayMilliseconds(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void uninstall() {
        if (!mInstalled) {
            L.d("pageload already uninstalled, ignore.");
            return;
        }
        L.d("app size uninstalled.");
        if (disposable != null) {
            disposable.dispose();
        }
        mInstalled = false;
    }
}

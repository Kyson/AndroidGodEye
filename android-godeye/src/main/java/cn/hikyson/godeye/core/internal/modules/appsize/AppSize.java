package cn.hikyson.godeye.core.internal.modules.appsize;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class AppSize extends ProduceableSubject<AppSizeInfo> implements Install<AppSizeContext> {

    private Disposable disposable;
    private boolean mInstalled = false;

    @Override
    public synchronized void install(AppSizeContext config) {
        if (mInstalled) {
            L.d("AppSize already installed, ignore.");
            return;
        }
        mInstalled = true;
        disposable = Schedulers.single().scheduleDirect(() -> AppSizeUtil.getAppSize(config.context(), new AppSizeUtil.OnGetSizeListener() {
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
        mInstalled = false;
        L.d("AppSize uninstalled.");
    }

    @Override
    protected Subject<AppSizeInfo> createSubject() {
        return BehaviorSubject.create();
    }
}

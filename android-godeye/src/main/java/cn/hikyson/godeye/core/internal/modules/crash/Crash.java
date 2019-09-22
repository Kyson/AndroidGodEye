package cn.hikyson.godeye.core.internal.modules.crash;

import java.util.List;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.Preconditions;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * 崩溃采集模块
 * 安装卸载可以任意线程（会切换到主线程执行）
 * 发射数据子线程和主线程都有可能
 * Created by kysonchao on 2017/12/18.
 */
public class Crash extends ProduceableSubject<List<CrashInfo>> implements Install<CrashProvider> {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private boolean mInstalled;

    @Override
    public void install(final CrashProvider crashProvider) {
        Preconditions.checkNotNull(crashProvider);
        if (ThreadUtil.isMainThread()) {
            installInMain(crashProvider);
        } else {
            ThreadUtil.sMain.execute(() -> installInMain(crashProvider));
        }
    }

    private void installInMain(CrashProvider crashProvider) {
        if (mInstalled) {
            L.d("crash already installed , ignore");
            return;
        }
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this, crashProvider, mDefaultHandler));
        mInstalled = true;
        L.d("crash installed");
    }

    @Override
    public void uninstall() {
        if (ThreadUtil.isMainThread()) {
            uninstallInMain();
        } else {
            ThreadUtil.sMain.execute(this::uninstallInMain);
        }
    }

    private void uninstallInMain() {
        if (!mInstalled) {
            L.d("crash already uninstalled , ignore");
            return;
        }
        Thread.setDefaultUncaughtExceptionHandler(mDefaultHandler);
        mInstalled = false;
        L.d("crash uninstalled");
    }

    @Override
    protected Subject<List<CrashInfo>> createSubject() {
        return BehaviorSubject.create();
    }
}

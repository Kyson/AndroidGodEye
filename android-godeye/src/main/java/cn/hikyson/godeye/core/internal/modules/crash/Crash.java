package cn.hikyson.godeye.core.internal.modules.crash;

import java.util.List;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.Preconditions;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by kysonchao on 2017/12/18.
 */
public class Crash extends ProduceableSubject<List<CrashInfo>> implements Install<CrashProvider> {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private boolean mInstalled;

    @Override
    public synchronized void install(final CrashProvider crashProvider) {
        if (mInstalled) {
            L.d("crash already installed , ignore");
            return;
        }
        Preconditions.checkNotNull(crashProvider);
        if (ThreadUtil.isMainThread()) {
            installInMain(crashProvider);
        } else {
            ThreadUtil.sMain.execute(new Runnable() {
                @Override
                public void run() {
                    installInMain(crashProvider);
                }
            });
        }
    }

    private void installInMain(CrashProvider crashProvider) {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this, crashProvider, mDefaultHandler));
        mInstalled = true;
        L.d("crash installed");
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("crash already uninstalled , ignore");
            return;
        }
        if (ThreadUtil.isMainThread()) {
            uninstallInMain();
        } else {
            ThreadUtil.sMain.execute(new Runnable() {
                @Override
                public void run() {
                    uninstallInMain();
                }
            });
        }
    }

    private void uninstallInMain() {
        Thread.setDefaultUncaughtExceptionHandler(mDefaultHandler);
        mInstalled = false;
        L.d("crash uninstalled");
    }

    @Override
    protected Subject<List<CrashInfo>> createSubject() {
        return BehaviorSubject.create();
    }
}

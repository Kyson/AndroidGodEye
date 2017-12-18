package cn.hikyson.godeye.core.internal.modules.crash;

import java.util.List;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by kysonchao on 2017/12/18.
 */
public class Crash extends ProduceableSubject<List<CrashInfo>> implements Install<CrashProvider> {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private boolean mInstalled;

    @Override
    public synchronized void install(CrashProvider crashProvider) {
        if (!mInstalled) {
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this, crashProvider, mDefaultHandler));
            mInstalled = true;
            L.d("crash installed");
        } else {
            L.d("crash already installed , ignore");
        }
    }

    @Override
    public synchronized void uninstall() {
        if (mInstalled) {
            Thread.setDefaultUncaughtExceptionHandler(mDefaultHandler);
            mInstalled = false;
            L.d("crash uninstalled");
        } else {
            L.d("crash already uninstalled , ignore");
        }

    }

    @Override
    protected Subject<List<CrashInfo>> createSubject() {
        return BehaviorSubject.create();
    }
}

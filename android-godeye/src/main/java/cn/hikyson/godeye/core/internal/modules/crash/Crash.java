package cn.hikyson.godeye.core.internal.modules.crash;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by kysonchao on 2017/12/18.
 */
public class Crash extends ProduceableSubject<CrashInfo> implements Install<Void> {
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    @Override
    public void install(Void config) {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this, mDefaultHandler));
        L.d("crash installed");
    }

    @Override
    public void uninstall() {
        Thread.setDefaultUncaughtExceptionHandler(mDefaultHandler);
        L.d("crash uninstalled");
    }

    @Override
    protected Subject<CrashInfo> createSubject() {
        return BehaviorSubject.create();
    }
}

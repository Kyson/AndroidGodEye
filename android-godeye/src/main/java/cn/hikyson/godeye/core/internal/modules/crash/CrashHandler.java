package cn.hikyson.godeye.core.internal.modules.crash;

import java.util.List;
import java.util.concurrent.Callable;

import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kysonchao on 2017/12/18.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private CrashProvider mCrashProvider;

    public CrashHandler(final Producer<List<CrashInfo>> producer, CrashProvider crashProvider, Thread.UncaughtExceptionHandler defaultHandler) {
        mDefaultHandler = defaultHandler;
        mCrashProvider = crashProvider;
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ThreadUtil.ensureWorkThread("CrashHandler call");
                try {
                    if (mCrashProvider != null) {
                        producer.produce(mCrashProvider.restoreCrash());
                    }
                } catch (Throwable throwable) {
                    L.e(String.valueOf(throwable));
                }
                return true;
            }
        }).subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation()).subscribe();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            if (mCrashProvider != null) {
                mCrashProvider.storeCrash(new CrashInfo(System.currentTimeMillis(), thread, ex));
            }
        } catch (Throwable throwable) {
            L.e(String.valueOf(throwable));
        }
        mDefaultHandler.uncaughtException(thread, ex);
    }
}
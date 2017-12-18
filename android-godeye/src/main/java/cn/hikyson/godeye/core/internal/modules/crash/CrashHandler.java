package cn.hikyson.godeye.core.internal.modules.crash;

import java.util.List;

import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.L;

/**
 * Created by kysonchao on 2017/12/18.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private CrashProvider mCrashProvider;

    public CrashHandler(Producer<List<CrashInfo>> producer, CrashProvider crashProvider, Thread.UncaughtExceptionHandler defaultHandler) {
        mDefaultHandler = defaultHandler;
        mCrashProvider = crashProvider;
        try {
            producer.produce(crashProvider.restoreCrash());
        } catch (Throwable throwable) {
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            mCrashProvider.storeCrash(new CrashInfo(thread, ex));
        } catch (Throwable throwable) {
        }
        mDefaultHandler.uncaughtException(thread, ex);
    }
}
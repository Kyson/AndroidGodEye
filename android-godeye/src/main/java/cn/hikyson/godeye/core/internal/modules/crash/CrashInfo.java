package cn.hikyson.godeye.core.internal.modules.crash;

import cn.hikyson.godeye.core.utils.StacktraceUtil;

/**
 * Created by kysonchao on 2017/12/18.
 */
public class CrashInfo {
    public Thread thread;
    public Throwable throwable;

    public CrashInfo(Thread thread, Throwable throwable) {
        this.thread = thread;
        this.throwable = throwable;
        throwable.getStackTrace();
        throwable.getLocalizedMessage();
//        StacktraceUtil.convertToStackString();
    }

    @Override
    public String toString() {
        return "CrashInfo{" +
                "thread=" + thread +
                ", throwable=" + throwable +
                '}';
    }
}

package cn.hikyson.godeye.core.internal.modules.crash;

import android.support.annotation.Keep;

import java.io.Serializable;
import java.util.List;

import cn.hikyson.godeye.core.utils.StacktraceUtil;


/**
 * Created by kysonchao on 2017/12/18.
 */
@Keep
public class CrashInfo implements Serializable {
    public long timestampMillis;
    public String threadName;
    public String threadState;
    public String threadGroupName;
    public boolean threadIsDaemon;
    public boolean threadIsAlive;
    public boolean threadIsInterrupted;
    public String throwableMessage;
    public List<String> throwableStacktrace;

    public CrashInfo() {
    }

    public CrashInfo(long timestampMillis, Thread thread, Throwable throwable) {
        this.timestampMillis = timestampMillis;
        this.threadName = thread.getName();
        this.threadState = String.valueOf(thread.getState());
        if (thread.getThreadGroup() != null) {
            this.threadGroupName = String.valueOf(thread.getThreadGroup().getName());
        }
        this.threadIsDaemon = thread.isDaemon();
        this.threadIsAlive = thread.isAlive();
        this.threadIsInterrupted = thread.isInterrupted();
        this.throwableMessage = throwable.getLocalizedMessage();
        this.throwableStacktrace = StacktraceUtil.getStack(throwable.getStackTrace());
    }

    @Override
    public String toString() {
        return "CrashInfo{" +
                "timestampMillis=" + timestampMillis +
                ", threadName='" + threadName + '\'' +
                ", threadState='" + threadState + '\'' +
                ", threadGroupName='" + threadGroupName + '\'' +
                ", threadIsDaemon=" + threadIsDaemon +
                ", threadIsAlive=" + threadIsAlive +
                ", threadIsInterrupted=" + threadIsInterrupted +
                ", throwableMessage='" + throwableMessage + '\'' +
                ", throwableStacktrace=" + throwableStacktrace +
                '}';
    }
}

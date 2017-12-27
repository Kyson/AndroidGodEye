package cn.hikyson.godeye.core.internal.modules.crash;

import java.io.Serializable;
import java.util.List;

import cn.hikyson.godeye.core.utils.StacktraceUtil;


/**
 * Created by kysonchao on 2017/12/18.
 */
public class CrashInfo implements Serializable {
    public String threadName;
    public String threadState;
    public String threadGroupName;
    public boolean threadIsDaemon;
    public boolean threadIsAlive;
    public boolean threadIsInterrupted;
    public String throwableMessage;
    public List<String> throwableStacktrace;

    public CrashInfo(Thread thread, Throwable throwable) {
        threadName = thread.getName();
        threadState = String.valueOf(thread.getState());
        threadGroupName = String.valueOf(thread.getThreadGroup().getName());
        threadIsDaemon = thread.isDaemon();
        threadIsAlive = thread.isAlive();
        threadIsInterrupted = thread.isInterrupted();
        throwableMessage = throwable.getLocalizedMessage();
        throwableStacktrace = StacktraceUtil.getStack(throwable.getStackTrace());
    }

    @Override
    public String toString() {
        return "CrashInfo{" +
                "threadName='" + threadName + '\'' +
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

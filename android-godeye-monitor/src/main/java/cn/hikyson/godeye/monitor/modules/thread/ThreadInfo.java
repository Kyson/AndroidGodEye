package cn.hikyson.godeye.monitor.modules.thread;

import android.support.annotation.Keep;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

import cn.hikyson.godeye.core.utils.StacktraceUtil;

/**
 * Created by kysonchao on 2018/1/17.
 */
@Keep
public class ThreadInfo implements Serializable {
    @Expose
    public long id;
    @Expose
    public String name;
    @Expose
    public String state;
    @Expose
    public boolean deamon;
    @Expose
    public int priority;
    @Expose
    public boolean isAlive;
    @Expose
    public boolean isInterrupted;
    @Expose
    public List<String> stackTraceElements;
    @Expose
    public ThreadRunningProcess threadRunningProcess;

    public ThreadInfo(Thread thread, ThreadRunningProcessClassifier threadRunningProcessClassifier) {
        this.id = thread.getId();
        this.name = thread.getName();
        this.state = String.valueOf(thread.getState());
        this.deamon = thread.isDaemon();
        this.priority = thread.getPriority();
        this.isAlive = thread.isAlive();
        this.isInterrupted = thread.isInterrupted();
        this.stackTraceElements = StacktraceUtil.getStack(thread.getStackTrace());
        if (threadRunningProcessClassifier != null) {
            this.threadRunningProcess = threadRunningProcessClassifier.classify(this);
        } else {
            this.threadRunningProcess = ThreadRunningProcess.UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", deamon=" + deamon +
                ", priority=" + priority +
                ", isAlive=" + isAlive +
                ", isInterrupted=" + isInterrupted +
                ", stackTraceElements=" + stackTraceElements +
                ", threadRunningProcess=" + threadRunningProcess +
                '}';
    }
}

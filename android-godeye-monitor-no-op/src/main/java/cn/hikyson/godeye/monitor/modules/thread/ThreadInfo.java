package cn.hikyson.godeye.monitor.modules.thread;

import java.util.List;

/**
 * Created by kysonchao on 2018/1/17.
 */
public class ThreadInfo {
    public long id;
    public String name;
    public String state;
    public boolean deamon;
    public int priority;
    public boolean isAlive;
    public boolean isInterrupted;
    public List<String> stackTraceElements;
    public ThreadRunningProcess threadRunningProcess;

    public ThreadInfo(Thread thread, ThreadRunningProcessClassifier threadRunningProcessClassifier) {
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

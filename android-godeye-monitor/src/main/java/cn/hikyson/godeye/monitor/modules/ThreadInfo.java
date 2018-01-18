package cn.hikyson.godeye.monitor.modules;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

import cn.hikyson.godeye.core.utils.StacktraceUtil;

/**
 * Created by kysonchao on 2018/1/17.
 */
public class ThreadInfo {
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
    public String deadlock;


    public ThreadInfo(Thread thread) {
        this.name = thread.getName();
        this.state = String.valueOf(thread.getState());
        this.deamon = thread.isDaemon();
        this.priority = thread.getPriority();
        this.id = thread.getId();
        this.isAlive = thread.isAlive();
        this.isInterrupted = thread.isInterrupted();
        this.stackTraceElements = StacktraceUtil.getStack(thread.getStackTrace());
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", deamon=" + deamon +
                ", priority=" + priority +
                ", isAlive=" + isAlive +
                ", isInterrupted=" + isInterrupted +
                ", stackTraceElements=" + stackTraceElements +
                '}';
    }

    public static List<ThreadInfo> convert(List<Thread> threads) {
        List<ThreadInfo> threadWrappers = new ArrayList<>();
        for (Thread thread : threads) {
            if (thread == null) {
                continue;
            }
            threadWrappers.add(new ThreadInfo(thread));
        }
        return threadWrappers;
    }

    public static List<ThreadInfo> convert(Thread... threads) {
        List<ThreadInfo> threadWrappers = new ArrayList<>();
        for (Thread thread : threads) {
            if (thread == null) {
                continue;
            }
            threadWrappers.add(new ThreadInfo(thread));
        }
        return threadWrappers;
    }
}

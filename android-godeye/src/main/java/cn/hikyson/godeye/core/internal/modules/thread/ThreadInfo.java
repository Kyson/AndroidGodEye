package cn.hikyson.godeye.core.internal.modules.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 线程信息
 * Created by kysonchao on 2018/1/12.
 */
public class ThreadInfo {
    public long id;
    public String name;
    public Thread.State state;
    public boolean deamon;
    public int priority;
    public boolean isAlive;
    public boolean isInterrupted;
    public StackTraceElement[] stackTraceElements;

    public ThreadInfo(Thread thread) {
        this.name = thread.getName();
        this.state = thread.getState();
        this.deamon = thread.isDaemon();
        this.priority = thread.getPriority();
        this.id = thread.getId();
        this.isAlive = thread.isAlive();
        this.isInterrupted = thread.isInterrupted();
        this.stackTraceElements = thread.getStackTrace();
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
                ", stackTraceElements=" + Arrays.toString(stackTraceElements) +
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

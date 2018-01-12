package cn.hikyson.godeye.core.internal.modules.deadlock;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kysonchao on 2018/1/12.
 */
public class ThreadDumper {
    /**
     * dump当前所有线程
     *
     * @param threadFilter
     * @return
     */
    public static List<Thread> dump(@NonNull ThreadFilter threadFilter) {
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parentGroup;
        while ((parentGroup = rootGroup.getParent()) != null) {
            rootGroup = parentGroup;
        }
        Thread[] threads = new Thread[rootGroup.activeCount()];
        while (rootGroup.enumerate(threads, true) >= threads.length) {
            threads = new Thread[threads.length * 2];
        }
        List<Thread> threadList = new ArrayList<>();
        for (Thread thread : threads) {
            if (thread != null && !threadList.contains(thread) && threadFilter.filter(thread)) {
                threadList.add(thread);
            }
        }
        return threadList;
    }

    public interface ThreadFilter {
        boolean filter(Thread thread);
    }
}

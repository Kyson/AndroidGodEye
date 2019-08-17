package cn.hikyson.godeye.monitor.modules.thread;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

public class ThreadInfoConverter {
    private static ThreadRunningProcessSorter sThreadRunningProcessSorter;

    public static void setThreadRunningProcessSorter(ThreadRunningProcessSorter threadRunningProcessSorter) {
        sThreadRunningProcessSorter = threadRunningProcessSorter;
    }

    public static Function<List<Thread>, List<ThreadInfo>> threadMap() {
        return ThreadInfoConverter::convert;
    }

    public static List<ThreadInfo> convert(List<Thread> threads) {
        List<ThreadInfo> threadWrappers = new ArrayList<>();
        for (Thread thread : threads) {
            if (thread == null) {
                continue;
            }
            threadWrappers.add(new ThreadInfo(thread, sThreadRunningProcessSorter));
        }
        return threadWrappers;
    }
}

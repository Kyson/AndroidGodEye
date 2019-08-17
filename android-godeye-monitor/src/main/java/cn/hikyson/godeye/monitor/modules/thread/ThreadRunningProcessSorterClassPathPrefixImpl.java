package cn.hikyson.godeye.monitor.modules.thread;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class ThreadRunningProcessSorterClassPathPrefixImpl implements ThreadRunningProcessSorter {
    private List<String> mClassPathPrefixs;

    public ThreadRunningProcessSorterClassPathPrefixImpl(List<String> classPathPrefixs) {
        if (classPathPrefixs == null) {
            classPathPrefixs = new ArrayList<>();
        }
        mClassPathPrefixs = classPathPrefixs;
    }

    @Override
    public ThreadRunningProcess sort(ThreadInfo threadInfo) {
        List<String> stackTraceElements = threadInfo.stackTraceElements;
        if (stackTraceElements == null || stackTraceElements.isEmpty()) {
            return ThreadRunningProcess.UNKNOWN;
        }
        for (String stacktrace : stackTraceElements) {
            if (TextUtils.isEmpty(stacktrace)) {
                continue;
            }
            for (String name : mClassPathPrefixs) {
                if (stacktrace.startsWith(name)) {
                    return ThreadRunningProcess.APP;
                }
            }
        }
        return ThreadRunningProcess.SYSTEM;
    }
}

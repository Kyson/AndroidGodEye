package cn.hikyson.godeye.monitor.modules.thread;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class ThreadRunningProcessClassifierImpl implements ThreadRunningProcessClassifier {
    private List<String> mClassPathPrefixs;

    public ThreadRunningProcessClassifierImpl(List<String> classPathPrefixs) {
        List<String> cpp = classPathPrefixs;
        if (classPathPrefixs == null) {
            cpp = new ArrayList<>();
        }
        mClassPathPrefixs = cpp;
    }

    @Override
    public ThreadRunningProcess classify(ThreadInfo threadInfo) {
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

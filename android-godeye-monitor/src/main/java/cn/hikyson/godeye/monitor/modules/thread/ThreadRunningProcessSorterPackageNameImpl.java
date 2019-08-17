package cn.hikyson.godeye.monitor.modules.thread;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;

public class ThreadRunningProcessSorterPackageNameImpl implements ThreadRunningProcessSorter {
    private @NonNull
    String mPackageName;

    public ThreadRunningProcessSorterPackageNameImpl(@NonNull String packageName) {
        mPackageName = packageName;
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
            if (stacktrace.startsWith(mPackageName)) {
                return ThreadRunningProcess.APP;
            }
        }
        return ThreadRunningProcess.SYSTEM;
    }
}

package cn.hikyson.godeye.core.internal.modules.methodcanary;

import java.util.List;

import cn.hikyson.methodcanary.lib.ThreadInfo;

public class MethodsRecordInfo {
    //nano time
    public long start;
    public long end;
    public List<MethodInfoOfThreadInfo> methodInfoOfThreadInfos;

    public MethodsRecordInfo(long start, long end, List<MethodInfoOfThreadInfo> methodInfoOfThreadInfos) {
        this.start = start;
        this.end = end;
        this.methodInfoOfThreadInfos = methodInfoOfThreadInfos;
    }

    public static class MethodInfoOfThreadInfo {
        public ThreadInfo threadInfo;
        public List<MethodInfo> methodInfos;

        public MethodInfoOfThreadInfo(ThreadInfo threadInfo, List<MethodInfo> methodInfos) {
            this.threadInfo = threadInfo;
            this.methodInfos = methodInfos;
        }

        public static class MethodInfo {
            public int stack;
            public long start;
            public long end;
            public String className;
            public int methodAccessFlag;
            public String methodName;
            public String methodDesc;
        }
    }
}

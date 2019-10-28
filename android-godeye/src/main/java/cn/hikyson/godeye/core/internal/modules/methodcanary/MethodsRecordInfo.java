package cn.hikyson.godeye.core.internal.modules.methodcanary;

import android.support.annotation.Keep;

import java.io.Serializable;
import java.util.List;

import cn.hikyson.methodcanary.lib.ThreadInfo;

@Keep
public class MethodsRecordInfo implements Serializable {
    //nano time
    public long start;
    public long end;
    public List<MethodInfoOfThreadInfo> methodInfoOfThreadInfos;

    public MethodsRecordInfo(long start, long end, List<MethodInfoOfThreadInfo> methodInfoOfThreadInfos) {
        this.start = start;
        this.end = end;
        this.methodInfoOfThreadInfos = methodInfoOfThreadInfos;
    }

    @Keep
    public static class MethodInfoOfThreadInfo implements Serializable {
        public ThreadInfo threadInfo;
        public List<MethodInfo> methodInfos;

        public MethodInfoOfThreadInfo(ThreadInfo threadInfo, List<MethodInfo> methodInfos) {
            this.threadInfo = threadInfo;
            this.methodInfos = methodInfos;
        }

        @Keep
        public static class MethodInfo implements Serializable {
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

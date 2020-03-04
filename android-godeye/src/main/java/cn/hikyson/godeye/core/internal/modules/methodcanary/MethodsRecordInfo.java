package cn.hikyson.godeye.core.internal.modules.methodcanary;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.List;

import cn.hikyson.methodcanary.lib.ThreadInfo;

@Keep
public class MethodsRecordInfo implements Serializable {
    //millis time
    public long startMillis;
    public long endMillis;
    public List<MethodInfoOfThreadInfo> methodInfoOfThreadInfos;

    public MethodsRecordInfo(long startMillis, long endMillis, List<MethodInfoOfThreadInfo> methodInfoOfThreadInfos) {
        this.startMillis = startMillis;
        this.endMillis = endMillis;
        this.methodInfoOfThreadInfos = methodInfoOfThreadInfos;
    }

    @Override
    public String toString() {
        return "MethodsRecordInfo{" +
                "startMillis=" + startMillis +
                ", endMillis=" + endMillis +
                ", methodInfoOfThreadInfos=" + methodInfoOfThreadInfos +
                '}';
    }

    @Keep
    public static class MethodInfoOfThreadInfo implements Serializable {
        public ThreadInfo threadInfo;
        public List<MethodInfo> methodInfos;

        public MethodInfoOfThreadInfo(ThreadInfo threadInfo, List<MethodInfo> methodInfos) {
            this.threadInfo = threadInfo;
            this.methodInfos = methodInfos;
        }

        @Override
        public String toString() {
            return "MethodInfoOfThreadInfo{" +
                    "threadInfo=" + threadInfo +
                    ", methodInfos=" + methodInfos +
                    '}';
        }

        @Keep
        public static class MethodInfo implements Serializable {
            public int stack;
            public long startMillis;
            public long endMillis;
            public String className;
            public int methodAccessFlag;
            public String methodName;
            public String methodDesc;

            @Override
            public String toString() {
                return "MethodInfo{" +
                        "stack=" + stack +
                        ", startMillis=" + startMillis +
                        ", endMillis=" + endMillis +
                        ", className='" + className + '\'' +
                        ", methodAccessFlag=" + methodAccessFlag +
                        ", methodName='" + methodName + '\'' +
                        ", methodDesc='" + methodDesc + '\'' +
                        '}';
            }
        }


    }
}

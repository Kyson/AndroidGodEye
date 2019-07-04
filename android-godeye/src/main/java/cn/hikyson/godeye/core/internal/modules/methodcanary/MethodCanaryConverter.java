package cn.hikyson.godeye.core.internal.modules.methodcanary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import cn.hikyson.methodcanary.lib.MethodEnterEvent;
import cn.hikyson.methodcanary.lib.MethodEvent;
import cn.hikyson.methodcanary.lib.MethodExitEvent;
import cn.hikyson.methodcanary.lib.ThreadInfo;

class MethodCanaryConverter {

    static MethodsRecordInfo convertToMethodsRecordInfo(long startTimeNanos, long stopTimeNanos, Map<ThreadInfo, List<MethodEvent>> methodEventMap) {
        MethodsRecordInfo methodsRecordInfo = new MethodsRecordInfo(startTimeNanos, stopTimeNanos, new ArrayList<MethodsRecordInfo.MethodInfoOfThreadInfo>());
        if (methodEventMap == null || methodEventMap.isEmpty()) {
            return methodsRecordInfo;
        }
        for (Map.Entry<ThreadInfo, List<MethodEvent>> entry : methodEventMap.entrySet()) {
            List<MethodEvent> methodEvents = entry.getValue();
            List<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodInfos = new ArrayList<>(methodEvents.size());
            Stack<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodEventsStackOfCurrentThread = new Stack<>();
            for (MethodEvent methodEvent : methodEvents) {
                if (methodEvent instanceof MethodEnterEvent) {
                    MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo methodInfo = new MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo();
                    methodInfo.className = methodEvent.className;
                    methodInfo.methodAccessFlag = methodEvent.methodAccessFlag;
                    methodInfo.methodName = methodEvent.methodName;
                    methodInfo.methodDesc = methodEvent.methodDesc;
                    methodInfo.start = methodEvent.eventNanoTime;
                    methodInfo.stack = methodEventsStackOfCurrentThread.size();
                    methodInfos.add(methodInfo);
                    methodEventsStackOfCurrentThread.push(methodInfo);
                } else if (methodEvent instanceof MethodExitEvent) {
                    MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo methodInfo = null;
                    try {
                        methodInfo = methodEventsStackOfCurrentThread.pop();
                    } catch (EmptyStackException ignore) {
                    }
                    if (methodInfo != null) {
                        methodInfo.end = methodEvent.eventNanoTime;
                    }
                }
            }
            methodsRecordInfo.methodInfoOfThreadInfos.add(new MethodsRecordInfo.MethodInfoOfThreadInfo(entry.getKey(), methodInfos));
        }
        return methodsRecordInfo;
    }

    static void filter(MethodsRecordInfo methodsRecordInfo, MethodCanaryContext methodCanaryContext) {
        int maxMethodCountSingleThreadByCost = methodCanaryContext.maxMethodCountSingleThreadByCost();
        Comparator<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodInfoCostComparator = methodInfoCostComparator(methodsRecordInfo.start, methodsRecordInfo.end);
        List<MethodsRecordInfo.MethodInfoOfThreadInfo> methodInfoOfThreadInfos = methodsRecordInfo.methodInfoOfThreadInfos;
        if (methodInfoOfThreadInfos != null && !methodInfoOfThreadInfos.isEmpty()) {
            for (MethodsRecordInfo.MethodInfoOfThreadInfo methodInfoOfThreadInfo : methodInfoOfThreadInfos) {
                List<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodInfos = methodInfoOfThreadInfo.methodInfos;
                if (methodInfos != null && !methodInfos.isEmpty()) {
                    filterByTopX(maxMethodCountSingleThreadByCost, methodInfoCostComparator, methodInfos);
                }
            }
        }
    }

    static void filterByTopX(int maxMethodCountSingleThreadByCost, Comparator<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodInfoCostComparator, List<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodInfos) {
        if (maxMethodCountSingleThreadByCost <= 0) {
            return;
        }
        int size = methodInfos.size();
        if (size <= maxMethodCountSingleThreadByCost) {
            return;
        }
        Collections.sort(methodInfos, methodInfoCostComparator);
        int deleteCount = size - maxMethodCountSingleThreadByCost;
        for (int i = 0; i < deleteCount; i++) {
            methodInfos.remove(methodInfos.size() - 1);
        }
    }

    static Comparator<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodInfoCostComparator(final long start, final long end) {
        return new Comparator<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo>() {
            @Override
            public int compare(MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo o1, MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo o2) {
                return longCompare(computeMethodCost(start, end, o2), computeMethodCost(start, end, o1));
            }
        };
    }

    private static int longCompare(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    private static long computeMethodCost(long start, long end, MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo methodInfo) {
        if (methodInfo == null) {
            return 0L;
        }
        long methodStart = Math.max(start, methodInfo.start);
        long methodEnd = methodInfo.end <= 0 ? end : Math.min(end, methodInfo.end);
        return Math.max(methodEnd - methodStart, 0L);
    }

}

package cn.hikyson.godeye.core.internal.modules.methodcanary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import cn.hikyson.methodcanary.lib.MethodEvent;
import cn.hikyson.methodcanary.lib.ThreadInfo;

class MethodCanaryConverter {

    static MethodsRecordInfo convertToMethodsRecordInfo(long startMillis, long stopMillis, Map<ThreadInfo, List<MethodEvent>> methodEventMap) {
        MethodsRecordInfo methodsRecordInfo = new MethodsRecordInfo(startMillis, stopMillis, new ArrayList<>());
        if (methodEventMap == null || methodEventMap.isEmpty()) {
            return methodsRecordInfo;
        }
        for (Map.Entry<ThreadInfo, List<MethodEvent>> entry : methodEventMap.entrySet()) {
            List<MethodEvent> methodEvents = entry.getValue();
            List<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodInfos = new ArrayList<>(methodEvents.size());
            Stack<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodEventsStackOfCurrentThread = new Stack<>();
            for (MethodEvent methodEvent : methodEvents) {
                if (methodEvent.isEnter) {
                    MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo methodInfo = new MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo();
                    methodInfo.className = methodEvent.className;
                    methodInfo.methodAccessFlag = methodEvent.methodAccessFlag;
                    methodInfo.methodName = methodEvent.methodName;
                    methodInfo.methodDesc = methodEvent.methodDesc;
                    methodInfo.startMillis = methodEvent.eventTimeMillis;
                    methodInfo.stack = methodEventsStackOfCurrentThread.size();
                    methodInfos.add(methodInfo);
                    methodEventsStackOfCurrentThread.push(methodInfo);
                } else {
                    MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo methodInfo = null;
                    if (!methodEventsStackOfCurrentThread.empty()) {
                        methodInfo = methodEventsStackOfCurrentThread.pop();
                    }
                    if (methodInfo != null) {
                        methodInfo.endMillis = methodEvent.eventTimeMillis;
//                        assertMethodEventPair(methodInfo, methodEvent);
                    }
                }
            }
            methodsRecordInfo.methodInfoOfThreadInfos.add(new MethodsRecordInfo.MethodInfoOfThreadInfo(entry.getKey(), methodInfos));
        }
        return methodsRecordInfo;
    }

    private static void assertMethodEventPair(MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo methodInfo, MethodEvent methodExitEvent) {
        if (!methodInfo.className.equals(methodExitEvent.className)
                || !methodInfo.className.equals(methodExitEvent.pairMethodEvent.className)
                || !methodInfo.methodName.equals(methodExitEvent.methodName)
                || !methodInfo.methodName.equals(methodExitEvent.pairMethodEvent.methodName)
                || !methodInfo.methodDesc.equals(methodExitEvent.methodDesc)
                || !methodInfo.methodDesc.equals(methodExitEvent.pairMethodEvent.methodDesc)
        ) {
            throw new IllegalStateException("Error assertMethodEventPair, methodInfo: " + methodInfo + ", methodExitEvent: " + methodExitEvent);
        }
    }

    static void filter(MethodsRecordInfo methodsRecordInfo, MethodCanaryConfig methodCanaryContext) {
        int maxMethodCountSingleThreadByCost = methodCanaryContext.maxMethodCountSingleThreadByCost();
        Comparator<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodInfoCostComparator = methodInfoCostComparator(methodsRecordInfo.startMillis, methodsRecordInfo.endMillis);
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

    private static void filterByTopX(int maxMethodCountSingleThreadByCost, Comparator<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodInfoCostComparator, List<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodInfos) {
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

    private static Comparator<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodInfoCostComparator(final long startMillis, final long endMillis) {
        return (o1, o2) -> Long.compare(computeMethodCost(startMillis, endMillis, o2), computeMethodCost(startMillis, endMillis, o1));
    }

    private static long computeMethodCost(long startMillis, long endMillis, MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo methodInfo) {
        if (methodInfo == null) {
            return 0L;
        }
        long methodStart = Math.max(startMillis, methodInfo.startMillis);
        long methodEnd = methodInfo.endMillis <= 0 ? endMillis : Math.min(endMillis, methodInfo.endMillis);
        return Math.max(methodEnd - methodStart, 0L);
    }

}

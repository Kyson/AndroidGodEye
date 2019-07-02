package cn.hikyson.godeye.core.internal.modules.methodcanary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;

import cn.hikyson.godeye.core.utils.IoUtil;
import cn.hikyson.methodcanary.lib.MethodEnterEvent;
import cn.hikyson.methodcanary.lib.MethodEvent;
import cn.hikyson.methodcanary.lib.MethodExitEvent;
import cn.hikyson.methodcanary.lib.ThreadInfo;
import cn.hikyson.methodcanary.lib.Util;

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

    static MethodsRecordInfo convertToMethodsRecordInfo(long startTimeNanos, long stopTimeNanos, File methodEventsFile) {
        MethodsRecordInfo methodsRecordInfo = new MethodsRecordInfo(startTimeNanos, stopTimeNanos, new ArrayList<MethodsRecordInfo.MethodInfoOfThreadInfo>());
        if (!methodEventsFile.exists() || methodEventsFile.isDirectory()) {
            return methodsRecordInfo;
        }
        BufferedReader reader = null;
        try {
            String line;
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(methodEventsFile)));
            MethodsRecordInfo.MethodInfoOfThreadInfo methodInfoOfCurrentThread = null;
            Stack<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodEventsStackOfCurrentThread = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(Util.START_THREAD)) {
                    Matcher m = Util.PATTERN_THREAD.matcher(line);
                    if (m.find()) {
                        long id = Long.parseLong(m.group(1));
                        String name = m.group(2);
                        int priority = Integer.parseInt(m.group(3));
                        methodInfoOfCurrentThread = new MethodsRecordInfo.MethodInfoOfThreadInfo(new ThreadInfo(id, name, priority), new ArrayList<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo>());
                        methodsRecordInfo.methodInfoOfThreadInfos.add(methodInfoOfCurrentThread);
                        methodEventsStackOfCurrentThread = new Stack<>();
                    } else {
                        throw new IllegalStateException("illegal format for [THREAD] line:" + line);
                    }
                } else if (line.startsWith(Util.START_METHOD_ENTER)) {
                    Matcher m = Util.PATTERN_METHOD_ENTER.matcher(line);
                    if (m.find()) {
                        long eventTime = Long.parseLong(m.group(1));
                        String className = m.group(2);
                        int methodAccessFlag = Integer.parseInt(m.group(3));
                        String methodName = m.group(4);
                        String methodDesc = m.group(5);
                        MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo methodInfo = new MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo();
                        methodInfo.className = className;
                        methodInfo.methodAccessFlag = methodAccessFlag;
                        methodInfo.methodName = methodName;
                        methodInfo.methodDesc = methodDesc;
                        methodInfo.start = eventTime;
                        if (methodEventsStackOfCurrentThread != null) {
                            methodInfo.stack = methodEventsStackOfCurrentThread.size();
                            methodEventsStackOfCurrentThread.push(methodInfo);
                            methodInfoOfCurrentThread.methodInfos.add(methodInfo);
                        }
                    } else {
                        throw new IllegalStateException("illegal format for PUSH line:" + line);
                    }
                } else if (line.startsWith(Util.START_METHOD_EXIT)) {
                    Matcher m = Util.PATTERN_METHOD_EXIT.matcher(line);
                    if (m.find()) {
                        long eventTime = Long.parseLong(m.group(1));
//                        String className = m.group(2);
//                        int methodAccessFlag = Integer.parseInt(m.group(3));
//                        String methodName = m.group(4);
//                        String methodDesc = m.group(5);
                        if (methodEventsStackOfCurrentThread != null) {
                            MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo methodInfo = null;
                            try {
                                methodInfo = methodEventsStackOfCurrentThread.pop();
                            } catch (EmptyStackException ignore) {
                            }
                            if (methodInfo != null) {
                                methodInfo.end = eventTime;
                            }
                        }
                    } else {
                        throw new IllegalStateException("illegal format for POP line:" + line);
                    }
                }
            }
            return methodsRecordInfo;
        } catch (IOException e) {
            e.printStackTrace();
            return methodsRecordInfo;
        } finally {
            IoUtil.closeSilently(reader);
        }
    }

    static void filter(MethodsRecordInfo methodsRecordInfo, MethodCanaryContext methodCanaryContext) {
        long lowCostMethodThresholdMillis = methodCanaryContext.lowCostMethodThresholdMillis();
        if (lowCostMethodThresholdMillis <= 0) {
            lowCostMethodThresholdMillis = Long.MAX_VALUE;
        }
        int maxMethodCountSingleThreadByCost = methodCanaryContext.maxMethodCountSingleThreadByCost();
        Comparator<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodInfoCostComparator = methodInfoCostComparator(methodsRecordInfo.start, methodsRecordInfo.end);
        List<MethodsRecordInfo.MethodInfoOfThreadInfo> methodInfoOfThreadInfos = methodsRecordInfo.methodInfoOfThreadInfos;
        if (methodInfoOfThreadInfos != null && !methodInfoOfThreadInfos.isEmpty()) {
            for (MethodsRecordInfo.MethodInfoOfThreadInfo methodInfoOfThreadInfo : methodInfoOfThreadInfos) {
                List<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodInfos = methodInfoOfThreadInfo.methodInfos;
                if (methodInfos != null && !methodInfos.isEmpty()) {
                    filterByLowCostMethodThreshold(methodsRecordInfo.start, methodsRecordInfo.end, lowCostMethodThresholdMillis, methodInfos);
                    filterByTopX(maxMethodCountSingleThreadByCost, methodInfoCostComparator, methodInfos);
                }
            }
        }
    }

    static void filterByLowCostMethodThreshold(final long start, final long end, final long lowCostMethodThresholdMillis, List<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> methodInfos) {
        List<MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo> delete = new ArrayList<>();
        for (MethodsRecordInfo.MethodInfoOfThreadInfo.MethodInfo methodInfo : methodInfos) {
            long cost = computeMethodCost(start, end, methodInfo);
            if ((cost / 1000000.0) <= lowCostMethodThresholdMillis) {
                delete.add(methodInfo);
            }
        }
        methodInfos.removeAll(delete);
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

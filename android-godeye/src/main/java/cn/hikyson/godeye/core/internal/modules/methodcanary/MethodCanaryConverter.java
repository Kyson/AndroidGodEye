package cn.hikyson.godeye.core.internal.modules.methodcanary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.regex.Matcher;

import cn.hikyson.godeye.core.utils.IoUtil;
import cn.hikyson.methodcanary.lib.ThreadInfo;
import cn.hikyson.methodcanary.lib.Util;

class MethodCanaryConverter {

    static MethodsRecordInfo filterMethodsRecordInfo(MethodsRecordInfo methodsRecordInfo, MethodCanaryContext methodCanaryContext) {
        for () {//TODO KYSON IMPL

        }
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
                        String className = m.group(2);
                        int methodAccessFlag = Integer.parseInt(m.group(3));
                        String methodName = m.group(4);
                        String methodDesc = m.group(5);
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


}

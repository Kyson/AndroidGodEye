package cn.hikyson.godeye.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class StacktraceUtil {
    public static List<String> stackTraceToStringArray(List<StackTraceElement> stackTraceElements) {
        List<String> stackList = new ArrayList<>();
        for (StackTraceElement traceElement : stackTraceElements) {
            stackList.add(String.valueOf(traceElement));
        }
        return stackList;
    }

    public static List<String> getStackTraceOfThread(Thread thread) {
        StackTraceElement[] stackTraceElements = thread.getStackTrace();
        return stackTraceToStringArray(Arrays.asList(stackTraceElements));
    }

    public static String getStringOfThrowable(Throwable throwable) {
        return throwable.getLocalizedMessage() + "\n" + stackTraceToString(throwable.getStackTrace());
    }

    private static String stackTraceToString(StackTraceElement... stackTraceElements) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement traceElement : stackTraceElements) {
            sb.append(String.valueOf(traceElement)).append("\n");
        }
        return sb.toString();
    }
}

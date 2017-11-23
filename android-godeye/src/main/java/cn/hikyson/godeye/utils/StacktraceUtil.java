package cn.hikyson.godeye.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class StacktraceUtil {
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US);

    /**
     * 原始的堆栈信息转换为字符串类型的堆栈信息
     *
     * @param ts
     * @return
     */
    public static Map<String, List<String>> convertToStackString(Map<Long, List<StackTraceElement>> ts) {
        // 筛选之后的堆栈
        Map<Long, List<StackTraceElement>> filterMap = new LinkedHashMap<>();
        for (Long key : ts.keySet()) {
            List<StackTraceElement> value = ts.get(key);
            if (!filterMap.containsValue(value)) {// 筛选一下是否存在堆栈信息相同的
                filterMap.put(key, value);
            }
        }
        // 转换为字符串
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (Map.Entry<Long, List<StackTraceElement>> entry : filterMap.entrySet()) {
            result.put(TIME_FORMATTER.format(entry.getKey()), getStack(entry.getValue()));
        }
        return result;
    }

    private static List<String> getStack(List<StackTraceElement> stackTraceElements) {
        List<String> stackList = new ArrayList<>();
        for (StackTraceElement traceElement : stackTraceElements) {
            stackList.add(String.valueOf(traceElement));
        }
        return stackList;
    }
}

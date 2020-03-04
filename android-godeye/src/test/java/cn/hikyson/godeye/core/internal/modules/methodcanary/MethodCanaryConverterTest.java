package cn.hikyson.godeye.core.internal.modules.methodcanary;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hikyson.methodcanary.lib.MethodEvent;
import cn.hikyson.methodcanary.lib.ThreadInfo;

import static org.junit.Assert.assertEquals;

public class MethodCanaryConverterTest {

    @Test
    public void convertToMethodsRecordInfo() {
        Map<ThreadInfo, List<MethodEvent>> methodEventMap = mockMethodEventMap();
        MethodsRecordInfo methodsRecordInfo = mockMethodsRecordInfo(methodEventMap);
        Gson gson = new Gson();
        String c = gson.toJson(methodsRecordInfo);
        System.out.println("methodsRecordInfo:\n" + c);
        assertEquals(methodsRecordInfo.methodInfoOfThreadInfos.size(), methodEventMap.size());
    }

    static Map<ThreadInfo, List<MethodEvent>> mockMethodEventMap() {
        Map<ThreadInfo, List<MethodEvent>> methodEventMap = new HashMap<>();
        methodEventMap.put(new ThreadInfo(1, "main", 5), new ArrayList<MethodEvent>());
        methodEventMap.put(new ThreadInfo(2, "thread2", 6), new ArrayList<MethodEvent>());
        methodEventMap.put(new ThreadInfo(3, "thread3", 8), new ArrayList<MethodEvent>());
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                methodEventMap.get(new ThreadInfo(1, "main", 5)).add(new MethodEvent("class" + i, i, "method" + i, "methoddesc" + i, true, System.currentTimeMillis(), 0));
                methodEventMap.get(new ThreadInfo(1, "main", 5)).add(new MethodEvent("class" + i + "x", i, "method" + i + "x", "methoddesc" + i + "x", true, System.currentTimeMillis(), 0));
            } else {
                methodEventMap.get(new ThreadInfo(1, "main", 5)).add(new MethodEvent("class" + i, i, "method" + i, "methoddesc" + i, false, System.currentTimeMillis(), 0));
                methodEventMap.get(new ThreadInfo(1, "main", 5)).add(new MethodEvent("class" + i + "x", i, "method" + i + "x", "methoddesc" + i + "x", false, System.currentTimeMillis(), 0));
            }
        }
        for (int i = 15; i < 20; i++) {
            if (i % 2 == 0) {
                methodEventMap.get(new ThreadInfo(2, "thread2", 6)).add(new MethodEvent("class" + i, i, "method" + i, "methoddesc" + i, false, System.currentTimeMillis(), 0));
            } else {
                methodEventMap.get(new ThreadInfo(2, "thread2", 6)).add(new MethodEvent("class" + i, i, "method" + i, "methoddesc" + i, false, System.currentTimeMillis(), 0));
            }
        }
        methodEventMap.get(new ThreadInfo(3, "thread3", 8)).add(new MethodEvent("class" + 99, 99, "method" + 99, "methoddesc" + 99, true, System.currentTimeMillis(), 0));
        for (int i = 100; i < 103; i++) {
            methodEventMap.get(new ThreadInfo(3, "thread3", 8)).add(new MethodEvent("class" + i, i, "method" + i, "methoddesc" + i, false, System.currentTimeMillis(), 0));
        }
        methodEventMap.get(new ThreadInfo(3, "thread3", 8)).add(new MethodEvent("class" + 103, 103, "method" + 103, "methoddesc" + 103, true, System.currentTimeMillis(), 0));
        methodEventMap.get(new ThreadInfo(3, "thread3", 8)).add(new MethodEvent("class" + 104, 104, "method" + 104, "methoddesc" + 104, false, System.currentTimeMillis(), 0));
        return methodEventMap;
    }

    static MethodsRecordInfo mockMethodsRecordInfo(Map<ThreadInfo, List<MethodEvent>> methodEventMap) {
        long start = System.currentTimeMillis();
        return MethodCanaryConverter.convertToMethodsRecordInfo(start, System.currentTimeMillis(), methodEventMap);
    }

}
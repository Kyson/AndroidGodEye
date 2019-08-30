package cn.hikyson.godeye.core.internal.modules.methodcanary;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hikyson.methodcanary.lib.MethodEnterEvent;
import cn.hikyson.methodcanary.lib.MethodEvent;
import cn.hikyson.methodcanary.lib.MethodExitEvent;
import cn.hikyson.methodcanary.lib.ThreadInfo;

import static org.junit.Assert.*;

public class MethodCanaryConverterTest {

    @Test
    public void convertToMethodsRecordInfo() {
        long start = System.nanoTime();
        Map<ThreadInfo, List<MethodEvent>> methodEventMap = new HashMap<>();
        methodEventMap.put(new ThreadInfo(1, "main", 5), new ArrayList<MethodEvent>());
        methodEventMap.put(new ThreadInfo(2, "thread2", 6), new ArrayList<MethodEvent>());
        methodEventMap.put(new ThreadInfo(3, "thread3", 8), new ArrayList<MethodEvent>());
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                methodEventMap.get(new ThreadInfo(1, "main", 5)).add(new MethodEnterEvent("class" + i, i, "method" + i, "methoddesc" + i, System.nanoTime()));
                methodEventMap.get(new ThreadInfo(1, "main", 5)).add(new MethodEnterEvent("class" + i + "x", i, "method" + i + "x", "methoddesc" + i + "x", System.nanoTime()));
            } else {
                methodEventMap.get(new ThreadInfo(1, "main", 5)).add(new MethodExitEvent("class" + i, i, "method" + i, "methoddesc" + i, System.nanoTime()));
                methodEventMap.get(new ThreadInfo(1, "main", 5)).add(new MethodExitEvent("class" + i + "x", i, "method" + i + "x", "methoddesc" + i + "x", System.nanoTime()));
            }
        }
        for (int i = 15; i < 20; i++) {
            if (i % 2 == 0) {
                methodEventMap.get(new ThreadInfo(2, "thread2", 6)).add(new MethodExitEvent("class" + i, i, "method" + i, "methoddesc" + i, System.nanoTime()));
            } else {
                methodEventMap.get(new ThreadInfo(2, "thread2", 6)).add(new MethodEnterEvent("class" + i, i, "method" + i, "methoddesc" + i, System.nanoTime()));
            }
        }
        methodEventMap.get(new ThreadInfo(3, "thread3", 8)).add(new MethodEnterEvent("class" + 99, 99, "method" + 99, "methoddesc" + 99, System.nanoTime()));
        for (int i = 100; i < 103; i++) {
            methodEventMap.get(new ThreadInfo(3, "thread3", 8)).add(new MethodExitEvent("class" + i, i, "method" + i, "methoddesc" + i, System.nanoTime()));
        }
        methodEventMap.get(new ThreadInfo(3, "thread3", 8)).add(new MethodEnterEvent("class" + 103, 103, "method" + 103, "methoddesc" + 103, System.nanoTime()));
        methodEventMap.get(new ThreadInfo(3, "thread3", 8)).add(new MethodExitEvent("class" + 104, 104, "method" + 104, "methoddesc" + 104, System.nanoTime()));

        MethodsRecordInfo methodsRecordInfo = MethodCanaryConverter.convertToMethodsRecordInfo(start, System.nanoTime(), methodEventMap);
        Gson gson = new Gson();
        String c = gson.toJson(methodsRecordInfo);
        System.out.println("methodsRecordInfo:\n" + c);
        assertEquals(methodsRecordInfo.methodInfoOfThreadInfos.size(), methodEventMap.size());
    }

}
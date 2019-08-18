package cn.hikyson.godeye.core.internal.modules.methodcanary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.google.gson.Gson;

import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hikyson.methodcanary.lib.MethodEnterEvent;
import cn.hikyson.methodcanary.lib.MethodEvent;
import cn.hikyson.methodcanary.lib.MethodExitEvent;
import cn.hikyson.methodcanary.lib.ThreadInfo;

public class MethodCanaryConverterTest {
    @Test
    public void methodInfoCostComparator() {
    }

    @Test
    public void computeMethodCost() {
    }

    private static File createMethodCanaryRecordFile() {
        final Context context = InstrumentationRegistry.getTargetContext();
        File file = new File(context.getCacheDir(), "methodcanary.txt");
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Throwable ignore) {
            }
        }
        String content = "[THREAD]id=2;name=main;priority=5\n" +
                "PUSH:et=3284436162929793;cn=cn/hikyson/methodcanary/sample/MainActivity;ma=1;mn=<init>;md=()V\n" +
                "POP:et=3284436163062866;cn=cn/hikyson/methodcanary/sample/MainActivity;ma=1;mn=<init>;md=()V\n" +
                "PUSH:et=3284436172374273;cn=cn/hikyson/methodcanary/sample/MainActivity;ma=4;mn=onCreate;md=(Landroid/os/Bundle;)V\n" +
                "PUSH:et=3284436240967658;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=8;mn=<clinit>;md=()V\n" +
                "PUSH:et=3284436241070366;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=0;mn=<init>;md=()V\n" +
                "POP:et=3284436241089950;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=0;mn=<init>;md=()V\n" +
                "[THREAD]id=147350;name=Thread-19;priority=5\n" +
                "PUSH:et=3284440487128854;cn=cn/hikyson/methodcanary/sample/SampleAppClassA$1;ma=1;mn=run;md=()V\n" +
                "[THREAD]id=147351;name=Thread-20;priority=5\n" +
                "PUSH:et=3284440700503229;cn=cn/hikyson/methodcanary/sample/SampleAppClassA$1;ma=1;mn=run;md=()V\n";
        writeFileFromBytesByStream(file, content.getBytes(Charset.forName("utf-8")), false);
        return file;
    }


    private static boolean writeFileFromBytesByStream(final File file,
                                                      final byte[] bytes,
                                                      final boolean append) {
        if (bytes == null) return false;
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file, append));
            bos.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void convertToMethodsRecordInfo1() {
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
    }
}
package cn.hikyson.godeye.core.internal.modules.methodcanary;

import android.app.Application;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.google.gson.Gson;

import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import cn.hikyson.godeye.core.utils.FileUtil;
import cn.hikyson.godeye.core.utils.IoUtil;
import cn.hikyson.methodcanary.lib.Util;

import static org.junit.Assert.*;

public class MethodCanaryConverterTest {

    @Test
    public void convertToMethodsRecordInfo() throws IOException {
        Context context = InstrumentationRegistry.getTargetContext();
        File file = new File(context.getCacheDir(), "methodcanary.txt");
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        String content = "[THREAD]id=2;name=main;priority=5\n" +
                "PUSH:et=3284436162929793;cn=cn/hikyson/methodcanary/sample/MainActivity;ma=1;mn=<init>;md=()V\n" +
                "POP:et=3284436163062866;cn=cn/hikyson/methodcanary/sample/MainActivity;ma=1;mn=<init>;md=()V\n" +
                "PUSH:et=3284436172374273;cn=cn/hikyson/methodcanary/sample/MainActivity;ma=4;mn=onCreate;md=(Landroid/os/Bundle;)V\n" +
                "PUSH:et=3284436240967658;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=8;mn=<clinit>;md=()V\n" +
                "PUSH:et=3284436241070366;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=0;mn=<init>;md=()V\n" +
                "POP:et=3284436241089950;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=0;mn=<init>;md=()V\n" +
                "POP:et=3284436241108856;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=8;mn=<clinit>;md=()V\n" +
                "PUSH:et=3284436241357502;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$2;ma=0;mn=<init>;md=(Lcn/hikyson/methodcanary/sample/MainActivity;)V\n" +
                "POP:et=3284436241399793;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$2;ma=0;mn=<init>;md=(Lcn/hikyson/methodcanary/sample/MainActivity;)V\n" +
                "POP:et=3284436241422762;cn=cn/hikyson/methodcanary/sample/MainActivity;ma=4;mn=onCreate;md=(Landroid/os/Bundle;)V\n" +
                "PUSH:et=3284436992559585;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=17;mn=onClick;md=(Landroid/view/View;)V\n" +
                "PUSH:et=3284436992732710;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1$1;ma=8;mn=<clinit>;md=()V\n" +
                "PUSH:et=3284436992770314;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1$1;ma=0;mn=<init>;md=()V\n" +
                "POP:et=3284436992789689;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1$1;ma=0;mn=<init>;md=()V\n" +
                "POP:et=3284436992808179;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1$1;ma=8;mn=<clinit>;md=()V\n" +
                "PUSH:et=3284436993420835;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1$2;ma=8;mn=<clinit>;md=()V\n" +
                "PUSH:et=3284436993550054;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1$2;ma=0;mn=<init>;md=()V\n" +
                "POP:et=3284436993572762;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1$2;ma=0;mn=<init>;md=()V\n" +
                "POP:et=3284436993592137;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1$2;ma=8;mn=<clinit>;md=()V\n" +
                "POP:et=3284436993884481;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=17;mn=onClick;md=(Landroid/view/View;)V\n" +
                "PUSH:et=3284437258055731;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=17;mn=onClick;md=(Landroid/view/View;)V\n" +
                "POP:et=3284437258713960;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=17;mn=onClick;md=(Landroid/view/View;)V\n" +
                "PUSH:et=3284437467832397;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=17;mn=onClick;md=(Landroid/view/View;)V\n" +
                "POP:et=3284437468582605;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=17;mn=onClick;md=(Landroid/view/View;)V\n" +
                "PUSH:et=3284437673277605;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=17;mn=onClick;md=(Landroid/view/View;)V\n" +
                "POP:et=3284437674414637;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=17;mn=onClick;md=(Landroid/view/View;)V\n" +
                "PUSH:et=3284437880583907;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=17;mn=onClick;md=(Landroid/view/View;)V\n" +
                "POP:et=3284437881484532;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1;ma=17;mn=onClick;md=(Landroid/view/View;)V\n" +
                "[THREAD]id=147350;name=Thread-19;priority=5\n" +
                "PUSH:et=3284440487128854;cn=cn/hikyson/methodcanary/sample/SampleAppClassA$1;ma=1;mn=run;md=()V\n" +
                "[THREAD]id=147351;name=Thread-20;priority=5\n" +
                "PUSH:et=3284440700503229;cn=cn/hikyson/methodcanary/sample/SampleAppClassA$1;ma=1;mn=run;md=()V\n" +
                "[THREAD]id=147338;name=Thread-7;priority=5\n" +
                "PUSH:et=3284436993519637;cn=cn/hikyson/methodcanary/sample/MainActivity$onCreate$1$1;ma=17;mn=run;md=()V\n" +
                "PUSH:et=3284436993600470;cn=cn/hikyson/methodcanary/sample/SampleAppClassA;ma=9;mn=testMethod1;md=()V\n" +
                "POP:et=3284439996130469;cn=cn/hikyson/methodcanary/sample/SampleAppClassA;ma=9;mn=testMethod1;md=()V\n" +
                "PUSH:et=3284439996935573;cn=cn/hikyson/methodcanary/sample/SampleAppClassA;ma=9;mn=testMethod2;md=()V\n" +
                "POP:et=3284440028103334;cn=cn/hikyson/methodcanary/sample/SampleAppClassA;ma=9;mn=testMethod2;md=()V\n" +
                "PUSH:et=3284440028387240;cn=cn/hikyson/methodcanary/sample/SampleAppClassA;ma=9;mn=testMethod3;md=()V\n" +
                "PUSH:et=3284440028766979;cn=cn/hikyson/methodcanary/sample/SampleAppClassA$1;ma=0;mn=<init>;md=()V\n" +
                "POP:et=3284440028837136;cn=cn/hikyson/methodcanary/sample/SampleAppClassA$1;ma=0;mn=<init>;md=()V\n" +
                "POP:et=3284440030084532;cn=cn/hikyson/methodcanary/sample/SampleAppClassA;ma=9;mn=testMethod3;md=()V\n" +
                "PUSH:et=3284440030165729;cn=cn/hikyson/methodcanary/sample/SampleAppClassA;ma=9;mn=testMethod1;md=()V\n";
        writeFileFromBytesByStream(file, content.getBytes(Charset.forName("utf-8")), false);
        MethodsRecordInfo methodsRecordInfo = MethodCanaryConverter.convertToMethodsRecordInfo(3284436152929793L, 3284436172929793L, file);
        Gson gson = new Gson();
        String c = gson.toJson(methodsRecordInfo);
        System.out.println("methodsRecordInfo:\n" + c);
    }

    @Test
    public void filter() throws IOException {
        final Context context = InstrumentationRegistry.getTargetContext();
        File file = new File(context.getCacheDir(), "methodcanary.txt");
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
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
        MethodsRecordInfo methodsRecordInfo = MethodCanaryConverter.convertToMethodsRecordInfo(3284436152929793L, 3284440700503230L, file);
        Gson gson = new Gson();
        String c = gson.toJson(methodsRecordInfo);
        System.out.println("methodsRecordInfo0:\n" + c);

        MethodCanaryConverter.filter(methodsRecordInfo, new MethodCanaryContext() {

            @Override
            public long lowCostMethodThresholdMillis() {
                return 500;
            }

            @Override
            public int maxMethodCountSingleThreadByCost() {
                return 0;
            }

            @Override
            public Application app() {
                return (Application) context.getApplicationContext();
            }
        });
        c = gson.toJson(methodsRecordInfo);
        System.out.println("methodsRecordInfo1:\n" + c);

    }


    static boolean writeFileFromBytesByStream(final File file,
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

}
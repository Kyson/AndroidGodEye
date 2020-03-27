package cn.hikyson.godeye.sample;

import android.app.Application;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by kysonchao on 2017/11/30.
 */
public class SampleApp extends Application {
    private static final SimpleDateFormat FORMATTER_2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

    @Override
    public void onCreate() {
        super.onCreate();
        StartupTracer.get().onApplicationCreate();
        SubProcessIntentService.startActionFoo(this);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    CrashInfo crashInfo = new CrashInfo();
//                    crashInfo.crashTime = FORMATTER_2.format(new Date());
//                    GodEye.instance().<Crash>getModule(GodEye.ModuleName.CRASH).produce(Arrays.asList(crashInfo));
//                } catch (UninstallException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }
}

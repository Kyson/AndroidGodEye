package cn.hikyson.godeye.sample;

import android.app.Application;

/**
 * Created by kysonchao on 2017/11/30.
 */
public class SampleApp extends Application {

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
//                    GodEye.instance().<Crash>getModule(GodEye.ModuleName.CRASH).produce(Arrays.asList(new CrashInfo()));
//                } catch (UninstallException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }
}

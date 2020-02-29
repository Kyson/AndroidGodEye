package cn.hikyson.godeye.sample;

import android.app.Application;

import cn.hikyson.godeye.core.GodEye;

/**
 * Created by kysonchao on 2017/11/30.
 */
public class SampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StartupTracer.get().onApplicationCreate();
        //UPDATE 1.8+ 需要初始化
        new Thread(new Runnable() {
            @Override
            public void run() {
                GodEye.instance().init(SampleApp.this);
            }
        }).start();
    }
}

package cn.hikyson.godeye.sample;

import android.app.Application;

/**
 * Created by kysonchao on 2017/11/30.
 */
public class SampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        GodEye.instance().init(SampleApp.this);
        StartupTracer.get().onApplicationCreate();
    }
}

package cn.hikyson.android.godeye.sample;

import android.app.Application;

import cn.hikyson.android.godeye.toolbox.StartupTracer;

/**
 * Created by kysonchao on 2017/11/30.
 */
public class SampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StartupTracer.get().onApplicationCreate();
    }
}

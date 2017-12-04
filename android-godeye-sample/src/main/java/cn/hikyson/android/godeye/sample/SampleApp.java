package cn.hikyson.android.godeye.sample;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

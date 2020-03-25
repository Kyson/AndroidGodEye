package cn.hikyson.godeye.sample;

import android.app.Application;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.monitor.GodEyeMonitor;

/**
 * Created by kysonchao on 2017/11/30.
 */
public class SampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        GodEye.instance().init(SampleApp.this);
        StartupTracer.get().onApplicationCreate();
        GodEye.instance().install(GodEyeConfig.fromAssets("android-godeye-config/install.config"));
        GodEyeMonitor.work(this);
        SubProcessIntentService.startActionFoo(this);
    }
}

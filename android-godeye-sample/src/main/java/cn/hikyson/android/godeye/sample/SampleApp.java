package cn.hikyson.android.godeye.sample;

import android.app.Application;

import cn.hikyson.android.godeye.toolbox.StartupTracer;
import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.monitor.GodEyeMonitor;

/**
 * Created by kysonchao on 2017/11/30.
 */
public class SampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StartupTracer.get().onApplicationCreate();
        //UPDATE 1.8+ 需要初始化
        GodEye.instance().init(this);
        GodEyeMonitor.injectAppInfoConext(new AppInfoProxyImpl(this));
    }
}

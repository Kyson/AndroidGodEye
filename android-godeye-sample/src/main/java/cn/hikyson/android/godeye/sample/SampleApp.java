package cn.hikyson.android.godeye.sample;

import android.app.Application;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.hikyson.android.godeye.toolbox.StartupTracer;
import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.monitor.GodEyeMonitor;
import cn.hikyson.methodcanary.lib.MethodCanaryConfig;
import cn.hikyson.methodcanary.lib.MethodCanaryInject;
import cn.hikyson.methodcanary.lib.MethodCanaryOutputCallback;
import cn.hikyson.methodcanary.lib.MethodEvent;
import cn.hikyson.methodcanary.lib.ThreadInfo;

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
        MethodCanaryInject.init(MethodCanaryConfig.MethodCanaryConfigBuilder.aMethodCanaryConfig()
                .app(this).methodEventThreshold(1000).methodCanaryOutputCallback(new MethodCanaryOutputCallback() {
                    @Override
                    public void output(Map<ThreadInfo, List<MethodEvent>> methodEventMap, File record) {

                    }
                }).build());
    }
}

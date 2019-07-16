package cn.hikyson.android.godeye.sample;

import android.app.Application;
import android.util.Log;

import cn.hikyson.android.godeye.toolbox.StartupTracer;
import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.pageload.PageLifecycleEventInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.Pageload;
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadContext;
import cn.hikyson.godeye.monitor.GodEyeMonitor;
import io.reactivex.functions.Consumer;

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
        Pageload pageload2 = new Pageload();
        pageload2.install(new PageloadContext() {
            @Override
            public Application application() {
                return SampleApp.this;
            }
        });
        pageload2.subject()
                .subscribe(new Consumer<PageLifecycleEventInfo>() {
                    @Override
                    public void accept(PageLifecycleEventInfo pageLifecycleEventInfo) throws Exception {
                        Log.d("kyson", "Thread.currentThread().getName():" + Thread.currentThread().getName());
                        Log.d("kyson", "[subject]" + String.valueOf(pageLifecycleEventInfo));
                    }
                });
    }
}

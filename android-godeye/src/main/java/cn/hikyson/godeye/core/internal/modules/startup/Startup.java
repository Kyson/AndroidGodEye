package cn.hikyson.godeye.core.internal.modules.startup;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * 启动模块
 * 发射数据线程未知
 * Created by kysonchao on 2017/11/23.
 */
public class Startup extends ProduceableSubject<StartupInfo> implements Install<StartupContext> {

    private StartupContext mConfig;

    @Override
    public synchronized void install(StartupContext config) {
        if (config == null) {
            throw new IllegalArgumentException("Startup module install fail because config is null.");
        }
        if (mConfig != null) {
            L.d("Startup already installed, ignore.");
            return;
        }
        mConfig = config;
        L.d("Startup installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (mConfig == null) {
            L.d("Startup already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        L.d("Startup uninstall.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mConfig != null;
    }

    @Override
    public StartupContext config() {
        return mConfig;
    }

    @Override
    public void produce(StartupInfo data) {
        if (mConfig == null) {
            L.d("Startup is not installed, produce data fail.");
            return;
        }
        super.produce(data);
    }

    @Override
    protected Subject<StartupInfo> createSubject() {
        return BehaviorSubject.create();
    }
}

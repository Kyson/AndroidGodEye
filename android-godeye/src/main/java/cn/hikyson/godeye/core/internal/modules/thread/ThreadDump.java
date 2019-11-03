package cn.hikyson.godeye.core.internal.modules.thread;

import java.util.List;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * 线程模块
 * 安装卸载可以任意线程
 * 发射数据在子线程
 * Created by kysonchao on 2018/1/14.
 */
public class ThreadDump extends ProduceableSubject<List<Thread>> implements Install<ThreadContext> {
    private ThreadEngine mThreadEngine;
    private ThreadContext mConfig;

    @Override
    public synchronized void install(ThreadContext config) {
        if (mThreadEngine != null) {
            L.d("ThreadDump already installed, ignore.");
            return;
        }
        mConfig = config;
        mThreadEngine = new ThreadEngine(this, config.intervalMillis(), config.threadFilter());
        mThreadEngine.work();
        L.d("ThreadDump installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (mThreadEngine == null) {
            L.d("ThreadDump already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        mThreadEngine.shutdown();
        mThreadEngine = null;
        L.d("ThreadDump uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mThreadEngine != null;
    }

    @Override
    public ThreadContext config() {
        return mConfig;
    }
}

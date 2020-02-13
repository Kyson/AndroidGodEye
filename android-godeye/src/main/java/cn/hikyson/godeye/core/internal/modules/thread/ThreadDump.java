package cn.hikyson.godeye.core.internal.modules.thread;

import java.util.List;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * 线程模块
 * 安装卸载可以任意线程
 * 发射数据在子线程
 * Created by kysonchao on 2018/1/14.
 */
public class ThreadDump extends ProduceableSubject<List<ThreadInfo>> implements Install<ThreadConfig> {
    private ThreadEngine mThreadEngine;
    private ThreadConfig mConfig;

    @Override
    public synchronized boolean install(ThreadConfig config) {
        if (mThreadEngine != null) {
            L.d("ThreadDump already installed, ignore.");
            return true;
        }
        mConfig = config;
        mThreadEngine = new ThreadEngine(this, config());
        mThreadEngine.work();
        L.d("ThreadDump installed.");
        return true;
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
    public ThreadConfig config() {
        return mConfig;
    }

    @Override
    protected Subject<List<ThreadInfo>> createSubject() {
        return BehaviorSubject.create();
    }
}

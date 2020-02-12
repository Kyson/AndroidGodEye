package cn.hikyson.godeye.core.internal.modules.fps;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;

/**
 * fps模块
 * 可能会影响ui性能
 * 安装卸载可以任意线程（会切换到主线程）
 * 发射数据在子线程
 * Created by kysonchao on 2017/11/22.
 */
public class Fps extends ProduceableSubject<FpsInfo> implements Install<FpsConfig> {
    private FpsEngine mFpsEngine;
    private FpsConfig mConfig;

    @Override
    public boolean install(final FpsConfig config) {
        if (ThreadUtil.isMainThread()) {
            installInMain(config);
        } else {
            ThreadUtil.sMain.execute(new Runnable() {
                @Override
                public void run() {
                    installInMain(config);
                }
            });
        }
        return true;
    }

    private synchronized void installInMain(FpsConfig config) {
        if (mFpsEngine != null) {
            L.d("Fps already installed, ignore.");
            return;
        }
        mConfig = config;
        mFpsEngine = new FpsEngine(GodEye.instance().getApplication(), this, config.intervalMillis());
        mFpsEngine.work();
        L.d("Fps installed.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mFpsEngine != null;
    }

    @Override
    public FpsConfig config() {
        return mConfig;
    }

    @Override
    public void uninstall() {
        if (ThreadUtil.isMainThread()) {
            uninstallInMain();
        } else {
            ThreadUtil.sMain.execute(new Runnable() {
                @Override
                public void run() {
                    uninstallInMain();
                }
            });
        }
    }

    private synchronized void uninstallInMain() {
        if (mFpsEngine == null) {
            L.d("Fps already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        mFpsEngine.shutdown();
        mFpsEngine = null;
        L.d("Fps uninstalled.");
    }
}

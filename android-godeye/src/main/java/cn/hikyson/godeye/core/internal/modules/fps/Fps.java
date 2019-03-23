package cn.hikyson.godeye.core.internal.modules.fps;

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
public class Fps extends ProduceableSubject<FpsInfo> implements Install<FpsContext> {
    private FpsEngine mFpsEngine;

    @Override
    public void install(final FpsContext config) {
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
    }

    private void installInMain(FpsContext config) {
        if (mFpsEngine != null) {
            L.d("fps already installed, ignore.");
            return;
        }
        mFpsEngine = new FpsEngine(config.context(), this, config.intervalMillis());
        mFpsEngine.work();
        L.d("fps installed.");
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

    private void uninstallInMain() {
        if (mFpsEngine == null) {
            L.d("fps already uninstalled, ignore.");
            return;
        }
        mFpsEngine.shutdown();
        mFpsEngine = null;
        L.d("fps uninstalled.");
    }
}

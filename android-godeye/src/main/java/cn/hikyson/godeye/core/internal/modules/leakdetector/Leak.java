package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Application;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ReflectUtil;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class Leak extends ProduceableSubject<LeakInfo> implements Install<LeakConfig> {
    private boolean mInstalled;
    private LeakConfig mConfig;

    @Override
    public synchronized boolean install(LeakConfig config) {
        if (mInstalled) {
            L.d("Leak already installed, ignore.");
            return true;
        }
        mConfig = config;
        try {
            ReflectUtil.invokeStaticMethodUnSafe("cn.hikyson.android.godeye.leakcanary.GodEyePluginLeakCanary", "install",
                    new Class<?>[]{Application.class, Leak.class}, new Object[]{GodEye.instance().getApplication(), this});
        } catch (Exception e) {
            L.d("Leak can not be installed, please add android-godeye-leakcanary dependency first:", e);
            return false;
        }
        mInstalled = true;
        L.d("Leak installed.");
        return true;
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("Leak already uninstalled, ignore.");
            return;
        }
        try {
            ReflectUtil.invokeStaticMethodUnSafe("cn.hikyson.android.godeye.leakcanary.GodEyePluginLeakCanary", "uninstall",
                    new Class<?>[]{}, new Object[]{});
        } catch (Exception e) {
            L.d("Leak can not be uninstalled, please add android-godeye-leakcanary dependency first:", e);
            return;
        }
        mConfig = null;
        mInstalled = false;
        L.d("Leak uninstalled.");
    }

    @Override
    public boolean isInstalled() {
        return this.mInstalled;
    }

    @Override
    public LeakConfig config() {
        return mConfig;
    }

    @Override
    protected Subject<LeakInfo> createSubject() {
        return ReplaySubject.create();
    }
}

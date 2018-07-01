package cn.hikyson.android.godeye.toolbox;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.startup.Startup;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;

/**
 * @deprecated 没啥用，删了
 * Created by kysonchao on 2017/12/2.
 */
public class StartupTracer {
    public interface OnStartupEndCallback {
        public void onStartupEnd();
    }

    private long mApplicationStartTime;
    private long mSplashStartTime;

    private StartupTracer() {
    }

    private static class InstanceHolder {
        private static final StartupTracer sInstance = new StartupTracer();
    }

    public static StartupTracer get() {
        return InstanceHolder.sInstance;
    }

    public void onApplicationCreate() {
        mApplicationStartTime = System.currentTimeMillis();
    }

    public void onSplashCreate() {
        mSplashStartTime = System.currentTimeMillis();
    }

    public void onHomeCreate(Activity activity) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("call onHomeCreate ui thread!");
        }
        activity.getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Startup startup = GodEye.instance().getModule(GodEye.ModuleName.STARTUP);
                        startup.produce(generateStartupInfo(System.currentTimeMillis()));
                    }
                });
            }
        });
    }

    private StartupInfo generateStartupInfo(long homeEndTime) {
        if (isCodeStart(homeEndTime)) {
            return new StartupInfo(StartupInfo.StartUpType.COLD, homeEndTime - mApplicationStartTime);
        } else if (isHotStart(homeEndTime)) {
            return new StartupInfo(StartupInfo.StartUpType.HOT, homeEndTime - mSplashStartTime);
        } else {
            return null;
        }
    }

    private boolean isCodeStart(long homeEndTime) {
        return mApplicationStartTime > 0 && homeEndTime > mSplashStartTime && mSplashStartTime > mApplicationStartTime;
    }

    private boolean isHotStart(long homeEndTime) {
        return mApplicationStartTime <= 0 && mSplashStartTime > 0 && homeEndTime > mSplashStartTime;
    }
}

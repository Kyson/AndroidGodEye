package cn.hikyson.android.godeye.toolbox;

import android.os.Looper;
import android.os.MessageQueue;

import cn.hikyson.godeye.GodEye;
import cn.hikyson.godeye.internal.modules.startup.StartupInfo;

/**
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

    public void onHomeCreate() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("call onHomeCreate ui thread!");
        }
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                GodEye.instance().startup().produce(generateStartupInfo(System.currentTimeMillis()));
                return false;
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

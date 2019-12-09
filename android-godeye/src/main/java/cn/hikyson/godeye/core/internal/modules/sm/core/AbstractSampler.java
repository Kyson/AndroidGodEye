package cn.hikyson.godeye.core.internal.modules.sm.core;

import android.os.Handler;

import java.util.concurrent.atomic.AtomicBoolean;

import cn.hikyson.godeye.core.utils.ThreadUtil;

/**
 * 每隔一个时间段做一次sample操作
 */
public abstract class AbstractSampler {

    private AtomicBoolean mShouldSample = new AtomicBoolean(false);
    public static final String SM_DO_DUMP = "godeye-sm-do-dump";

    //每隔interval时间dump一次信息
    long mSampleInterval;

    long mSampleDelay;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doSample();

            if (mShouldSample.get()) {
                Handler handler = ThreadUtil.obtainHandler(SM_DO_DUMP);
                if (handler != null) {
                    handler.postDelayed(mRunnable, mSampleInterval);
                }
            }
        }
    };

    AbstractSampler(long sampleInterval, long sampleDelay) {
        mSampleInterval = sampleInterval;
        mSampleDelay = sampleDelay;
    }


    public void start() {
        if (mShouldSample.getAndSet(true)) {
            return;
        }
        Handler handler = ThreadUtil.obtainHandler(SM_DO_DUMP);
        if (handler != null) {
            handler.removeCallbacks(mRunnable);
            handler.postDelayed(mRunnable, mSampleDelay);
        }
    }

    public void stop() {
        if (!mShouldSample.getAndSet(false)) {
            return;
        }
        Handler handler = ThreadUtil.obtainHandler(SM_DO_DUMP);
        if (handler != null) {
            handler.removeCallbacks(mRunnable);
        }
    }

    abstract void doSample();
}

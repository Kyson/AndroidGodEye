package cn.hikyson.godeye.core.internal.modules.sm.core;

import java.util.concurrent.atomic.AtomicBoolean;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.sm.Sm;

/**
 * 每隔一个时间段做一次sample操作
 */
public abstract class AbstractSampler {

    private AtomicBoolean mShouldSample = new AtomicBoolean(false);

    //每隔interval时间dump一次信息
    long mSampleInterval;

    long mSampleDelay;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doSample();

            if (mShouldSample.get()) {
                HandlerThreadFactory.getDoDumpThreadHandler()
                        .postDelayed(mRunnable, mSampleInterval);
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
        HandlerThreadFactory.getDoDumpThreadHandler().removeCallbacks(mRunnable);
        HandlerThreadFactory.getDoDumpThreadHandler().postDelayed(mRunnable, mSampleDelay);
    }

    public void stop() {
        if (!mShouldSample.getAndSet(false)) {
            return;
        }
        HandlerThreadFactory.getDoDumpThreadHandler().removeCallbacks(mRunnable);
    }

    abstract void doSample();
}

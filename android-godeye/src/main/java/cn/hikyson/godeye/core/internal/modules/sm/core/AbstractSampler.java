package cn.hikyson.godeye.core.internal.modules.sm.core;

import java.util.concurrent.atomic.AtomicBoolean;

import cn.hikyson.godeye.core.internal.modules.sm.Sm;

/**
 * 每隔一个时间段做一次sample操作
 */
public abstract class AbstractSampler {

    private static final int DEFAULT_SAMPLE_INTERVAL = 300;

    protected AtomicBoolean mShouldSample = new AtomicBoolean(false);

    //每隔interval时间dump一次信息
    protected long mSampleInterval;

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

    public AbstractSampler(long sampleInterval) {
        if (0 == sampleInterval) {
            sampleInterval = DEFAULT_SAMPLE_INTERVAL;
        }
        mSampleInterval = sampleInterval;
    }

    public void start() {
        if (mShouldSample.getAndSet(true)) {
            return;
        }
        HandlerThreadFactory.getDoDumpThreadHandler().removeCallbacks(mRunnable);
        HandlerThreadFactory.getDoDumpThreadHandler().postDelayed(mRunnable,
                Sm.core().getSampleDelay());
    }

    public void stop() {
        if (!mShouldSample.getAndSet(false)) {
            return;
        }
        HandlerThreadFactory.getDoDumpThreadHandler().removeCallbacks(mRunnable);
    }

    abstract void doSample();

    public void setSampleInterval(long sampleInterval) {
        mSampleInterval = sampleInterval;
    }
}

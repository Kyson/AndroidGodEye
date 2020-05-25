package cn.hikyson.godeye.core.internal.modules.fps;

import android.view.Choreographer;

import androidx.annotation.UiThread;

import cn.hikyson.godeye.core.helper.ChoreographerInjecor;

public class FpsMonitor implements Choreographer.FrameCallback {
    private Choreographer mChoreographer;
    private int mCurrentFrameCount;
    private long mStartFrameTimeNanos;
    private long mCurrentFrameTimeNanos;

    @UiThread
    public void start() {
        mChoreographer = ChoreographerInjecor.getChoreographerProvider().getChoreographer();
        mCurrentFrameCount = 0;
        mCurrentFrameTimeNanos = mStartFrameTimeNanos;
        mChoreographer.postFrameCallback(this);
    }

    @UiThread
    public void stop() {
        mChoreographer.removeFrameCallback(this);
        mChoreographer = null;
        mCurrentFrameCount = 0;
        mStartFrameTimeNanos = 0;
        mCurrentFrameTimeNanos = 0;
    }

    // callback every 16ms
    @Override
    public void doFrame(long frameTimeNanos) {
        mCurrentFrameCount++;
        if (mStartFrameTimeNanos == 0) {
            mStartFrameTimeNanos = frameTimeNanos;
        }
        mCurrentFrameTimeNanos = frameTimeNanos;
        mChoreographer.postFrameCallback(this);
    }

    /**
     * 导出当前平均fps（从上次导出开始）
     *
     * @return
     */
    @UiThread
    int exportThenReset() {
        if (mCurrentFrameCount < 1 || mCurrentFrameTimeNanos < mStartFrameTimeNanos) {
            return -1;
        }
        double fps = (mCurrentFrameCount - 1) * 1000000000.0 / (mCurrentFrameTimeNanos - mStartFrameTimeNanos);
        mStartFrameTimeNanos = 0;
        mCurrentFrameTimeNanos = 0;
        mCurrentFrameCount = 0;
        return (int) Math.round(fps);
    }
}

package cn.hikyson.godeye.core.internal.modules.fps;

import android.view.Choreographer;

import androidx.annotation.UiThread;

import cn.hikyson.godeye.core.helper.ChoreographerInjecor;
import cn.hikyson.godeye.core.utils.L;

public class FpsMonitor implements Choreographer.FrameCallback {
    private Choreographer mChoreographer;
    private int mCurrentFrameCount;
    private long mStartFrameTimeNanos;
    private long mCurrentFrameTimeNanos;

    private long currentMillis;

    @UiThread
    public void start() {
        mChoreographer = ChoreographerInjecor.getChoreographerProvider().getChoreographer();
        mCurrentFrameCount = 0;
        mCurrentFrameTimeNanos = mStartFrameTimeNanos;
        currentMillis = System.currentTimeMillis();
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

    @Override
    public void doFrame(long frameTimeNanos) {
        mCurrentFrameCount++;
        if (mStartFrameTimeNanos == 0) {
            mStartFrameTimeNanos = frameTimeNanos;
        }
        mCurrentFrameTimeNanos = frameTimeNanos;
        L.d("FpsMonitor doFrame, frameTimeNanos:%s , time cost:%s ms",frameTimeNanos,( System.currentTimeMillis() - currentMillis));
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

package cn.hikyson.godeye.core.internal.modules.sm.core;

import android.os.SystemClock;
import android.util.Printer;

public class LooperMonitor implements Printer {
    public static final String TAG = "LooperMonitor";
    // 长卡顿的阀值
    private long mLongBlockThresholdMillis;
    // 短卡顿的阀值
    private long mShortBlockThresholdMillis;
    // 一次事件开始时间
    private long mThisEventStartTime = 0;
    // 一次事件开始时间（线程内）
    private long mThisEventStartThreadTime = 0;
    private BlockListener mBlockListener = null;
    // 事件开始标记
    private boolean mEventStart = false;

    public interface BlockListener {
        void onEventStart(long startTime);

        void onEventEnd(long endTime);

        /**
         * 卡顿事件
         *
         * @param eventStartTimeMilliis     事件开始时间
         * @param eventEndTimeMillis        事件结束时间
         * @param blockTimeMillis           卡顿时间（事件处理时间）
         * @param threadBlockTimeMillis     事件真实消耗时间
         * @param longBlockThresholdMillis  长卡顿阀值标准
         * @param shortBlockThresholdMillis 短卡顿阀值标准
         */
        void onBlockEvent(long blockTimeMillis, long threadBlockTimeMillis, boolean longBlock,
                          long eventStartTimeMilliis, long eventEndTimeMillis, long longBlockThresholdMillis,
                          long shortBlockThresholdMillis);
    }

    public LooperMonitor(BlockListener blockListener, long longBlockThresholdMillis, long shortBlockThresholdMillis) {
        if (blockListener == null) {
            throw new IllegalArgumentException("blockListener should not be null.");
        }
        mBlockListener = blockListener;
        mLongBlockThresholdMillis = longBlockThresholdMillis;
        mShortBlockThresholdMillis = shortBlockThresholdMillis;
    }

    /**
     * 更新阀值配置
     *
     * @param shortBlockThresholdMillis
     * @param longBlockThresholdMillis
     */
    public void setBlockThreshold(long shortBlockThresholdMillis, long longBlockThresholdMillis) {
        this.mShortBlockThresholdMillis = shortBlockThresholdMillis;
        this.mLongBlockThresholdMillis = longBlockThresholdMillis;
    }

    @Override
    public void println(String x) {
        if (!mEventStart) {// 事件开始
            mThisEventStartTime = System.currentTimeMillis();
            mThisEventStartThreadTime = SystemClock.currentThreadTimeMillis();
            mEventStart = true;
            mBlockListener.onEventStart(mThisEventStartTime);
        } else {// 事件结束
            final long thisEventEndTime = System.currentTimeMillis();
            final long thisEventThreadEndTime = SystemClock.currentThreadTimeMillis();
            mEventStart = false;

            long eventCostTime = thisEventEndTime - mThisEventStartTime;
            long eventCostThreadTime = thisEventThreadEndTime - mThisEventStartThreadTime;
            if (eventCostTime >= mLongBlockThresholdMillis) {
                mBlockListener.onBlockEvent(eventCostTime, eventCostThreadTime, true, mThisEventStartTime,
                        thisEventEndTime, mLongBlockThresholdMillis, mShortBlockThresholdMillis);
            } else if (eventCostTime >= mShortBlockThresholdMillis) {
                mBlockListener.onBlockEvent(eventCostTime, eventCostThreadTime, false, mThisEventStartTime,
                        thisEventEndTime, mLongBlockThresholdMillis, mShortBlockThresholdMillis);
            }
            mBlockListener.onEventEnd(thisEventEndTime);
        }
    }
}
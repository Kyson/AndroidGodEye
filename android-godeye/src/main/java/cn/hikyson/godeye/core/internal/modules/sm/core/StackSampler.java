package cn.hikyson.godeye.core.internal.modules.sm.core;


import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 线程堆栈信息dump
 */
public class StackSampler extends AbstractSampler {

    private static final int DEFAULT_MAX_ENTRY_COUNT = 30;
    private static final LinkedHashMap<Long, StackTraceElement[]> sStackMap = new LinkedHashMap<>();

    private int mMaxEntryCount;
    private Thread mCurrentThread;

    StackSampler(Thread thread, long sampleIntervalMillis, long sampleDelay) {
        this(thread, DEFAULT_MAX_ENTRY_COUNT, sampleIntervalMillis, sampleDelay);
    }

    StackSampler(Thread thread, int maxEntryCount, long sampleIntervalMillis, long sampleDelay) {
        super(sampleIntervalMillis, sampleDelay);
        mCurrentThread = thread;
        mMaxEntryCount = maxEntryCount;
    }

    /**
     * 获取这个时间段内dump的堆栈信息
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Map<Long, List<StackTraceElement>> getThreadStackEntries(long startTime, long endTime) {
        Map<Long, List<StackTraceElement>> result = new LinkedHashMap<>();
        synchronized (sStackMap) {
            for (Long entryTime : sStackMap.keySet()) {
                if (startTime < entryTime && entryTime < endTime) {
                    result.put(entryTime, Arrays.asList(sStackMap.get(entryTime)));
                }
            }
        }
        return result;
    }

    @Override
    protected void doSample() {
        synchronized (sStackMap) {
            if (sStackMap.size() == mMaxEntryCount && mMaxEntryCount > 0) {
                sStackMap.remove(sStackMap.keySet().iterator().next());
            }
            sStackMap.put(System.currentTimeMillis(), mCurrentThread.getStackTrace());
        }
    }
}
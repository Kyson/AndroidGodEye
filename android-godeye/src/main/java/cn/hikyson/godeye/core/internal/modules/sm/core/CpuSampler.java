package cn.hikyson.godeye.core.internal.modules.sm.core;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.core.internal.modules.cpu.CpuInfo;
import cn.hikyson.godeye.core.utils.IoUtil;

/**
 * cpu使用率的采集
 * 必须要doSample两次才有数据
 */
public class CpuSampler extends AbstractSampler {
    private static final int BUFFER_SIZE = 1000;

    private final int BUSY_TIME;
    private static final int MAX_ENTRY_COUNT = 10;

    private final LinkedHashMap<Long, CpuInfo> mCpuInfoEntries = new LinkedHashMap<>();
    private int mPid = 0;
    private long mUserLast = 0;
    private long mSystemLast = 0;
    private long mIdleLast = 0;
    private long mIoWaitLast = 0;
    private long mTotalLast = 0;
    private long mAppCpuTimeLast = 0;

    public CpuSampler(long sampleInterval) {
        super(sampleInterval);
        BUSY_TIME = (int) (mSampleInterval * 1.2f);
    }

    @Override
    public void start() {
        reset();
        super.start();
    }

    /**
     * 获取这段时间内cpu使用情况
     *
     * @return string show cpu rate information
     */
    public List<CpuInfo> getCpuRateInfo(long startTime, long endTime) {
        List<CpuInfo> result = new ArrayList<>();
        synchronized (mCpuInfoEntries) {
            for (Map.Entry<Long, CpuInfo> entry : mCpuInfoEntries.entrySet()) {
                if (startTime < entry.getKey() && entry.getKey() < endTime) {
                    result.add(entry.getValue());
                }
            }
        }
        return result;
    }

    /**
     * cpu繁忙判定...
     * 如果每次采集的相差时间超过采样时间间隔的1.2倍，则认为cpu繁忙
     *
     * @param start
     * @param end
     * @return
     */
    public boolean isCpuBusy(long start, long end) {
        if (end - start > mSampleInterval) {
            long s = start - mSampleInterval;
            long e = start + mSampleInterval;
            long last = 0;
            synchronized (mCpuInfoEntries) {
                for (Map.Entry<Long, CpuInfo> entry : mCpuInfoEntries.entrySet()) {
                    long time = entry.getKey();
                    if (s < time && time < e) {
                        if (last != 0 && time - last > BUSY_TIME) {
                            return true;
                        }
                        last = time;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void doSample() {
        /**
         * cpu信息采集必须要有两次执行才能出结果，否则为空
         * 也就是说，如果认为block时间是1000ms,开始采集的延时时间为800ms，sampl时间间隔为300ms，如果实际运行中
         * block的时间 >1000ms && < 1400ms （delayTime + 2*intervalMillis），那么是采集不到cpu数据的
         */
        BufferedReader cpuReader = null;
        BufferedReader pidReader = null;
        try {
            cpuReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), BUFFER_SIZE);
            String cpuRate = cpuReader.readLine();
            if (cpuRate == null) {
                cpuRate = "";
            }

            if (mPid == 0) {
                mPid = android.os.Process.myPid();
            }
            pidReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + mPid + "/stat")), BUFFER_SIZE);
            String pidCpuRate = pidReader.readLine();
            if (pidCpuRate == null) {
                pidCpuRate = "";
            }
            /**
             * 从系统启动开始，花在各种处理上的apu时间
             */
            parse(cpuRate, pidCpuRate);
        } catch (Throwable ignored) {
        } finally {
            IoUtil.closeSilently(cpuReader);
            IoUtil.closeSilently(pidReader);
        }
    }

    private void reset() {
        mUserLast = 0;
        mSystemLast = 0;
        mIdleLast = 0;
        mIoWaitLast = 0;
        mTotalLast = 0;
        mAppCpuTimeLast = 0;
    }

    private void parse(String cpuRate, String pidCpuRate) {
        String[] cpuInfoArray = cpuRate.split(" ");
        if (cpuInfoArray.length < 9) {
            return;
        }
        long user = Long.parseLong(cpuInfoArray[2]);
        long nice = Long.parseLong(cpuInfoArray[3]);
        long system = Long.parseLong(cpuInfoArray[4]);
        long idle = Long.parseLong(cpuInfoArray[5]);
        long ioWait = Long.parseLong(cpuInfoArray[6]);
        long total = user + nice + system + idle + ioWait
                + Long.parseLong(cpuInfoArray[7])
                + Long.parseLong(cpuInfoArray[8]);
        String[] pidCpuInfoList = pidCpuRate.split(" ");
        if (pidCpuInfoList.length < 17) {
            return;
        }

        long appCpuTime = Long.parseLong(pidCpuInfoList[13])
                + Long.parseLong(pidCpuInfoList[14])
                + Long.parseLong(pidCpuInfoList[15])
                + Long.parseLong(pidCpuInfoList[16]);
        if (mTotalLast != 0) {
            long idleTime = idle - mIdleLast;
            long totalTime = total - mTotalLast;
            /**
             * 一个sample时间段内
             * 总的cpu使用率
             * app的cpu使用率
             * 用户进程cpu使用率
             * 系统进程cpu使用率
             * io等待时间占比
             */
            CpuInfo cpuInfo = new CpuInfo((totalTime - idleTime) * 100.0 / totalTime, (appCpuTime - mAppCpuTimeLast) *
                    100.0 / totalTime,
                    (user - mUserLast) * 100.0 / totalTime, (system - mSystemLast) * 100.0 / totalTime, (ioWait -
                    mIoWaitLast) * 100.0 / totalTime);
            synchronized (mCpuInfoEntries) {
                mCpuInfoEntries.put(System.currentTimeMillis(), cpuInfo);
                if (mCpuInfoEntries.size() > MAX_ENTRY_COUNT) {
                    int overSize = mCpuInfoEntries.size() - MAX_ENTRY_COUNT;
                    List<Long> willRemove = new ArrayList<>();
                    for (Map.Entry<Long, CpuInfo> entry : mCpuInfoEntries.entrySet()) {
                        willRemove.add(entry.getKey());
                        if (willRemove.size() >= overSize) {
                            break;
                        }
                    }
                    for (Long removeKey : willRemove) {
                        mCpuInfoEntries.remove(removeKey);
                    }
                }
            }
        }
        mUserLast = user;
        mSystemLast = system;
        mIdleLast = idle;
        mIoWaitLast = ioWait;
        mTotalLast = total;

        mAppCpuTimeLast = appCpuTime;
    }
}
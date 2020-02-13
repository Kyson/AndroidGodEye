package cn.hikyson.godeye.core.internal.modules.sm.core;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.hikyson.godeye.core.internal.modules.cpu.CpuInfo;
import cn.hikyson.godeye.core.utils.StacktraceUtil;

/**
 * 时间单位毫秒
 * Created by kysonchao on 2017/5/17.
 */
@Keep
public class LongBlockInfo implements Serializable {
    //卡顿开始时间
    public long timeStart;
    //卡顿结束时间
    public long timeEnd;
    //卡顿时间
    public long blockTime;
    /**
     * 卡顿所在线程消耗的时间
     * 如果线程消耗时间和实际时间（timeCost）差不多，那么说明在这个线程上（主线程）执行某个任务太耗时
     * 如果线程消耗时间远小于实际时间，那么说明这个线程正在等待资源（等待资源耗时）
     */
    public long threadTimeCost;
    //cpu是否繁忙
    public boolean cpuBusy;
    //cpu使用情况
    public List<CpuInfo> cpuRateInfos;
    //原始的堆栈信息
    public transient Map<Long, List<StackTraceElement>> mThreadStackEntries;
    //线程的堆栈情况(去重并且转换为字符串显示)
    public Map<String, List<String>> threadStackEntriesForExport;
    //内存详情
    public MemoryInfo memoryDetailInfo;

    public LongBlockInfo(long timeStart, long timeEnd, long threadTimeCost, long blockTime, boolean cpuBusy, List<CpuInfo> cpuRateInfos, Map<Long, List<StackTraceElement>> threadStackEntries, MemoryInfo memoryDetailInfo) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.blockTime = blockTime;
        this.threadTimeCost = threadTimeCost;
        this.memoryDetailInfo = memoryDetailInfo;
        this.cpuBusy = cpuBusy;
        this.cpuRateInfos = cpuRateInfos;
        this.mThreadStackEntries = threadStackEntries;
        this.threadStackEntriesForExport = convertToStackString(threadStackEntries);
    }

    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US);

    /**
     * 原始的堆栈信息转换为字符串类型的堆栈信息
     *
     * @param ts
     * @return
     */
    private static Map<String, List<String>> convertToStackString(Map<Long, List<StackTraceElement>> ts) {
        if (ts == null || ts.isEmpty()) {
            return new LinkedHashMap<>();
        }
        // 筛选之后的堆栈
        Map<Long, List<StackTraceElement>> filterMap = new LinkedHashMap<>();
        for (Long key : ts.keySet()) {
            List<StackTraceElement> value = ts.get(key);
            if (!filterMap.containsValue(value)) {// 筛选一下是否存在堆栈信息相同的
                filterMap.put(key, value);
            }
        }
        // 转换为字符串
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (Map.Entry<Long, List<StackTraceElement>> entry : filterMap.entrySet()) {
            result.put(TIME_FORMATTER.format(entry.getKey()), StacktraceUtil.stackTraceToStringArray(entry.getValue()));
        }
        return result;
    }

    @Override
    public String toString() {
        return "LongBlockInfo{" +
                "timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", blockTime=" + blockTime +
                ", threadTimeCost=" + threadTimeCost +
                ", memoryDetailInfo=" + memoryDetailInfo +
                ", cpuBusy=" + cpuBusy +
                ", cpuRateInfos=" + cpuRateInfos +
                ", threadStackEntriesForExport=" + threadStackEntriesForExport +
                ", mThreadStackEntries=" + mThreadStackEntries +
                '}';
    }

    /**
     * 根据数据生成特征值，同一种卡顿特征相同，用于筛选卡顿是否为同一个
     * 根据卡顿堆栈的第一个堆栈，计算其hashcode
     * 如果没有堆栈，则没有特征值
     *
     * @deprecated
     */
    public String generateKey() {
        Iterator<Map.Entry<Long, List<StackTraceElement>>> iterator = mThreadStackEntries.entrySet().iterator();
        if (iterator.hasNext()) {
            return String.valueOf(iterator.next().getValue().hashCode());
        }
        return "";
    }
}

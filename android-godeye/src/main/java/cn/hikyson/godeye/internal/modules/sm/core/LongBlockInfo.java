package cn.hikyson.godeye.internal.modules.sm.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.internal.modules.cpu.CpuInfo;
import cn.hikyson.godeye.utils.StacktraceUtil;

/**
 * 时间单位毫秒
 * Created by kysonchao on 2017/5/17.
 */
public class LongBlockInfo {
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
    //内存详情
    public MemoryInfo memoryDetailInfo;
    //cpu是否繁忙
    public boolean cpuBusy;
    //cpu使用情况
    public List<CpuInfo> cpuRateInfos = new ArrayList<>();
    //线程的堆栈情况(去重并且转换为字符串显示)
    public Map<String, List<String>> threadStackEntriesForExport = new LinkedHashMap<>();
    //原始的堆栈信息
    public Map<Long, List<StackTraceElement>> mThreadStackEntries = new LinkedHashMap<>();

    public static LongBlockInfo create(long realTimeStart, long realTimeEnd, long threadTime, long blockTime, boolean cpuBusy, List<CpuInfo> crs, Map<Long, List<StackTraceElement>> ts, MemoryInfo memoryInfo) {
        LongBlockInfo blockBaseinfo = new LongBlockInfo();
        blockBaseinfo.timeStart = realTimeStart;
        blockBaseinfo.timeEnd = realTimeEnd;
        blockBaseinfo.threadTimeCost = threadTime;
        blockBaseinfo.blockTime = blockTime;
        blockBaseinfo.cpuBusy = cpuBusy;
        blockBaseinfo.cpuRateInfos = crs;
        blockBaseinfo.mThreadStackEntries = ts;
        blockBaseinfo.threadStackEntriesForExport = StacktraceUtil.convertToStackString(blockBaseinfo.mThreadStackEntries);
        blockBaseinfo.memoryDetailInfo = memoryInfo;
        return blockBaseinfo;
    }

    @Override
    public String toString() {
        return "BlockBaseinfo{" +
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
     */
    public String generateKey() {
        Iterator<Map.Entry<Long, List<StackTraceElement>>> iterator = mThreadStackEntries.entrySet().iterator();
        if (iterator.hasNext()) {
            return String.valueOf(iterator.next().getValue().hashCode());
        }
        return "";
    }
}

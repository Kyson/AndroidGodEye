package cn.hikyson.godeye.core.internal.modules.sm.core;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class ShortBlockInfo {
    //卡顿开始时间
    public long timeStart;
    //卡顿结束时间
    public long timeEnd;
    public long threadTimeCost;
    public long blockTime;

    public MemoryInfo memoryDetailInfo;

    public ShortBlockInfo(long timeStart, long timeEnd, long threadTimeCost, long blockTime, MemoryInfo memoryDetailInfo) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.threadTimeCost = threadTimeCost;
        this.blockTime = blockTime;
        this.memoryDetailInfo = memoryDetailInfo;
    }

    @Override
    public String toString() {
        return "ShortBlockInfo{" +
                "timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", threadTimeCost=" + threadTimeCost +
                ", blockTime=" + blockTime +
                ", memoryDetailInfo=" + memoryDetailInfo +
                '}';
    }
}

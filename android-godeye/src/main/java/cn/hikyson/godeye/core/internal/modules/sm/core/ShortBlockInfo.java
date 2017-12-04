package cn.hikyson.godeye.core.internal.modules.sm.core;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class ShortBlockInfo {
    public long blockTime;

    public ShortBlockInfo(long blockTime) {
        this.blockTime = blockTime;
    }

    @Override
    public String toString() {
        return "ShortBlockInfo{" +
                "blockTime=" + blockTime +
                '}';
    }
}

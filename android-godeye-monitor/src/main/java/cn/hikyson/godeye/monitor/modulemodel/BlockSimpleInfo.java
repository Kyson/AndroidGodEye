package cn.hikyson.godeye.monitor.modulemodel;

import cn.hikyson.godeye.core.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.monitor.utils.GsonUtil;

public class BlockSimpleInfo {
    public long blockTime;
    public String blockBaseinfo;

    public BlockSimpleInfo(BlockInfo blockInfo) {
        if (BlockInfo.BlockType.LONG.equals(blockInfo.blockType)) {
            this.blockTime = blockInfo.longBlockInfo.blockTime;
            //把不需要的信息隐藏掉
            blockInfo.longBlockInfo.mThreadStackEntries = null;
            this.blockBaseinfo = GsonUtil.toJson(blockInfo.longBlockInfo);
        } else if (BlockInfo.BlockType.SHORT.equals(blockInfo.blockType)) {
            this.blockTime = blockInfo.shortBlockInfo.blockTime;
            this.blockBaseinfo = null;
        } else {
            //do nothing
        }
    }
}

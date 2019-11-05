package cn.hikyson.godeye.monitor.modules;

import android.support.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.core.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.monitor.utils.GsonUtil;

@Keep
public class BlockSimpleInfo implements Serializable {
    public long blockTime;
    public String blockBaseinfo;

    public BlockSimpleInfo(BlockInfo blockInfo) {
        if (BlockInfo.BlockType.LONG.equals(blockInfo.blockType)) {
            this.blockTime = blockInfo.longBlockInfo.blockTime;
            this.blockBaseinfo = GsonUtil.toJson(blockInfo.longBlockInfo);
        } else if (BlockInfo.BlockType.SHORT.equals(blockInfo.blockType)) {
            this.blockTime = blockInfo.shortBlockInfo.blockTime;
            this.blockBaseinfo = GsonUtil.toJson(blockInfo.shortBlockInfo);
        }  //do nothing

    }
}

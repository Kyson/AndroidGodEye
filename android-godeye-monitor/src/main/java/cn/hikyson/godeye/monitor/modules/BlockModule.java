package cn.hikyson.godeye.monitor.modules;

import java.util.ArrayList;
import java.util.Collection;

import cn.hikyson.godeye.core.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.monitor.driver.Pipe;
import cn.hikyson.godeye.monitor.utils.GsonUtil;

/**
 * Created by kysonchao on 2017/9/4.
 */
public class BlockModule extends BaseListModule<BlockModule.BlockSimpleInfo> {

    @Override
    Collection<BlockModule.BlockSimpleInfo> popData() {
        Collection<BlockInfo> blockInfos = Pipe.instance().popBlockInfos();
        Collection<BlockModule.BlockSimpleInfo> blockSimpleInfos = new ArrayList<>();
        for (BlockInfo blockInfo : blockInfos) {
            blockSimpleInfos.add(new BlockModule.BlockSimpleInfo(blockInfo));
        }
        return blockSimpleInfos;
    }

    static class BlockSimpleInfo {
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

}

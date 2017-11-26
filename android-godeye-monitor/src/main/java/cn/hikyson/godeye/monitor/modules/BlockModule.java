package cn.hikyson.godeye.monitor.modules;

import android.net.Uri;

import com.ctrip.ibu.performance.debugmonitor.StotageQueue;
import com.ctrip.ibu.performance.internal.sm.blockcanary.core.internal.BlockBaseinfo;
import com.ctrip.ibu.utility.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kysonchao on 2017/9/4.
 */
public class BlockModule implements Module {
    @Override
    public byte[] process(String path, Uri uri) throws Throwable {
        List<BlockBaseinfo> blockBaseinfos = StotageQueue.popLongBlockInfos();
        List<Long> shortBlockInfos = StotageQueue.popShortBlockInfos();
        List<BlockInfo> blockInfos = new ArrayList<>();
        if (blockBaseinfos != null && !blockBaseinfos.isEmpty()) {
            for (BlockBaseinfo blockBaseinfo : blockBaseinfos) {
                blockInfos.add(new BlockInfo(blockBaseinfo.blockTime, blockBaseinfo));
            }
        }
        if (shortBlockInfos != null && !shortBlockInfos.isEmpty()) {
            for (long shortBlock : shortBlockInfos) {
                blockInfos.add(new BlockInfo(shortBlock));
            }
        }
        if (blockInfos.isEmpty()) {
            return new ResultWrapper("no new block info").toBytes();
        }
        return new ResultWrapper<>(blockInfos).toBytes();
    }

    public static class BlockInfo {
        public boolean isLongBlock;
        public long blockTime;
        public String blockBaseinfo;

        public BlockInfo(long blockTime, BlockBaseinfo blockBaseinfo) {
            this.isLongBlock = true;
            this.blockTime = blockTime;
            this.blockBaseinfo = JsonUtil.toJson(blockBaseinfo);
        }

        public BlockInfo(long blockTime) {
            this.isLongBlock = false;
            this.blockTime = blockTime;
            this.blockBaseinfo = null;
        }
    }
}

package cn.hikyson.godeye.internal.engines;

import android.content.Context;
import android.support.annotation.WorkerThread;

import cn.hikyson.godeye.internal.Eater;
import cn.hikyson.godeye.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.internal.modules.sm.core.BlockInterceptor;
import cn.hikyson.godeye.internal.modules.sm.core.LongBlockInfo;
import cn.hikyson.godeye.internal.modules.sm.core.ShortBlockInfo;
import cn.hikyson.godeye.internal.modules.sm.core.SmConfig;
import cn.hikyson.godeye.internal.modules.sm.core.SmCore;

/**
 * Created by kysonchao on 2017/11/23.
 */

public class BlockEngine extends InstallEngine<BlockInfo, SmConfig> {
    private SmCore mBlockCore;
    private Context mContext;

    public BlockEngine(Context context, Eater<BlockInfo> eater) {
        super(eater);
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void install(SmConfig config) {
        this.mBlockCore = new SmCore(mContext, config);
        this.mBlockCore.addBlockInterceptor(new BlockInterceptor() {
            @Override
            public void onStart(Context context) {
            }

            @Override
            public void onStop(Context context) {
            }

            @WorkerThread
            @Override
            public void onShortBlock(Context context, long blockTimeMillis) {
                eat(new BlockInfo(new ShortBlockInfo(blockTimeMillis)));
            }

            @WorkerThread
            @Override
            public void onLongBlock(Context context, LongBlockInfo blockInfo) {
                eat(new BlockInfo(blockInfo));
            }
        });
        mBlockCore.install();
    }

    @Override
    public void uninstall() {
        if (mBlockCore != null) {
            mBlockCore.uninstall();
        }
    }
}

package cn.hikyson.godeye.core.internal.modules.sm;

import android.content.Context;
import android.support.annotation.WorkerThread;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.internal.modules.sm.core.BlockInterceptor;
import cn.hikyson.godeye.core.internal.modules.sm.core.LongBlockInfo;
import cn.hikyson.godeye.core.internal.modules.sm.core.ShortBlockInfo;
import cn.hikyson.godeye.core.internal.modules.sm.core.SmCore;
import cn.hikyson.godeye.core.utils.L;


public final class Sm extends ProduceableSubject<BlockInfo> implements Install<SmContext> {

    private SmCore mBlockCore;
    private boolean mInstalled = false;

    private Sm() {
    }

    private static class InstanceHoler {
        private static Sm sINSTANCE = new Sm();
    }

    public static Sm instance() {
        return InstanceHoler.sINSTANCE;
    }

    public synchronized void install(Context context) {
        install(new SmContextImpl(context, 2000, 500, 800));
    }

    @Override
    public synchronized void install(SmContext config) {
        if (mInstalled) {
            L.d("sm already installed, ignore.");
            return;
        }
        this.mInstalled = true;
        this.mBlockCore = new SmCore(config.context(), config.config());
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
                produce(new BlockInfo(new ShortBlockInfo(blockTimeMillis)));
            }

            @WorkerThread
            @Override
            public void onLongBlock(Context context, LongBlockInfo blockInfo) {
                produce(new BlockInfo(blockInfo));
            }
        });
        mBlockCore.install();
        L.d("sm installed");
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("sm already uninstalled, ignore.");
            return;
        }
        mInstalled = false;
        mBlockCore.uninstall();
        L.d("sm uninstalled");
    }

    public static SmCore core() {
        return instance().mBlockCore;
    }
}

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
import cn.hikyson.godeye.core.utils.ThreadUtil;

/**
 * 卡顿模块
 * 安装卸载可以在任意线程
 * 发射数据在子线程
 */
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

    @Override
    public synchronized void install(SmContext config) {
        if (mInstalled) {
            L.d("sm already installed, ignore.");
            return;
        }
        this.mInstalled = true;
        this.mBlockCore = new SmCore(config.context(), config.debugNotify(),
                config.longBlockThreshold(), config.shortBlockThreshold(), config.dumpInterval());
        this.mBlockCore.addBlockInterceptor(new BlockInterceptor() {
            @Override
            public void onStart(Context context) {
            }

            @Override
            public void onStop(Context context) {
            }

            @WorkerThread
            @Override
            public void onShortBlock(Context context, ShortBlockInfo shortBlockInfo) {
                ThreadUtil.ensureWorkThread("Sm onShortBlock");
                produce(new BlockInfo(shortBlockInfo));
            }

            @WorkerThread
            @Override
            public void onLongBlock(Context context, LongBlockInfo blockInfo) {
                ThreadUtil.ensureWorkThread("Sm onLongBlock");
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

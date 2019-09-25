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
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

/**
 * 卡顿模块
 * 安装卸载可以在任意线程
 * 发射数据在子线程
 */
public final class Sm extends ProduceableSubject<BlockInfo> implements Install<SmContext> {

    private SmCore mBlockCore;
    private SmContext mSmContext;
    private boolean mInstalled = false;

    @Override
    public synchronized void install(SmContext config) {
        if (mInstalled) {
            L.d("Sm already installed, ignore.");
            return;
        }
        this.mInstalled = true;
        this.mSmContext = config;
        this.mBlockCore = new SmCore(config.context(), config.debugNotification(),
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
        L.d("Sm installed");
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("Sm already uninstalled, ignore.");
            return;
        }
        mInstalled = false;
        this.mSmContext = null;
        mBlockCore.uninstall();
        L.d("Sm uninstalled");
    }

    @Override
    protected Subject<BlockInfo> createSubject() {
        return ReplaySubject.create();
    }

    public SmCore getBlockCore() {
        return mBlockCore;
    }

    public SmContext getSmContext() {
        return mSmContext;
    }
}

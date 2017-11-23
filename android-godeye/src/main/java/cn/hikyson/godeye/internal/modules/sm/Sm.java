package cn.hikyson.godeye.internal.modules.sm;

import android.content.Context;
import android.support.annotation.WorkerThread;

import cn.hikyson.godeye.internal.Install;
import cn.hikyson.godeye.internal.Consumer;
import cn.hikyson.godeye.internal.modules.sm.core.BlockInterceptor;
import cn.hikyson.godeye.internal.modules.sm.core.LongBlockInfo;
import cn.hikyson.godeye.internal.modules.sm.core.ShortBlockInfo;
import cn.hikyson.godeye.internal.modules.sm.core.SmConfig;
import cn.hikyson.godeye.internal.modules.sm.core.SmCore;
import cn.hikyson.godeye.utils.L;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


public final class Sm implements Install<SmConfig>, Consumer<BlockInfo> {

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
    public synchronized void install(Context context, SmConfig smConfig) {
        if (mInstalled) {
            L.d("SM has installed");
            return;
        }
        this.mInstalled = true;
        this.mBlockCore = new SmCore(context, smConfig);
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
                if (mBlockInfoObservableEmitter != null && !mBlockInfoObservableEmitter.isDisposed()) {
                    mBlockInfoObservableEmitter.onNext(new BlockInfo(new ShortBlockInfo(blockTimeMillis)));
                }
            }

            @WorkerThread
            @Override
            public void onLongBlock(Context context, LongBlockInfo blockInfo) {
                if (mBlockInfoObservableEmitter != null && !mBlockInfoObservableEmitter.isDisposed()) {
                    mBlockInfoObservableEmitter.onNext(new BlockInfo(blockInfo));
                }
            }
        });
        mBlockCore.install();
    }

    @Override
    public synchronized void uninstall(Context context) {
        if (!mInstalled) {
            L.d("SM has uninstalled");
            return;
        }
        mInstalled = false;
        mBlockCore.uninstall();
    }

    private ObservableEmitter<BlockInfo> mBlockInfoObservableEmitter;

    @Override
    public Observable<BlockInfo> consume() {
        return Observable.create(new ObservableOnSubscribe<BlockInfo>() {
            @Override
            public void subscribe(ObservableEmitter<BlockInfo> e) throws Exception {
                mBlockInfoObservableEmitter = e;
            }
        });
    }

    public static SmCore core() {
        return instance().mBlockCore;
    }
}

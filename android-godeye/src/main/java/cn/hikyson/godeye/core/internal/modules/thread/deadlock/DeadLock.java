package cn.hikyson.godeye.core.internal.modules.thread.deadlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.internal.modules.thread.ExcludeSystemThreadFilter;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadFilter;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 检测线程死锁
 * deadlock模块依赖于threaddump模块，所以如果要安装的话需要先安装threaddump模块
 * Created by kysonchao on 2018/1/12.
 */
public class DeadLock extends ProduceableSubject<List<Thread>> implements Install<DeadLockContext> {
    private CompositeDisposable mCompositeDisposable;

    public void install(Observable<List<Thread>> listSubject) {
        install(new DeadLockContextImpl(listSubject, new ExcludeSystemThreadFilter()));
    }

    public void install(Observable<List<Thread>> listSubject, ThreadFilter threadFilter) {
        install(new DeadLockContextImpl(listSubject, threadFilter));
    }

    @Override
    public void install(final DeadLockContext config) {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            L.d("deadlock already installed, ignore.");
            return;
        }
        L.d("deadlock installed.");
        mCompositeDisposable = new CompositeDisposable();
        final DeadLockDetector deadLockDetector = new DeadLockDetector();
        mCompositeDisposable.add(config.threadInfoSubject().sample(config.intervalMillis(), TimeUnit.MILLISECONDS).map(new Function<List<Thread>, List<Thread>>() {
            @Override
            public List<Thread> apply(List<Thread> threadInfos) throws Exception {
                List<Thread> results = new ArrayList<>();
                for (Thread ti : threadInfos) {
                    if (!config.threadFilter().filter(ti)) {
                        continue;
                    }
                    if (Thread.State.BLOCKED.equals(ti.getState()) || Thread.State.WAITING.equals(ti.getState())) {
                        results.add(ti);
                    }
                }
                return results;
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<List<Thread>>() {
            @Override
            public void accept(List<Thread> threads) throws Exception {
                List<Thread> deadLockThreads = deadLockDetector.detect(threads);
                if (deadLockThreads == null || deadLockThreads.isEmpty()) {
                    return;
                }
                produce(deadLockThreads);
            }
        }));
    }

    @Override
    public void uninstall() {
        if (mCompositeDisposable != null) {
            if (!mCompositeDisposable.isDisposed()) {
                mCompositeDisposable.dispose();
            }
            mCompositeDisposable = null;
        }
        L.d("deadlock uninstalled.");
    }
}

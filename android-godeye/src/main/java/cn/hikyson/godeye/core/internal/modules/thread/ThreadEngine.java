package cn.hikyson.godeye.core.internal.modules.thread;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class ThreadEngine implements Engine {
    private Producer<List<Thread>> mProducer;
    private long mIntervalMillis;
    private CompositeDisposable mCompositeDisposable;
    private @NonNull
    ThreadFilter mThreadFilter;

    public ThreadEngine(Producer<List<Thread>> producer, long intervalMillis, @NonNull ThreadFilter threadFilter) {
        mProducer = producer;
        mIntervalMillis = intervalMillis;
        mThreadFilter = threadFilter;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void work() {
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS).map(aLong -> {
            ThreadUtil.ensureWorkThread("ThreadEngine apply");
            return dump(mThreadFilter);
        }).subscribeOn(ThreadUtil.sComputationScheduler)
                .observeOn(ThreadUtil.sComputationScheduler)
                .subscribe(food -> {
                    ThreadUtil.ensureWorkThread("ThreadEngine accept");
                    mProducer.produce(food);
                }));
    }

    @Override
    public void shutdown() {
        mCompositeDisposable.dispose();
    }

    /**
     * dump当前所有线程
     *
     * @param threadFilter
     * @return
     */
    public static List<Thread> dump(@NonNull ThreadFilter threadFilter) {
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        if (rootGroup == null) {
            return new ArrayList<>();
        }
        ThreadGroup parentGroup;
        while ((parentGroup = rootGroup.getParent()) != null) {
            rootGroup = parentGroup;
        }
        Thread[] threads = new Thread[rootGroup.activeCount()];
        while (rootGroup.enumerate(threads, true) >= threads.length) {
            threads = new Thread[threads.length * 2];
        }
        List<Thread> threadList = new ArrayList<>();
        for (Thread thread : threads) {
            if (thread != null && !threadList.contains(thread) && threadFilter.filter(thread)) {
                threadList.add(thread);
            }
        }
        return threadList;
    }
}

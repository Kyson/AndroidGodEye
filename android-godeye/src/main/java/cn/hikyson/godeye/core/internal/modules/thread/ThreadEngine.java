package cn.hikyson.godeye.core.internal.modules.thread;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS).
                concatMap(new Function<Long, ObservableSource<List<Thread>>>() {
                    @Override
                    public ObservableSource<List<Thread>> apply(Long aLong) throws Exception {
                        return create(mThreadFilter);
                    }
                }).subscribe(new Consumer<List<Thread>>() {
            @Override
            public void accept(List<Thread> food) throws Exception {
                mProducer.produce(food);
            }
        }));
    }

    @Override
    public void shutdown() {
        mCompositeDisposable.dispose();
    }

    private Observable<List<Thread>> create(@NonNull final ThreadFilter threadFilter) {
        return Observable.fromCallable(new Callable<List<Thread>>() {
            @Override
            public List<Thread> call() throws Exception {
                return dump(threadFilter);
            }
        });
    }

    /**
     * dump当前所有线程
     *
     * @param threadFilter
     * @return
     */
    public static List<Thread> dump(@NonNull ThreadFilter threadFilter) {
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
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

    public interface ThreadFilter {
        boolean filter(Thread thread);
    }
}

package cn.hikyson.godeye.core.internal.modules.thread;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by kysonchao on 2017/11/23.
 */
class ThreadEngine implements Engine {
    private Producer<List<ThreadInfo>> mProducer;
    private long mIntervalMillis;
    private CompositeDisposable mCompositeDisposable;
    private @NonNull
    ThreadFilter mThreadFilter;
    private @NonNull
    ThreadTagger mThreadTagger;

    ThreadEngine(Producer<List<ThreadInfo>> producer, ThreadConfig config) {
        ThreadFilter threadFilter = new ExcludeSystemThreadFilter();
        try {
            threadFilter = (ThreadFilter) Class.forName(config.threadFilter).newInstance();
        } catch (Throwable e) {
            L.e(String.format("Thread install warning, can not find ThreadFilter class %s, use ExcludeSystemThreadFilter, error: %s", config.threadFilter, e));
        }
        ThreadTagger threadTagger = new DefaultThreadTagger();
        try {
            threadTagger = (ThreadTagger) Class.forName(config.threadTagger).newInstance();
        } catch (Throwable e) {
            L.e(String.format("Thread install warning, can not find ThreadTagger class %s, use DefaultThreadTagger, error: %s", config.threadTagger, e));
        }
        mProducer = producer;
        mIntervalMillis = config.intervalMillis;
        mThreadFilter = threadFilter;
        mThreadTagger = threadTagger;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void work() {
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS).map(aLong -> {
            ThreadUtil.ensureWorkThread("ThreadEngine apply");
            return dump(mThreadFilter, mThreadTagger);
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
    @VisibleForTesting
    static List<ThreadInfo> dump(@NonNull ThreadFilter threadFilter, @NonNull ThreadTagger threadTagger) {
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
        List<ThreadInfo> threadList = new ArrayList<>();
        Set<Long> threadIds = new HashSet<>();
        for (Thread thread : threads) {
            if (thread != null && !threadIds.contains(thread.getId()) && threadFilter.filter(thread)) {
                threadIds.add(thread.getId());
                ThreadInfo threadInfo = new ThreadInfo(thread);
                threadInfo.threadTag = threadTagger.tag(thread, threadInfo);
                threadList.add(threadInfo);
            }
        }
        return threadList;
    }
}

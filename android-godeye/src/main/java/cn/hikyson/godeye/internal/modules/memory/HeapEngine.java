package cn.hikyson.godeye.internal.modules.memory;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.internal.Engine;
import cn.hikyson.godeye.internal.Producer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class HeapEngine implements Engine {
    private Producer<HeapInfo> mProducer;
    private long mIntervalMillis;
    private CompositeDisposable mCompositeDisposable;

    public HeapEngine(Producer<HeapInfo> producer, long intervalMillis) {
        mProducer = producer;
        mIntervalMillis = intervalMillis;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void work() {
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS).
                concatMap(new Function<Long, ObservableSource<HeapInfo>>() {
                    @Override
                    public ObservableSource<HeapInfo> apply(Long aLong) throws Exception {
                        return create();
                    }
                }).subscribe(new Consumer<HeapInfo>() {
            @Override
            public void accept(HeapInfo food) throws Exception {
                mProducer.produce(food);
            }
        }));
    }

    @Override
    public void shutdown() {
        mCompositeDisposable.dispose();
    }

    private Observable<HeapInfo> create() {
        return Observable.fromCallable(new Callable<HeapInfo>() {
            @Override
            public HeapInfo call() throws Exception {
                return MemoryUtil.getAppHeapInfo();
            }
        });
    }
}

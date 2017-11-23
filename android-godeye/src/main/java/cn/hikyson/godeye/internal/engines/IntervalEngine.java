package cn.hikyson.godeye.internal.engines;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.internal.Eater;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 每隔一段时间生产一个数据
 * Created by kysonchao on 2017/11/23.
 */
public abstract class IntervalEngine<T,C> extends EngineImpl<T,C> {
    private long mIntervalMillis;
    private CompositeDisposable mCompositeDisposable;

    public IntervalEngine(Eater<T> eater, long intervalMillis) {
        super(eater);
        mCompositeDisposable = new CompositeDisposable();
        mIntervalMillis = intervalMillis;
    }

    abstract Observable<T> produce();

    @Override
    public void work(C config) {
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS).
                concatMap(new Function<Long, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(Long aLong) throws Exception {
                        return produce();
                    }
                }).subscribe(new Consumer<T>() {
            @Override
            public void accept(T food) throws Exception {
                eat(food);
            }
        }));
    }

    @Override
    public void shutdown() {
        mCompositeDisposable.dispose();
    }
}

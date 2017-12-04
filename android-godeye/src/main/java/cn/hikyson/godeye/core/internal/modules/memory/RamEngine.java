package cn.hikyson.godeye.core.internal.modules.memory;

import android.content.Context;

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
public class RamEngine implements Engine {
    private Context mContext;
    private Producer<RamInfo> mProducer;
    private long mIntervalMillis;
    private CompositeDisposable mCompositeDisposable;

    public RamEngine(Context context, Producer<RamInfo> producer, long intervalMillis) {
        mContext = context;
        mProducer = producer;
        mIntervalMillis = intervalMillis;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void work() {
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS).
                concatMap(new Function<Long, ObservableSource<RamInfo>>() {
                    @Override
                    public ObservableSource<RamInfo> apply(Long aLong) throws Exception {
                        return create();
                    }
                }).subscribe(new Consumer<RamInfo>() {
            @Override
            public void accept(RamInfo food) throws Exception {
                mProducer.produce(food);
            }
        }));
    }

    @Override
    public void shutdown() {
        mCompositeDisposable.dispose();
    }

    private Observable<RamInfo> create() {
        return Observable.fromCallable(new Callable<RamInfo>() {
            @Override
            public RamInfo call() throws Exception {
                return MemoryUtil.getRamInfo(mContext);
            }
        });
    }
}

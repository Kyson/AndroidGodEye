package cn.hikyson.godeye.core.internal.modules.memory;

import android.content.Context;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class PssEngine implements Engine {
    private Context mContext;
    private Producer<PssInfo> mProducer;
    private long mIntervalMillis;
    private CompositeDisposable mCompositeDisposable;

    public PssEngine(Context context, Producer<PssInfo> producer, long intervalMillis) {
        mContext = context;
        mProducer = producer;
        mIntervalMillis = intervalMillis;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void work() {
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS).map(new Function<Long, PssInfo>() {
            @Override
            public PssInfo apply(Long aLong) throws Exception {
                ThreadUtil.ensureWorkThread("PssEngine accept");
                return MemoryUtil.getAppPssInfo(mContext);
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(new Consumer<PssInfo>() {
                    @Override
                    public void accept(PssInfo food) throws Exception {
                        ThreadUtil.ensureWorkThread("PssEngine accept");
                        mProducer.produce(food);
                    }
                }));
    }

    @Override
    public void shutdown() {
        mCompositeDisposable.dispose();
    }
}

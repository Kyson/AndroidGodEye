package cn.hikyson.godeye.core.internal.modules.memory;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Observable;
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

    RamEngine(Context context, Producer<RamInfo> producer, long intervalMillis) {
        mContext = context;
        mProducer = producer;
        mIntervalMillis = intervalMillis;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void work() {
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS).map(new Function<Long, RamInfo>() {
            @Override
            public RamInfo apply(Long aLong) throws Exception {
                ThreadUtil.ensureWorkThread("RamEngine apply");
                return MemoryUtil.getRamInfo(mContext);
            }
        })
                .subscribeOn(ThreadUtil.computationScheduler())
                .observeOn(ThreadUtil.computationScheduler())
                .subscribe(new Consumer<RamInfo>() {
                    @Override
                    public void accept(RamInfo food) throws Exception {
                        ThreadUtil.ensureWorkThread("RamEngine accept");
                        mProducer.produce(food);
                    }
                }));
    }

    @Override
    public void shutdown() {
        mCompositeDisposable.dispose();
    }
}

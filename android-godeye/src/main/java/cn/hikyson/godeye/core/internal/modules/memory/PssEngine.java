package cn.hikyson.godeye.core.internal.modules.memory;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class PssEngine implements Engine {
    private Context mContext;
    private Producer<PssInfo> mProducer;
    private long mIntervalMillis;
    private CompositeDisposable mCompositeDisposable;

    PssEngine(Context context, Producer<PssInfo> producer, long intervalMillis) {
        mContext = context;
        mProducer = producer;
        mIntervalMillis = intervalMillis;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void work() {
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS).map(aLong -> {
            ThreadUtil.ensureWorkThread("PssEngine accept");
            return MemoryUtil.getAppPssInfo(mContext);
        }).subscribeOn(ThreadUtil.computationScheduler())
                .observeOn(ThreadUtil.computationScheduler())
                .subscribe(food -> {
                    ThreadUtil.ensureWorkThread("PssEngine accept");
                    mProducer.produce(food);
                }));
    }

    @Override
    public void shutdown() {
        mCompositeDisposable.dispose();
    }
}

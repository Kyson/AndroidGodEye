package cn.hikyson.godeye.core.internal.modules.cpu;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class CpuEngine implements Engine {
    private Producer<CpuInfo> mProducer;
    private long mIntervalMillis;
    private CompositeDisposable mCompositeDisposable;

    public CpuEngine(Producer<CpuInfo> producer, long intervalMillis) {
        mProducer = producer;
        mIntervalMillis = intervalMillis;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void work() {
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS)
                .subscribeOn(ThreadUtil.sComputationScheduler)
                .observeOn(ThreadUtil.sComputationScheduler)
                .map(new Function<Long, CpuInfo>() {
                    @Override
                    public CpuInfo apply(Long aLong) throws Exception {
                        ThreadUtil.ensureWorkThread("CpuEngine apply");
                        return CpuUsage.getCpuInfo();
                    }
                })
                .filter(new Predicate<CpuInfo>() {
                            @Override
                            public boolean test(CpuInfo cpuInfo) throws Exception {
                                return CpuInfo.INVALID != cpuInfo;
                            }
                        }
                )
                .subscribe(new Consumer<CpuInfo>() {
                    @Override
                    public void accept(CpuInfo food) throws Exception {
                        ThreadUtil.ensureWorkThread("CpuEngine accept");
                        mProducer.produce(food);
                    }
                }));
    }

    @Override
    public void shutdown() {
        mCompositeDisposable.dispose();
    }
}

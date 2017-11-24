package cn.hikyson.godeye.internal.modules.traffic;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.internal.Producer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class TrafficEngine {
    private Producer<TrafficInfo> mProducer;
    private long mIntervalMillis;
    private long mSampleMillis;
    private CompositeDisposable mCompositeDisposable;

    public TrafficEngine(Producer<TrafficInfo> producer, long intervalMillis, long sampleMillis) {
        mProducer = producer;
        mIntervalMillis = intervalMillis;
        mSampleMillis = sampleMillis;
        mCompositeDisposable = new CompositeDisposable();
    }

    public void work() {
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS).
                concatMap(new Function<Long, ObservableSource<TrafficInfo>>() {
                    @Override
                    public ObservableSource<TrafficInfo> apply(Long aLong) throws Exception {
                        return create();
                    }
                }).subscribe(new Consumer<TrafficInfo>() {
            @Override
            public void accept(TrafficInfo food) throws Exception {
                mProducer.produce(food);
            }
        }));
    }

    public void shutdown() {
        mCompositeDisposable.dispose();
    }

    public Observable<TrafficInfo> create() {
        final TrafficSnapshot start = TrafficSnapshot.snapshot();
        return Observable.timer(mSampleMillis, TimeUnit.MILLISECONDS).map(new Function<Long, TrafficInfo>() {
            @Override
            public TrafficInfo apply(Long aLong) throws Exception {
                TrafficSnapshot endTrafficSnapshot = TrafficSnapshot.snapshot();
                TrafficInfo trafficInfo = new TrafficInfo();
                trafficInfo.rxTotalRate = (endTrafficSnapshot.rxTotalKB - start.rxTotalKB) * 1000 / mSampleMillis;
                trafficInfo.txTotalRate = (endTrafficSnapshot.txTotalKB - start.txTotalKB) * 1000 / mSampleMillis;
                trafficInfo.rxUidRate = (endTrafficSnapshot.rxUidKB - start.rxUidKB) * 1000 / mSampleMillis;
                trafficInfo.txUidRate = (endTrafficSnapshot.txUidKB - start.txUidKB) * 1000 / mSampleMillis;
                return trafficInfo;
            }
        });
    }
}

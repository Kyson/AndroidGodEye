package cn.hikyson.godeye.core.internal.modules.cpu;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.internal.exception.GodEyeInvalidDataException;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class CpuEngine implements Engine {
    private Producer<CpuInfo> mProducer;
    private long mIntervalMillis;
    private long mSampleMillis;
    private CompositeDisposable mCompositeDisposable;

    public CpuEngine(Producer<CpuInfo> producer, long intervalMillis, long sampleMillis) {
        mProducer = producer;
        mIntervalMillis = intervalMillis;
        mSampleMillis = sampleMillis;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void work() {
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS).
                concatMap(new Function<Long, ObservableSource<CpuInfo>>() {
                    @Override
                    public ObservableSource<CpuInfo> apply(Long aLong) throws Exception {
                        ThreadUtil.ensureWorkThread("cpu");
                        return create();
                    }
                }).subscribe(new Consumer<CpuInfo>() {
            @Override
            public void accept(CpuInfo food) throws Exception {
                mProducer.produce(food);
            }
        }));
    }

    @Override
    public void shutdown() {
        mCompositeDisposable.dispose();
    }

    private Observable<CpuInfo> create() {
        final CpuSnapshot startSnapshot = CpuSnapshot.snapshot();
        return Observable.timer(mSampleMillis, TimeUnit.MILLISECONDS).map(new Function<Long, CpuInfo>() {
            @Override
            public CpuInfo apply(Long aLong) throws Exception {
                CpuSnapshot endSnapshot = CpuSnapshot.snapshot();
                float totalTime = (endSnapshot.total - startSnapshot.total) * 1.0f;
                if (totalTime <= 0) {
                    throw new GodEyeInvalidDataException("totalTime must greater than 0");
                }
                long idleTime = endSnapshot.idle - startSnapshot.idle;
                double totalRatio = (totalTime - idleTime) / totalTime;
                double appRatio = (endSnapshot.app - startSnapshot.app) / totalTime;
                double userRatio = (endSnapshot.user - startSnapshot.user) / totalTime;
                double systemRatio = (endSnapshot.system - startSnapshot.system) / totalTime;
                double ioWaitRatio = (endSnapshot.ioWait - startSnapshot.ioWait) / totalTime;
                if (!isValidRatios(totalRatio, appRatio, userRatio, systemRatio, ioWaitRatio)) {
                    throw new GodEyeInvalidDataException("not valid ratio");
                }
                return new CpuInfo(totalRatio, appRatio, userRatio, systemRatio, ioWaitRatio);
            }
        });
    }

    private boolean isValidRatios(Double... ratios) {
        for (double ratio : ratios) {
            if (ratio < 0 || ratio > 1) {
                return false;
            }
        }
        return true;
    }
}

package cn.hikyson.godeye.internal.modules.cpu;


import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.internal.ProduceableConsumer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by kysonchao on 2017/5/19.
 */
public class Cpu extends ProduceableConsumer<CpuInfo> {
    private CpuEngine mCpuEngine;

    public Cpu(long intervalMillis, long sampleMillis) {
        mCpuEngine = new CpuEngine(this, intervalMillis, sampleMillis);
    }

    public void work() {
        mCpuEngine.work();
    }

    public void shutdown() {
        mCpuEngine.shutdown();
    }


//    private static final long DEFAULT_SNAPSHOT_INTERVAL = 1000;
//
//    private long mSnapshotInterval = DEFAULT_SNAPSHOT_INTERVAL;
//
//    public Cpu(long snapshotIntervalMillis) {
//        if (snapshotIntervalMillis <= 0) {
//            L.onRuntimeException(new IllegalArgumentException("snapshotIntervalMillis can not be less than 0"));
//        }
//        mSnapshotInterval = snapshotIntervalMillis;
//    }
//
//    public Cpu() {
//    }
//
//    @Override
//    public Observable<CpuInfo> consume() {
//        final CpuSnapshot startSnapshot = CpuSnapshot.snapshot();
//        return Observable.timer(intervalMillis(), TimeUnit.MILLISECONDS).map(new Function<Long, CpuInfo>() {
//            @Override
//            public CpuInfo apply(Long aLong) throws Exception {
//                CpuSnapshot endSnapshot = CpuSnapshot.snapshot();
//                if (startSnapshot == CpuSnapshot.INVALID || endSnapshot == CpuSnapshot.INVALID) {
//                    return CpuInfo.INVALID;
//                }
//                float totalTime = (endSnapshot.total - startSnapshot.total) * 1.0f;
//                if (totalTime <= 0) {
//                    return CpuInfo.INVALID;
//                }
//                long idleTime = endSnapshot.idle - startSnapshot.idle;
//                double totalRatio = (totalTime - idleTime) / totalTime;
//                double appRatio = (endSnapshot.app - startSnapshot.app) / totalTime;
//                double userRatio = (endSnapshot.user - startSnapshot.user) / totalTime;
//                double systemRatio = (endSnapshot.system - startSnapshot.system) / totalTime;
//                double ioWaitRatio = (endSnapshot.ioWait - startSnapshot.ioWait) / totalTime;
//                if (!isValidRatios(totalRatio, appRatio, userRatio, systemRatio, ioWaitRatio)) {
//                    return CpuInfo.INVALID;
//                }
//                return new CpuInfo(totalRatio, appRatio, userRatio, systemRatio, ioWaitRatio);
//            }
//        });
//    }
//
//
//    private boolean isValidRatios(Double... ratios) {
//        for (double ratio : ratios) {
//            if (ratio < 0 || ratio > 1) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//
//    @Override
//    public long intervalMillis() {
//        return mSnapshotInterval;
//    }
}

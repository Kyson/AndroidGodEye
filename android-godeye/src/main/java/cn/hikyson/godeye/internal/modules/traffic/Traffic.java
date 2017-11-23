package cn.hikyson.godeye.internal.modules.traffic;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.internal.Interval;
import cn.hikyson.godeye.utils.L;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 流量信息获取
 * Created by kysonchao on 2017/5/22.
 */
public class Traffic implements Interval, Snapshotable<TrafficInfo> {

    private static final long DEFAULT_SNAPSHOT_INTERVAL = 1000;

    private long mSnapshotInterval = DEFAULT_SNAPSHOT_INTERVAL;

    public Traffic(long snapshotIntervalMillis) {
        if (snapshotIntervalMillis <= 0) {
            L.onRuntimeException(new IllegalArgumentException("snapshotIntervalMillis can not be less than 0"));
        }
        mSnapshotInterval = snapshotIntervalMillis;
    }

    @Override
    public long intervalMillis() {
        return mSnapshotInterval;
    }

    @Override
    public Observable<TrafficInfo> snapshot() {
        final TrafficSnapshot start = TrafficSnapshot.snapshot();
        final long interval = intervalMillis();
        return Observable.timer(interval, TimeUnit.MILLISECONDS).map(new Function<Long, TrafficInfo>() {
            @Override
            public TrafficInfo apply(Long aLong) throws Exception {
                TrafficSnapshot endTrafficSnapshot = TrafficSnapshot.snapshot();
                TrafficInfo trafficInfo = new TrafficInfo();
                trafficInfo.rxTotalRate = (endTrafficSnapshot.rxTotalKB - start.rxTotalKB) * 1000 / interval;
                trafficInfo.txTotalRate = (endTrafficSnapshot.txTotalKB - start.txTotalKB) * 1000 / interval;
                trafficInfo.rxUidRate = (endTrafficSnapshot.rxUidKB - start.rxUidKB) * 1000 / interval;
                trafficInfo.txUidRate = (endTrafficSnapshot.txUidKB - start.txUidKB) * 1000 / interval;
                return trafficInfo;
            }
        });
    }
}

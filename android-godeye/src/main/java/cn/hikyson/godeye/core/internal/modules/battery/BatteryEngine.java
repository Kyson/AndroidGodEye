package cn.hikyson.godeye.core.internal.modules.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.internal.exception.GodEyeInvalidDataException;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class BatteryEngine implements Engine {
    private Context mContext;
    private Producer<BatteryInfo> mProducer;
    private long mIntervalMillis;
    private CompositeDisposable mCompositeDisposable;

    public BatteryEngine(Context context, Producer<BatteryInfo> producer, long intervalMillis) {
        this.mContext = context;
        mProducer = producer;
        mIntervalMillis = intervalMillis;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void work() {
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS).
                concatMap(new Function<Long, ObservableSource<BatteryInfo>>() {
                    @Override
                    public ObservableSource<BatteryInfo> apply(Long aLong) throws Exception {
                        return create();
                    }
                }).subscribe(new Consumer<BatteryInfo>() {
            @Override
            public void accept(BatteryInfo food) throws Exception {
                if(food == BatteryInfo.INVALID){
                    return;
                }
                mProducer.produce(food);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                L.e(String.valueOf(throwable));
            }
        }));
    }

    @Override
    public void shutdown() {
        mCompositeDisposable.dispose();
    }

    private Observable<BatteryInfo> create() {
        return Observable.fromCallable(new Callable<BatteryInfo>() {
            @Override
            public BatteryInfo call() throws Exception {
                return getBatteryInfo(mContext);
            }
        });
    }

    private static final class BatteryIntentFilterHolder {
        private static final IntentFilter BATTERY_INTENT_FILTER = new IntentFilter();

        static {
            BATTERY_INTENT_FILTER.addAction(Intent.ACTION_BATTERY_CHANGED);
            BATTERY_INTENT_FILTER.addAction(Intent.ACTION_BATTERY_LOW);
            BATTERY_INTENT_FILTER.addAction(Intent.ACTION_BATTERY_OKAY);
        }
    }

    /**
     * 获取电池信息
     *
     * @param context
     * @return 获取电池信息，或者invalid如果获取失败
     */
    private static BatteryInfo getBatteryInfo(Context context) {
        try {
            Intent batteryInfoIntent = context.registerReceiver(null, BatteryIntentFilterHolder.BATTERY_INTENT_FILTER);
            if (batteryInfoIntent == null) {
                L.e("can not registerReceiver for battery");
                return BatteryInfo.INVALID;
            }
            BatteryInfo batteryInfo = new BatteryInfo();
            batteryInfo.status = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager
                    .BATTERY_STATUS_UNKNOWN);
            batteryInfo.health = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager
                    .BATTERY_HEALTH_UNKNOWN);
            batteryInfo.present = batteryInfoIntent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
            batteryInfo.level = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryInfo.scale = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            batteryInfo.plugged = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            batteryInfo.voltage = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            batteryInfo.temperature = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            batteryInfo.technology = batteryInfoIntent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            return batteryInfo;
        } catch (Throwable e) {
            L.e(String.valueOf(e));
            return BatteryInfo.INVALID;
        }
    }
}

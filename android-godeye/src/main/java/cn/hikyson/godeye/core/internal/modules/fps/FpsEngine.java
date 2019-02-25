package cn.hikyson.godeye.core.internal.modules.fps;

import android.content.Context;
import android.os.Looper;
import android.view.Choreographer;
import android.view.Display;
import android.view.WindowManager;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class FpsEngine implements Engine {
    private Producer<FpsInfo> mProducer;
    private long mIntervalMillis;
    private CompositeDisposable mCompositeDisposable;
    private FpsMonitor mFpsMonitor;
    private final double mSystemRate;

    public FpsEngine(Context context, Producer<FpsInfo> producer, long intervalMillis) {
        mProducer = producer;
        mIntervalMillis = intervalMillis;
        mSystemRate = getRefreshRate(context);
        mFpsMonitor = new FpsMonitor();
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void work() {
        mFpsMonitor.start();
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        double fps = mFpsMonitor.exportThenReset();
                        mProducer.produce(new FpsInfo(fps, mSystemRate));
                    }
                }));
    }

    @Override
    public void shutdown() {
        mCompositeDisposable.dispose();
        mFpsMonitor.stop();
    }

    /**
     * 每秒理论刷新次数
     *
     * @param context
     * @return
     */
    private static double getRefreshRate(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getRefreshRate();
    }
}

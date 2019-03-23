package cn.hikyson.godeye.core.internal.modules.fps;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;

import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class FpsEngine implements Engine {
    private Producer<FpsInfo> mProducer;
    private long mIntervalMillis;
    private CompositeDisposable mCompositeDisposable;
    private FpsMonitor mFpsMonitor;
    private final int mSystemRate;

    public FpsEngine(Context context, Producer<FpsInfo> producer, long intervalMillis) {
        mProducer = producer;
        mIntervalMillis = intervalMillis;
        mSystemRate = getRefreshRate(context);
        mFpsMonitor = new FpsMonitor();
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void work() {
        ThreadUtil.ensureMainThread("FpsEngine work");
        mFpsMonitor.start();
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS)
                .observeOn(ThreadUtil.sComputationScheduler)
                .subscribeOn(ThreadUtil.sComputationScheduler)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        ThreadUtil.ensureWorkThread("FpsEngine accept");
                        int fps = mFpsMonitor.exportThenReset();
                        mProducer.produce(new FpsInfo(fps, mSystemRate));
                    }
                }));
    }

    @Override
    public void shutdown() {
        ThreadUtil.ensureMainThread("FpsEngine work");
        mCompositeDisposable.dispose();
        mFpsMonitor.stop();
    }

    /**
     * 每秒理论刷新次数
     *
     * @param context
     * @return
     */
    private static int getRefreshRate(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return Math.round(display.getRefreshRate());
    }
}

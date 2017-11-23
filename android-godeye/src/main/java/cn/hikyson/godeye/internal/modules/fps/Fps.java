package cn.hikyson.godeye.internal.modules.fps;

import android.content.Context;
import android.view.Choreographer;
import android.view.Display;
import android.view.WindowManager;

import cn.hikyson.godeye.utils.L;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class Fps implements Snapshotable<FpsInfo> {
    private Context mContext;

    public Fps(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public Observable<FpsInfo> snapshot() {
        final float systemRate = getRefreshRate(mContext);
        final Choreographer choreographer = Choreographer.getInstance();
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(final ObservableEmitter<Long> e) throws Exception {
                choreographer.postFrameCallback(new Choreographer.FrameCallback() {
                    @Override
                    public void doFrame(long frameTimeNanos) {
                        L.d("start:" + frameTimeNanos);
                        e.onNext(frameTimeNanos);
                    }
                });
            }
        }).concatMap(new Function<Long, ObservableSource<FpsInfo>>() {
            @Override
            public ObservableSource<FpsInfo> apply(final Long startTimeNanos) throws Exception {
                L.d("got start:" + startTimeNanos);
                return Observable.create(new ObservableOnSubscribe<FpsInfo>() {
                    @Override
                    public void subscribe(final ObservableEmitter<FpsInfo> e) throws Exception {
                        choreographer.postFrameCallback(new Choreographer.FrameCallback() {
                            @Override
                            public void doFrame(long frameTimeNanos) {
                                L.d("got end:" + frameTimeNanos);
                                long frameInterval = frameTimeNanos - startTimeNanos;//计算两帧的时间间隔
                                e.onNext(new FpsInfo((float) (1000000000 / frameInterval), systemRate));
                            }
                        });
                    }
                });
            }
        });
    }

    private static float getRefreshRate(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getRefreshRate();
    }
}

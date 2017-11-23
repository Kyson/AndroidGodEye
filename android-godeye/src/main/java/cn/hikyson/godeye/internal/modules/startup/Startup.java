package cn.hikyson.godeye.internal.modules.startup;

import cn.hikyson.godeye.internal.Eater;
import cn.hikyson.godeye.internal.Consumer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class Startup implements Eater<StartupInfo>, Consumer<StartupInfo> {

    private ObservableEmitter<StartupInfo> mStartupInfoObservableEmitter;

    @Override
    public void produce(StartupInfo data) {
        if (mStartupInfoObservableEmitter == null || mStartupInfoObservableEmitter.isDisposed()) {
            return;
        }
        mStartupInfoObservableEmitter.onNext(data);
    }

    @Override
    public Observable<StartupInfo> consume() {
        return Observable.create(new ObservableOnSubscribe<StartupInfo>() {
            @Override
            public void subscribe(ObservableEmitter<StartupInfo> e) throws Exception {
                mStartupInfoObservableEmitter = e;
            }
        });
    }
}

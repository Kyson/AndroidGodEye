package cn.hikyson.godeye.internal.modules.network;

import cn.hikyson.godeye.internal.Producer;
import cn.hikyson.godeye.internal.Consumer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


/**
 * Created by kysonchao on 2017/11/22.
 */
public class Network<T extends RequestBaseInfo> implements Producer<T>, Consumer<T> {
    private ObservableEmitter<T> mObservableEmitter;

    @Override
    public Observable<T> consume() {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                mObservableEmitter = e;
            }
        });
    }

    @Override
    public void produce(T data) {
        if (mObservableEmitter == null || mObservableEmitter.isDisposed()) {
            return;
        }
        mObservableEmitter.onNext(data);
    }
}

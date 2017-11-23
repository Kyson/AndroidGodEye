package cn.hikyson.godeye.internal;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class ProduceableConsumer<T> implements Consumer<T>, Producer<T> {
    private ObservableEmitter<T> mObservableEmitter;

    @Override
    public void produce(T data) {
        if (mObservableEmitter == null || mObservableEmitter.isDisposed()) {
            return;
        }
        mObservableEmitter.onNext(data);
    }

    @Override
    public Observable<T> consume() {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                mObservableEmitter = e;
            }
        });
    }
}

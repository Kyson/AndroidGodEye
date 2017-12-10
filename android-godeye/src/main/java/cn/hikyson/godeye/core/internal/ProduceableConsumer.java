package cn.hikyson.godeye.core.internal;

import cn.hikyson.godeye.core.utils.L;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class ProduceableConsumer<T> implements Consumer<T>, Producer<T> {
    private Subject<T> mSubject;

    public ProduceableConsumer() {
        mSubject = PublishSubject.create();
    }

    @Override
    public void produce(T data) {
        mSubject.onNext(data);
    }

    @Override
    public Observable<T> consume() {
        return mSubject;
    }
}

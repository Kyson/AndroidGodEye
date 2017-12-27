package cn.hikyson.godeye.core.internal;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class ProduceableSubject<T> implements SubjectSupport<T>, Producer<T> {
    private Subject<T> mSubject;

    public ProduceableSubject() {
        mSubject = createSubject();
    }

    protected Subject<T> createSubject() {
        return PublishSubject.create();
    }

    @Override
    public void produce(T data) {
        mSubject.onNext(data);
    }

    @Override
    public Observable<T> subject() {
        return mSubject;
    }
}

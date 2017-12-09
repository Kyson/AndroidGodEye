package cn.hikyson.android.godeye.sample;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by kysonchao on 2017/12/9.
 */

public class LogObserver<T> extends DisposableObserver<T> {
    private String mName;
    private Loggable mLoggable;

    public LogObserver(String name, Loggable loggable) {
        this.mName = name;
        this.mLoggable = loggable;
    }

    @Override
    public void onNext(T t) {
        mLoggable.log("DEBUG: " + mName + " , " + String.valueOf(t));
    }

    @Override
    public void onError(Throwable e) {
        mLoggable.log("!ERROR: " + mName + " , " + String.valueOf(e));
    }

    @Override
    public void onComplete() {
        mLoggable.log("DEBUG: " + mName + " , onComplete.");
    }
}

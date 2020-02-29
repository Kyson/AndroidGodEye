package cn.hikyson.godeye.sample;

import io.reactivex.functions.Consumer;

/**
 * Created by kysonchao on 2017/12/9.
 */

public class LogObserver<T> implements Consumer<T> {
    private String mName;
    private Loggable mLoggable;

    public LogObserver(String name, Loggable loggable) {
        this.mName = name;
        this.mLoggable = loggable;
    }

    @Override
    public void accept(T t) throws Exception {
        mLoggable.log(mName + " , " + t);
    }
}

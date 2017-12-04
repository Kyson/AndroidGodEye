package cn.hikyson.godeye.core.internal;

import io.reactivex.Observable;

/**
 * Created by kysonchao on 2017/11/22.
 */
public interface Consumer<T> {
    Observable<T> consume();
}

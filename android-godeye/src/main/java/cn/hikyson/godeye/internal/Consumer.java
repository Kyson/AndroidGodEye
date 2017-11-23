package cn.hikyson.godeye.internal;

import android.content.Context;

import io.reactivex.Observable;

/**
 * Created by kysonchao on 2017/11/22.
 */
public interface Consumer<T> {
    Observable<T> consume();
}

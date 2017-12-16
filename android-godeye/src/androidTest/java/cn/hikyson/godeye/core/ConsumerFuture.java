package cn.hikyson.godeye.core;

import java.util.concurrent.CompletableFuture;

import io.reactivex.functions.Consumer;

/**
 * Created by kysonchao on 2017/12/16.
 */
public class ConsumerFuture<T> extends CompletableFuture<T> implements Consumer<T> {

    @Override
    public void accept(T t) throws Exception {
        complete(t);
    }
}

package cn.hikyson.godeye.core.internal;

/**
 * Created by kysonchao on 2017/11/23.
 */
public interface Producer<T> {
    void produce(T data);
}

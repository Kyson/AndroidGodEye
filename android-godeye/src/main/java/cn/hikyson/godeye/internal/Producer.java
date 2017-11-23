package cn.hikyson.godeye.internal;

/**
 * Created by kysonchao on 2017/11/23.
 */
public interface Producer<T> {
    void produce(T data);
}

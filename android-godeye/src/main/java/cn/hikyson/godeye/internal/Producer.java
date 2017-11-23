package cn.hikyson.godeye.internal;

/**
 * 数据生产者，对于部分不能自主生产数据的类型有效
 * Created by kysonchao on 2017/11/21.
 */
public interface Producer<T> {
    void produce(T data);
}

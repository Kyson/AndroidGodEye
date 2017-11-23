package cn.hikyson.godeye.internal.engines;

import cn.hikyson.godeye.internal.Eater;
import cn.hikyson.godeye.internal.Producer;

/**
 * Created by kysonchao on 2017/11/23.
 */
public abstract class EngineImpl<T, C> implements Engine, Producer<T> {
    private Eater<T> mEater;

    public EngineImpl(Eater<T> eater) {
        mEater = eater;
    }

    @Override
    public void eat(T data) {
        mEater.eat(data);
    }
}

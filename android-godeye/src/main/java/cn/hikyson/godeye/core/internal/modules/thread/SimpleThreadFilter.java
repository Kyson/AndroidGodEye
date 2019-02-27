package cn.hikyson.godeye.core.internal.modules.thread;

import android.support.annotation.Keep;

/**
 * 全部展示
 * Created by kysonchao on 2018/1/15.
 */
@Keep
public class SimpleThreadFilter implements ThreadFilter {

    @Override
    public boolean filter(Thread thread) {
        return true;
    }
}

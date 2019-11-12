package cn.hikyson.godeye.core.internal.modules.thread;

import android.support.annotation.Keep;

/**
 * 不需要这个类，默认使用 {@link ExcludeSystemThreadFilter}
 * 全部展示
 * Created by kysonchao on 2018/1/15.
 */
@Keep
@Deprecated
public class SimpleThreadFilter implements ThreadFilter {

    @Override
    public boolean filter(Thread thread) {
        return true;
    }
}

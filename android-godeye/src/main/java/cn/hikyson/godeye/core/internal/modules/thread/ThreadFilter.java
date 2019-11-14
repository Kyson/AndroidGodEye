package cn.hikyson.godeye.core.internal.modules.thread;

import androidx.annotation.Keep;

/**
 * Created by kysonchao on 2018/1/15.
 */
@Keep
public interface ThreadFilter {
    boolean filter(Thread thread);
}

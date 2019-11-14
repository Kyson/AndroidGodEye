package cn.hikyson.godeye.core.internal.modules.thread;

import androidx.annotation.Keep;

/**
 * Created by kysonchao on 2017/11/24.
 */
@Keep
public interface ThreadContext {

    long intervalMillis();

    // ThreadFilter
    String threadFilter();
}

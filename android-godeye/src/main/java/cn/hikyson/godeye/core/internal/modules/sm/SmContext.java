package cn.hikyson.godeye.core.internal.modules.sm;

import android.content.Context;

/**
 * Created by kysonchao on 2017/11/24.
 */
public interface SmContext {
    Context context();

    boolean debugNotify();

    //长卡顿阀值
    long longBlockThreshold();

    //短卡顿阀值
    long shortBlockThreshold();

    //dump信息的间隔
    long dumpInterval();
}

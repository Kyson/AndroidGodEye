package cn.hikyson.godeye.core.internal.modules.thread;

import androidx.annotation.Keep;

/**
 * 排序系统thread组的线程
 * Created by kysonchao on 2018/1/15.
 */
@Keep
public class ExcludeSystemThreadFilter implements ThreadFilter {

    @Override
    public boolean filter(Thread thread) {
        if (thread == null) {
            return false;
        }
        if (thread.getThreadGroup() == null) {
            return true;
        }
        return !"system".equals(thread.getThreadGroup().getName());
    }
}

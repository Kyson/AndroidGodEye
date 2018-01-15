package cn.hikyson.godeye.core.internal.modules.thread;

/**
 * Created by kysonchao on 2017/11/24.
 */
public class ThreadContextImpl implements ThreadContext {

    @Override
    public long intervalMillis() {
        return 2000;
    }

    @Override
    public ThreadFilter threadFilter() {
        return new ExcludeSystemThreadFilter();
    }
}

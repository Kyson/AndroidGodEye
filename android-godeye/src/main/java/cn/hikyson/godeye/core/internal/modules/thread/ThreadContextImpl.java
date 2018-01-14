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
    public ThreadEngine.ThreadFilter threadFilter() {
        return new ThreadEngine.ThreadFilter() {
            @Override
            public boolean filter(Thread thread) {
                return true;
            }
        };
    }
}

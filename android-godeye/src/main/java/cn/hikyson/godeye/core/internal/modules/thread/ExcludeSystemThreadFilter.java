package cn.hikyson.godeye.core.internal.modules.thread;

/**
 * Created by kysonchao on 2018/1/15.
 */
public class ExcludeSystemThreadFilter implements ThreadFilter {

    @Override
    public boolean filter(Thread thread) {
        if ("system".equals(thread.getThreadGroup().getName())) {
            return false;
        }
        return true;
    }
}

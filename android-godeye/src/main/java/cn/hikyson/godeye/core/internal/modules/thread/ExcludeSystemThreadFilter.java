package cn.hikyson.godeye.core.internal.modules.thread;

/**
 * 排序系统thread组的线程
 * Created by kysonchao on 2018/1/15.
 */
public class ExcludeSystemThreadFilter implements ThreadFilter {

    @Override
    public boolean filter(Thread thread) {
        return !"system".equals(thread.getThreadGroup().getName());
    }
}

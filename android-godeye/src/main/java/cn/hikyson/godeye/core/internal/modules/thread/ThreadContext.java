package cn.hikyson.godeye.core.internal.modules.thread;

/**
 * Created by kysonchao on 2017/11/24.
 */
public interface ThreadContext {

    long intervalMillis();

    ThreadEngine.ThreadFilter threadFilter();
}

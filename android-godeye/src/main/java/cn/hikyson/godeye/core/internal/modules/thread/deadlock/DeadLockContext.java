package cn.hikyson.godeye.core.internal.modules.thread.deadlock;

import java.util.List;

import cn.hikyson.godeye.core.internal.modules.thread.ThreadFilter;
import io.reactivex.Observable;

/**
 * Created by kysonchao on 2018/1/14.
 */
public interface DeadLockContext {
    long intervalMillis();

    Observable<List<Thread>> threadInfoSubject();

    ThreadFilter threadFilter();
}

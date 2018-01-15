package cn.hikyson.godeye.core.internal.modules.thread.deadlock;

import java.util.List;

import cn.hikyson.godeye.core.internal.modules.thread.ThreadFilter;
import io.reactivex.Observable;

/**
 * Created by kysonchao on 2018/1/14.
 */
public class DeadLockContextImpl implements DeadLockContext {

    private Observable<List<Thread>> mListSubject;
    private ThreadFilter mThreadFilter;

    public DeadLockContextImpl(Observable<List<Thread>> listSubject, ThreadFilter threadFilter) {
        mListSubject = listSubject;
        mThreadFilter = threadFilter;
    }

    @Override
    public long intervalMillis() {
        return 8000;
    }

    @Override
    public Observable<List<Thread>> threadInfoSubject() {
        return mListSubject;
    }

    @Override
    public ThreadFilter threadFilter() {
        return mThreadFilter;
    }
}

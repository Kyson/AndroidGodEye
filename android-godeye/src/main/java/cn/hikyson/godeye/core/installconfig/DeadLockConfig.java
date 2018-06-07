//package cn.hikyson.godeye.core.installconfig;
//
//import java.util.List;
//
//import cn.hikyson.godeye.core.GodEye;
//import cn.hikyson.godeye.core.internal.modules.thread.deadlock.DeadLockContext;
//import cn.hikyson.godeye.core.internal.modules.thread.deadlock.DeadLockContextImpl;
//import cn.hikyson.godeye.core.internal.modules.thread.deadlock.DeadlockDefaultThreadFilter;
//import io.reactivex.Observable;
//
//public class DeadLockConfig implements InstallConfig<DeadLockContext> {
//
//    private Observable<List<Thread>> mListSubject;
//
//    public DeadLockConfig(Observable<List<Thread>> listSubject) {
//        mListSubject = listSubject;
//    }
//
//    @Override
//    public DeadLockContext getConfig() {
//        return new DeadLockContextImpl(mListSubject, new DeadlockDefaultThreadFilter());
//    }
//
//    @Override
//    public @GodEye.ModuleName
//    String getModule() {
//        return GodEye.ModuleName.DEADLOCK;
//    }
//}

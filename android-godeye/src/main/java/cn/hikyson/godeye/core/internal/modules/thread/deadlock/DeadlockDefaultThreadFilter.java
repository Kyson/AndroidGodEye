//package cn.hikyson.godeye.core.internal.modules.thread.deadlock;
//
//import cn.hikyson.godeye.core.internal.modules.thread.ThreadFilter;
//
///**
// * Created by kysonchao on 2018/1/19.
// */
//public class DeadlockDefaultThreadFilter implements ThreadFilter {
//    @Override
//    public boolean filter(Thread thread) {
//        if (thread == null) {
//            return false;
//        }
//        if ("LeakCanary-File-IO".equals(thread.getName())) {
//            return false;
//        }
//        if (thread.getThreadGroup() == null) {
//            return true;
//        }
//        if ("system".equals(thread.getThreadGroup().getName())) {
//            return false;
//        }
//        return true;
//    }
//}

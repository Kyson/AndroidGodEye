//package cn.hikyson.godeye.core.internal.modules.thread.deadlock;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by kysonchao on 2018/1/14.
// */
//public class DeadLockDetector {
//    private long mLastDetectTime;
//    private List<Thread> mLastThreadInfos;
//
//    /**
//     * 检测两次的线程状态
//     *
//     * @param threadInfos
//     * @return 如果有同一线程block和waiting在同一行代码，那么认为疑似死锁
//     */
//    public List<Thread> detect(List<Thread> threadInfos) {
//        List<Thread> blockThreads = new ArrayList<>();
//        if (mLastThreadInfos != null && !mLastThreadInfos.isEmpty()
//                && mLastDetectTime > 0
//                && threadInfos != null && !threadInfos.isEmpty()) {
//            for (Thread lti : mLastThreadInfos) {
//                for (Thread ti : threadInfos) {
//                    if (isBlockAtOneLine(lti, ti)) {
//                        blockThreads.add(ti);
//                    }
//                }
//            }
//        }
//        mLastDetectTime = System.currentTimeMillis();
//        mLastThreadInfos = threadInfos;
//        return blockThreads;
//    }
//
//    /**
//     * 两个线程是否相同且卡在同一堆栈代码
//     *
//     * @param lti
//     * @param rti
//     * @return
//     */
//    private boolean isBlockAtOneLine(Thread lti, Thread rti) {
//        if (lti.getId() != rti.getId()) {
//            return false;
//        }
//        if ((Thread.State.BLOCKED.equals(lti.getState()) && Thread.State.BLOCKED.equals(rti.getState()))
//                || (Thread.State.WAITING.equals(lti.getState()) && Thread.State.WAITING.equals(rti.getState()))) {
//            StackTraceElement[] lElements = lti.getStackTrace();
//            StackTraceElement[] rElements = rti.getStackTrace();
//            if (lElements != null && lElements.length > 0
//                    && rElements != null && rElements.length > 0
//                    && isArrayEqual(lElements, rElements)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean isArrayEqual(Object[] l, Object[] r) {
//        if (l.length != r.length) {
//            return false;
//        }
//        final int size = l.length;
//        for (int i = 0; i < size; i++) {
//            if (!l[i].equals(r[i])) {
//                return false;
//            }
//        }
//        return true;
//    }
//}

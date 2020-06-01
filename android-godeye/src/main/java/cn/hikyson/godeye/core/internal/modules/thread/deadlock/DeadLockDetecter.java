package cn.hikyson.godeye.core.internal.modules.thread.deadlock;

public class DeadLockDetecter {

    static {
        System.loadLibrary("native-lib");
    }

    public static native String stringFromJNI();

}

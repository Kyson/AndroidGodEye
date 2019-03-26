package cn.hikyson.godeye.core.internal.modules.deadlock;

import android.os.Process;

public class A {

    /**
     * adb pull data/anr/traces.txt /Users/xxx/Desktop
     * adb shell cd /data/anr
     * stat traces.txt
     */
    public static void send(){
        Process.sendSignal(Process.myPid(),Process.SIGNAL_QUIT);
    }

}

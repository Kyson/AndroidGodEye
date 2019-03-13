package cn.hikyson.godeye.core.helper;

import android.os.Debug;

public class AndroidDebug {
    private static boolean sIsDebug = true;

    /**
     * 是否正在debug
     *
     * @return
     */
    public static boolean isDebugging() {
        return sIsDebug && Debug.isDebuggerConnected();
    }

    public static void setIsDebug(boolean isDebug) {
        sIsDebug = isDebug;
    }
}

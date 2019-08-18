package cn.hikyson.godeye.core.helper;

import android.os.Debug;

public class AndroidDebug {
    /**
     * 是否正在debug
     *
     * @return
     */
    public static boolean isDebugging() {
        return Debug.isDebuggerConnected();
    }
}

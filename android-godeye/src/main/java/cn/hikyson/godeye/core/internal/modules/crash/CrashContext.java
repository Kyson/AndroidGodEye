package cn.hikyson.godeye.core.internal.modules.crash;

import android.content.Context;

public interface CrashContext {
    Context context();

    /**
     * output immediately when crash
     *
     * @return
     */
    boolean immediate();
}

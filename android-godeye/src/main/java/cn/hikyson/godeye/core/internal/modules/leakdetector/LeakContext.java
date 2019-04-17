package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Application;

/**
 * Created by kysonchao on 2017/11/24.
 */

public interface LeakContext {
    Application application();

    boolean enableRelease();
}

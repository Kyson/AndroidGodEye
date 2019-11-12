package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * Created by kysonchao on 2017/11/24.
 */
public interface LeakContext {
    @NonNull
    Application application();

    // if not debug, it will not dump hprof and analysis
    // NOTE: if you want leak module work in production,set debug false
    boolean debug();

    boolean debugNotification();

    //LeakRefInfoProvider
    @NonNull
    String leakRefInfoProvider();
}

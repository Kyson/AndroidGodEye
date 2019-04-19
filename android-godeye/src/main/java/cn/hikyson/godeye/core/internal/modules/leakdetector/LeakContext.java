package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * Created by kysonchao on 2017/11/24.
 */

public interface LeakContext {
    @NonNull
    Application application();

    boolean debug();

    boolean debugNotification();

    @NonNull
    LeakRefNameProvider leakRefNameProvider();
}

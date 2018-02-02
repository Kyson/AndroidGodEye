package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Application;

import io.reactivex.Observable;

/**
 * Created by kysonchao on 2017/11/24.
 */

public interface LeakContext {
    Application application();
    Observable<Boolean> permissionNeed(String permission);
}

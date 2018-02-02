package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Application;
import android.support.v4.content.PermissionChecker;

import java.util.concurrent.Callable;

import io.reactivex.Observable;

/**
 * Created by kysonchao on 2017/11/24.
 */
public class LeakContextImpl implements LeakContext {
    private Application mApplication;

    public LeakContextImpl(Application application) {
        mApplication = application;
    }

    @Override
    public Application application() {
        return mApplication;
    }

    @Override
    public Observable<Boolean> permissionNeed(final String permission) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return PermissionChecker.checkSelfPermission(mApplication, permission) == PermissionChecker.PERMISSION_GRANTED;
            }
        });
    }
}

package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Application;
import android.support.v4.content.PermissionChecker;

import java.util.concurrent.Callable;

import io.reactivex.Observable;

/**
 * 权限请求的简单实现，如果有权限则返回true，没有权限返回false，不给外部请求权限的接口了
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
    public Observable<Boolean> permissionNeed(final String... permissions) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                for (String p : permissions) {
                    if (PermissionChecker.checkSelfPermission(application(), p) != PermissionChecker.PERMISSION_GRANTED) {
                        return false;
                    }
                }
                return true;
            }
        });
    }
}

package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.helper.ActivityStackSubject;
import cn.hikyson.godeye.core.helper.PermissionRequest;
import cn.hikyson.godeye.core.helper.SimpleActivityLifecycleCallbacks;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 需要权限的时候的第一个打开的页面开始请求，权限请求过程外部实现
 * Created by kysonchao on 2017/11/24.
 */
public class LeakContextImpl2 implements LeakContext {
    private Application mApplication;
    private PermissionRequest mPermissionRequest;

    public LeakContextImpl2(Application application, PermissionRequest permissionRequest) {
        mApplication = application;
        mPermissionRequest = permissionRequest;
    }

    @Override
    public Application application() {
        return mApplication;
    }

    @Override
    public Observable<Boolean> permissionNeed(final String... permissions) {
        if (GodEye.instance().getActivityStackSubject() == null) {
            throw new RuntimeException("Please call GodEye.instance().init() first.");
        }
        return GodEye.instance().getActivityStackSubject().topActivityObservable().concatMap(new Function<Activity, ObservableSource<Boolean>>() {
            @Override
            public ObservableSource<Boolean> apply(Activity activity) throws Exception {
                return mPermissionRequest.dispatchRequest(activity, permissions);
            }
        });
    }
}

package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import cn.hikyson.godeye.core.helper.PermissionRequest;
import cn.hikyson.godeye.core.helper.SimpleActivityLifecycleCallbacks;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

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
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                mApplication.registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                        mPermissionRequest.dispatchRequest(activity, permissions).subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                e.onNext(aBoolean);
                                e.onComplete();
                            }
                        });
                        mApplication.unregisterActivityLifecycleCallbacks(this);
                    }
                });
            }
        });
    }
}

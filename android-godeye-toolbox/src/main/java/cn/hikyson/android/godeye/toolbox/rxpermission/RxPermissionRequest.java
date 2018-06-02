package cn.hikyson.android.godeye.toolbox.rxpermission;

import android.app.Activity;

import cn.hikyson.godeye.core.helper.PermissionRequest;
import io.reactivex.Observable;

public class RxPermissionRequest implements PermissionRequest {
    @Override
    public Observable<Boolean> dispatchRequest(Activity activity, String... permissions) {
        return new RxPermissions(activity).request(permissions);
    }
}

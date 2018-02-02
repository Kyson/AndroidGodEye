package cn.hikyson.godeye.core.helper;

import android.app.Activity;

import io.reactivex.Observable;

/**
 * Created by kysonchao on 2018/1/29.
 */
public interface PermissionRequest {
    Observable<Boolean> dispatchRequest(Activity activity, String... permissions);
}

package cn.hikyson.godeye.core.helper;

import io.reactivex.Observable;

/**
 * Created by kysonchao on 2018/2/3.
 */
public interface IPermissionNeed {
    Observable<Boolean> permissionNeed(final String... permissions);
}

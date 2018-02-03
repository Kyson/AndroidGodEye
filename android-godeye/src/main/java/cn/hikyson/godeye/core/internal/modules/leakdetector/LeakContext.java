package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Application;

import cn.hikyson.godeye.core.helper.IPermissionNeed;
import io.reactivex.Observable;

/**
 * Created by kysonchao on 2017/11/24.
 */

public interface LeakContext extends IPermissionNeed{
    Application application();
}

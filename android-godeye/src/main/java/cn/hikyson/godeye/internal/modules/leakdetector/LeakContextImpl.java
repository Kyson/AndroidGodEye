package cn.hikyson.godeye.internal.modules.leakdetector;

import android.app.Application;

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
}

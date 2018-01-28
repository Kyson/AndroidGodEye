package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Application;

/**
 * Created by kysonchao on 2018/1/25.
 */
public class PageloadContextImpl implements PageloadContext {
    private Application mApplication;

    public PageloadContextImpl(Application application) {
        mApplication = application;
    }

    @Override
    public Application application() {
        return mApplication;
    }
}

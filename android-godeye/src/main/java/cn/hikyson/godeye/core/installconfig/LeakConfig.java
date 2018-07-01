package cn.hikyson.godeye.core.installconfig;

import android.app.Application;
import android.content.Context;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.helper.PermissionRequest;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContext;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContextImpl;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakContext;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakContextImpl2;

public class LeakConfig implements InstallConfig<LeakContext> {
    private Application mApplication;
    private PermissionRequest mPermissionRequest;

    public LeakConfig(Application application, PermissionRequest permissionRequest) {
        mApplication = application;
        mPermissionRequest = permissionRequest;
    }

    @Override
    public LeakContext getConfig() {
        return new LeakContextImpl2(mApplication, mPermissionRequest);
    }

    @Override
    public @GodEye.ModuleName
    String getModule() {
        return GodEye.ModuleName.LEAK;
    }
}

package cn.hikyson.godeye.core.installconfig;

import android.app.Application;
import android.content.Context;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContext;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContextImpl;
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadContext;
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadContextImpl;

public class PageloadConfig implements InstallConfig<PageloadContext> {
    private Application mContext;

    public PageloadConfig(Application context) {
        mContext = context;
    }

    @Override
    public PageloadContext getConfig() {
        return new PageloadContextImpl(mContext);
    }

    @Override
    public @GodEye.ModuleName
    String getModule() {
        return GodEye.ModuleName.PAGELOAD;
    }
}

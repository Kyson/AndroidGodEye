package cn.hikyson.godeye.core.installconfig;

import android.content.Context;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContext;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContextImpl;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadContext;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadContextImpl;

public class ThreadConfig implements InstallConfig<ThreadContext> {

    @Override
    public ThreadContext getConfig() {
        return new ThreadContextImpl();
    }

    @Override
    public @GodEye.ModuleName
    String getModule() {
        return GodEye.ModuleName.THREAD;
    }
}

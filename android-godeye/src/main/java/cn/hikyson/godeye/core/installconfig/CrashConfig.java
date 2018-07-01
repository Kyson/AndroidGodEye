package cn.hikyson.godeye.core.installconfig;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.crash.CrashProvider;

public class CrashConfig implements InstallConfig<CrashProvider> {
    private CrashProvider mCrashProvider;

    public CrashConfig(CrashProvider crashProvider) {
        mCrashProvider = crashProvider;
    }

    @Override
    public CrashProvider getConfig() {
        return mCrashProvider;
    }

    @Override
    public @GodEye.ModuleName
    String getModule() {
        return GodEye.ModuleName.CRASH;
    }
}

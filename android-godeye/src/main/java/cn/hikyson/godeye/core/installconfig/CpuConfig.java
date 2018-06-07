package cn.hikyson.godeye.core.installconfig;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuContext;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuContextImpl;

public class CpuConfig implements InstallConfig<CpuContext> {
    @Override
    public CpuContext getConfig() {
        return new CpuContextImpl();
    }

    @Override
    public @GodEye.ModuleName
    String getModule() {
        return GodEye.ModuleName.CPU;
    }
}

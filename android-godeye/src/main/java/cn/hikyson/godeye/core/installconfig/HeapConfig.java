package cn.hikyson.godeye.core.installconfig;

import cn.hikyson.godeye.core.GodEye;

public class HeapConfig implements InstallConfig<Long> {

    @Override
    public Long getConfig() {
        return 2000L;
    }

    @Override
    public @GodEye.ModuleName
    String getModule() {
        return GodEye.ModuleName.HEAP;
    }
}

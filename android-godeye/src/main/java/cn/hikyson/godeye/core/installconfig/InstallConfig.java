package cn.hikyson.godeye.core.installconfig;

import cn.hikyson.godeye.core.GodEye;

public interface InstallConfig<T> {
    T getConfig();

    @GodEye.ModuleName
    String getModule();
}

package cn.hikyson.godeye.core;


import cn.hikyson.godeye.core.helper.Predicate;
import cn.hikyson.godeye.core.internal.modules.appsize.AppSizeInfo;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuInfo;

public interface GodEyeNotificationConfig {
    Predicate<AppSizeInfo> appSizePredicate();

    Predicate<CpuInfo> cpuPredicate();
}

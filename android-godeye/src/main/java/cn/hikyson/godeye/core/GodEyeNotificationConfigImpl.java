package cn.hikyson.godeye.core;


import androidx.annotation.NonNull;

import cn.hikyson.godeye.core.helper.Predicate;
import cn.hikyson.godeye.core.internal.modules.appsize.AppSizeInfo;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuInfo;

public class GodEyeNotificationConfigImpl implements GodEyeNotificationConfig {
    public Predicate<AppSizeInfo> appSizePredicate() {
        return new Predicate<AppSizeInfo>() {
            @Override
            public boolean test(AppSizeInfo appSizeInfo) {
                return appSizeInfo.dataSize > 100;
            }
        };
    }

    public Predicate<CpuInfo> cpuPredicate() {
        Predicate predicate = null;
        return new Predicate<CpuInfo>() {
            @Override
            public boolean test(@NonNull CpuInfo cpuInfo) throws Exception {
                return cpuInfo.appCpuRatio > 0.0001;
            }
        };
    }
}

package cn.hikyson.godeye.core.installconfig;

import android.content.Context;

import java.util.ConcurrentModificationException;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContext;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContextImpl;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuContext;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuContextImpl;

public class BatteryConfig implements InstallConfig<BatteryContext> {
    private Context mContext;

    public BatteryConfig(Context context) {
        mContext = context;
    }

    @Override
    public BatteryContext getConfig() {
        return new BatteryContextImpl(mContext);
    }

    @Override
    public @GodEye.ModuleName
    String getModule() {
        return GodEye.ModuleName.BATTERY;
    }
}

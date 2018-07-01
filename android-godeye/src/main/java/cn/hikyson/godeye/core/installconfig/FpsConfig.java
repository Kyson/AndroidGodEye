package cn.hikyson.godeye.core.installconfig;

import android.content.Context;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContext;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContextImpl;
import cn.hikyson.godeye.core.internal.modules.fps.FpsContext;
import cn.hikyson.godeye.core.internal.modules.fps.FpsContextImpl;

public class FpsConfig implements InstallConfig<FpsContext> {
    private Context mContext;

    public FpsConfig(Context context) {
        mContext = context;
    }

    @Override
    public FpsContext getConfig() {
        return new FpsContextImpl(mContext);
    }

    @Override
    public @GodEye.ModuleName
    String getModule() {
        return GodEye.ModuleName.FPS;
    }
}

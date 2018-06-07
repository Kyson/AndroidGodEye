package cn.hikyson.godeye.core.installconfig;

import android.content.Context;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContext;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContextImpl;
import cn.hikyson.godeye.core.internal.modules.memory.PssContext;
import cn.hikyson.godeye.core.internal.modules.memory.PssContextImpl;

public class PssConfig implements InstallConfig<PssContext> {
    private Context mContext;

    public PssConfig(Context context) {
        mContext = context;
    }

    @Override
    public PssContext getConfig() {
        return new PssContextImpl(mContext);
    }

    @Override
    public @GodEye.ModuleName
    String getModule() {
        return GodEye.ModuleName.PSS;
    }
}

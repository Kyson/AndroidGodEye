package cn.hikyson.godeye.core.installconfig;

import android.content.Context;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContext;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContextImpl;
import cn.hikyson.godeye.core.internal.modules.sm.SmContext;
import cn.hikyson.godeye.core.internal.modules.sm.SmContextImpl;

public class SmConfig implements InstallConfig<SmContext> {
    private Context mContext;

    public SmConfig(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public SmContext getConfig() {
        return new SmContextImpl(mContext);
    }

    @Override
    public @GodEye.ModuleName
    String getModule() {
        return GodEye.ModuleName.SM;
    }
}

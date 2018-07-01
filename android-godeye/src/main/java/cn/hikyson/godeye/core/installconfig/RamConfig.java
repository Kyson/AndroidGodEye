package cn.hikyson.godeye.core.installconfig;

import android.content.Context;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuContext;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuContextImpl;
import cn.hikyson.godeye.core.internal.modules.memory.RamContext;
import cn.hikyson.godeye.core.internal.modules.memory.RamContextImpl;

public class RamConfig implements InstallConfig<RamContext> {
    private Context mContext;

    public RamConfig(Context context) {
        mContext = context;
    }

    @Override
    public RamContext getConfig() {
        return new RamContextImpl(mContext);
    }

    @Override
    public @GodEye.ModuleName
    String getModule() {
        return GodEye.ModuleName.RAM;
    }
}

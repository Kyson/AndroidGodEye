package cn.hikyson.godeye.core.installconfig;



import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficContext;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficContextImpl;

public class TrafficConfig implements InstallConfig<TrafficContext> {

    @Override
    public TrafficContext getConfig() {
        return new TrafficContextImpl();
    }

    @Override
    public @GodEye.ModuleName
    String getModule() {
        return GodEye.ModuleName.TRAFFIC;
    }
}

package cn.hikyson.godeye.monitor.server;

import android.content.Context;
import android.net.Uri;
import android.support.v4.util.ArrayMap;

import java.util.Map;

import cn.hikyson.godeye.monitor.modules.AppInfoModule;
import cn.hikyson.godeye.monitor.modules.AssetsModule;
import cn.hikyson.godeye.monitor.modules.BatteryModule;
import cn.hikyson.godeye.monitor.modules.BlockModule;
import cn.hikyson.godeye.monitor.modules.CpuModule;
import cn.hikyson.godeye.monitor.modules.CrashModule;
import cn.hikyson.godeye.monitor.modules.FpsModule;
import cn.hikyson.godeye.monitor.modules.HeapModule;
import cn.hikyson.godeye.monitor.modules.LeakMemoryModule;
import cn.hikyson.godeye.monitor.modules.Module;
import cn.hikyson.godeye.monitor.modules.NetworkModule;
import cn.hikyson.godeye.monitor.modules.PssModule;
import cn.hikyson.godeye.monitor.modules.RamModule;
import cn.hikyson.godeye.monitor.modules.StartUpModule;
import cn.hikyson.godeye.monitor.modules.ThreadModule;
import cn.hikyson.godeye.monitor.modules.TrafficModule;

/**
 * Created by kysonchao on 2017/9/3.
 */
public class Router {
    private Map<String, Module> mRouteModules;

    private Router() {
    }

    private static class InstanceHolder {
        private static Router sInstance = new Router();
    }

    public static Router get() {
        return InstanceHolder.sInstance;
    }

    public void init(Context context) {
        mRouteModules = new ArrayMap<>();
        AssetsModule assetsModule = new AssetsModule(context, "androidgodeye");
        mRouteModules.put("assets", assetsModule);
        AppInfoModule appInfoModule = new AppInfoModule();
        mRouteModules.put("appinfo", appInfoModule);
        BatteryModule batteryModule = new BatteryModule();
        mRouteModules.put("battery", batteryModule);
        BlockModule blockModule = new BlockModule();
        mRouteModules.put("block", blockModule);
        CpuModule cpuModule = new CpuModule();
        mRouteModules.put("cpu", cpuModule);
        FpsModule fpsModule = new FpsModule();
        mRouteModules.put("fps", fpsModule);
        HeapModule heapModule = new HeapModule();
        mRouteModules.put("heap", heapModule);
        LeakMemoryModule LeakMemoryModule = new LeakMemoryModule();
        mRouteModules.put("leakMemory", LeakMemoryModule);
        NetworkModule networkModule = new NetworkModule();
        mRouteModules.put("network", networkModule);
        PssModule pssModule = new PssModule();
        mRouteModules.put("pss", pssModule);
        RamModule ramModule = new RamModule();
        mRouteModules.put("ram", ramModule);
        StartUpModule startUpModule = new StartUpModule();
        mRouteModules.put("startup", startUpModule);
        TrafficModule trafficModule = new TrafficModule();
        mRouteModules.put("traffic", trafficModule);
        ThreadModule threadModule = new ThreadModule();
        mRouteModules.put("thread", threadModule);
        CrashModule crashModule = new CrashModule();
        mRouteModules.put("crash", crashModule);
    }

    public byte[] process(Uri uri) throws Throwable {
        String moduleName = uri.getPath();
        Module module = mRouteModules.get(moduleName);
        if (module == null) {
            return mRouteModules.get("assets").process(moduleName, uri);
        }
        return module.process(moduleName, uri);
    }
}

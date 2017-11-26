package cn.hikyson.godeye.monitor.server;

import android.net.Uri;
import android.support.v4.util.ArrayMap;


import java.util.Map;

import cn.hikyson.godeye.monitor.modules.Module;

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

    public void init() {
        mRouteModules = new ArrayMap<>();
        StartUpModule startUpModule = new StartUpModule();
        AssetsModule assetsModule = new AssetsModule();
        CommandModule commandModule = new CommandModule();
        PageLoadModule pageLoadModule = new PageLoadModule();
        SessionTickModule sessionTickModule = new SessionTickModule();
        SessionEndModule sessionEndModule = new SessionEndModule();
        FpsModule fpsModule = new FpsModule();
        BlockModule blockModule = new BlockModule();
        LeakMemoryModule LeakMemoryModule = new LeakMemoryModule();
//        LeakMemoryStatusModule leakMemoryStautsModule = new LeakMemoryStatusModule();
        mRouteModules.put("startup", startUpModule);
        mRouteModules.put("assets", assetsModule);
        mRouteModules.put("command", commandModule);
        mRouteModules.put("pageLoad", pageLoadModule);
        mRouteModules.put("sessionTick", sessionTickModule);
        mRouteModules.put("sessionEnd", sessionEndModule);
        mRouteModules.put("fps", fpsModule);
        mRouteModules.put("block", blockModule);
        mRouteModules.put("leakMemory", LeakMemoryModule);
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

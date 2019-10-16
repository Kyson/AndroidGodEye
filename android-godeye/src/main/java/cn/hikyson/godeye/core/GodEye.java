package cn.hikyson.godeye.core;


import android.app.Application;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.modules.appsize.AppSize;
import cn.hikyson.godeye.core.internal.modules.battery.Battery;
import cn.hikyson.godeye.core.internal.modules.cpu.Cpu;
import cn.hikyson.godeye.core.internal.modules.crash.Crash;
import cn.hikyson.godeye.core.internal.modules.fps.Fps;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakDetector;
import cn.hikyson.godeye.core.internal.modules.memory.Heap;
import cn.hikyson.godeye.core.internal.modules.memory.Pss;
import cn.hikyson.godeye.core.internal.modules.memory.Ram;
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanary;
import cn.hikyson.godeye.core.internal.modules.network.Network;
import cn.hikyson.godeye.core.internal.modules.pageload.Pageload;
import cn.hikyson.godeye.core.internal.modules.sm.Sm;
import cn.hikyson.godeye.core.internal.modules.startup.Startup;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadDump;
import cn.hikyson.godeye.core.internal.modules.traffic.Traffic;

/**
 * 入口
 * install -> module.subject() -> uninstall
 * Created by kysonchao on 2017/11/22.
 */
public class GodEye {
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ModuleName.CPU, ModuleName.BATTERY, ModuleName.FPS, ModuleName.LEAK,
            ModuleName.HEAP, ModuleName.PSS, ModuleName.TRAFFIC, ModuleName.CRASH,
            ModuleName.THREAD, ModuleName.RAM, ModuleName.NETWORK, ModuleName.SM,
            ModuleName.STARTUP, ModuleName.DEADLOCK, ModuleName.PAGELOAD, ModuleName.METHOD_CANARY, ModuleName.APP_SIZE
    })
    public @interface ModuleName {
        public static final String CPU = "CPU";
        public static final String BATTERY = "BATTERY";
        public static final String FPS = "FPS";
        public static final String LEAK = "LEAK";
        public static final String HEAP = "HEAP";
        public static final String PSS = "PSS";
        public static final String RAM = "RAM";
        public static final String NETWORK = "NETWORK";
        public static final String SM = "SM";
        public static final String STARTUP = "STARTUP";
        public static final String TRAFFIC = "TRAFFIC";
        public static final String CRASH = "CRASH";
        public static final String THREAD = "THREAD";
        public static final String DEADLOCK = "DEADLOCK";
        public static final String PAGELOAD = "PAGELOAD";
        public static final String METHOD_CANARY = "METHOD_CANARY";
        public static final String APP_SIZE = "APP_SIZE";
    }

    private Application mApplication;

    private Map<String, Object> mModules = new HashMap<>();

    private GodEye() {
    }

    private static class InstanceHolder {
        private static final GodEye sInstance = new GodEye();
    }

    public static GodEye instance() {
        return InstanceHolder.sInstance;
    }


    /**
     * 初始化
     *
     * @param application
     */
    public void init(Application application) {
        mApplication = application;
        mModules.put(ModuleName.CPU, new Cpu());
        mModules.put(ModuleName.BATTERY, new Battery());
        mModules.put(ModuleName.FPS, new Fps());
        mModules.put(ModuleName.LEAK, LeakDetector.instance());
        mModules.put(ModuleName.HEAP, new Heap());
        mModules.put(ModuleName.PSS, new Pss());
        mModules.put(ModuleName.RAM, new Ram());
        mModules.put(ModuleName.NETWORK, new Network());
        mModules.put(ModuleName.SM, Sm.instance());
        mModules.put(ModuleName.STARTUP, new Startup());
        mModules.put(ModuleName.TRAFFIC, new Traffic());
        mModules.put(ModuleName.CRASH, new Crash());
        mModules.put(ModuleName.THREAD, new ThreadDump());
        mModules.put(ModuleName.PAGELOAD, new Pageload());
        mModules.put(ModuleName.METHOD_CANARY, new MethodCanary());
        mModules.put(ModuleName.APP_SIZE, new AppSize());
    }

    public GodEye install(final GodEyeConfig godEyeConfig) {
        if (godEyeConfig.getCpuConfig() != null) {
            ((Cpu) mModules.get(ModuleName.CPU)).install(godEyeConfig.getCpuConfig());
        }
        if (godEyeConfig.getBatteryConfig() != null) {
            ((Battery) mModules.get(ModuleName.BATTERY)).install(godEyeConfig.getBatteryConfig());
        }
        if (godEyeConfig.getFpsConfig() != null) {
            ((Fps) mModules.get(ModuleName.FPS)).install(godEyeConfig.getFpsConfig());
        }
        if (godEyeConfig.getLeakConfig() != null) {
            ((LeakDetector) mModules.get(ModuleName.LEAK)).install(godEyeConfig.getLeakConfig());
        }
        if (godEyeConfig.getHeapConfig() != null) {
            ((Heap) mModules.get(ModuleName.HEAP)).install(godEyeConfig.getHeapConfig());
        }
        if (godEyeConfig.getPssConfig() != null) {
            ((Pss) mModules.get(ModuleName.PSS)).install(godEyeConfig.getPssConfig());
        }
        if (godEyeConfig.getRamConfig() != null) {
            ((Ram) mModules.get(ModuleName.RAM)).install(godEyeConfig.getRamConfig());
        }
        if (godEyeConfig.getSmConfig() != null) {
            ((Sm) mModules.get(ModuleName.SM)).install(godEyeConfig.getSmConfig());
        }
        if (godEyeConfig.getTrafficConfig() != null) {
            ((Traffic) mModules.get(ModuleName.TRAFFIC)).install(godEyeConfig.getTrafficConfig());
        }
        if (godEyeConfig.getCrashConfig() != null) {
            ((Crash) mModules.get(ModuleName.CRASH)).install(godEyeConfig.getCrashConfig());
        }
        if (godEyeConfig.getThreadConfig() != null) {
            ((ThreadDump) mModules.get(ModuleName.THREAD)).install(godEyeConfig.getThreadConfig());
        }
        if (godEyeConfig.getPageloadConfig() != null) {
            ((Pageload) mModules.get(ModuleName.PAGELOAD)).install(godEyeConfig.getPageloadConfig());
        }
        if (godEyeConfig.getMethodCanaryConfig() != null) {
            ((MethodCanary) mModules.get(ModuleName.METHOD_CANARY)).install(godEyeConfig.getMethodCanaryConfig());
        }
        if (godEyeConfig.getAppSizeConfig() != null) {
            ((AppSize) mModules.get(ModuleName.APP_SIZE)).install(godEyeConfig.getAppSizeConfig());
        }
        return this;
    }

    /**
     * 卸载所有可卸载的模块
     *
     * @return
     */
    public GodEye uninstall() {
        for (Map.Entry<String, Object> entry : mModules.entrySet()) {
            if (entry.getValue() instanceof Install) {
                ((Install) entry.getValue()).uninstall();
            }
        }
        return this;
    }

    /**
     * 获取模块
     *
     * @param moduleName
     * @param <T>
     * @return
     */
    public <T> T getModule(@ModuleName String moduleName) {
        try {
            // noinspection unchecked
            return (T) mModules.get(moduleName);
        } catch (Throwable e) {
            throw new UnexpectException("module [" + moduleName + "] is not exist or type is wrong");
        }
    }

    public Application getApplication() {
        return mApplication;
    }
}

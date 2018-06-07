package cn.hikyson.godeye.core;


import android.app.Application;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

import cn.hikyson.godeye.core.helper.ActivityStackSubject;
import cn.hikyson.godeye.core.installconfig.CpuConfig;
import cn.hikyson.godeye.core.installconfig.InstallConfig;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.modules.battery.Battery;
import cn.hikyson.godeye.core.internal.modules.cpu.Cpu;
import cn.hikyson.godeye.core.internal.modules.crash.Crash;
import cn.hikyson.godeye.core.internal.modules.fps.Fps;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakDetector;
import cn.hikyson.godeye.core.internal.modules.memory.Heap;
import cn.hikyson.godeye.core.internal.modules.memory.Pss;
import cn.hikyson.godeye.core.internal.modules.memory.Ram;
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
            ModuleName.STARTUP, ModuleName.DEADLOCK, ModuleName.PAGELOAD
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
    }

    private GodEye() {
    }

    private static class InstanceHolder {
        private static final GodEye sInstance = new GodEye();
    }

    public static GodEye instance() {
        return InstanceHolder.sInstance;
    }

    private ActivityStackSubject mActivityStackSubject;

    private Map<String, Object> mModules = new HashMap<>();

    /**
     * 初始化
     *
     * @param application
     */
    public void init(Application application) {
        mActivityStackSubject = new ActivityStackSubject(application);
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
//        mModules.put(ModuleName.DEADLOCK, new DeadLock());
        mModules.put(ModuleName.PAGELOAD, new Pageload());
    }

    /**
     * 安装模块
     *
     * @param installConfig
     * @param <T>
     * @return
     */
    public <T> GodEye install(InstallConfig<T> installConfig) {
        if (installConfig == null) {
            throw new UnexpectException("can not install by InstallConfig null.");
        }
        if (TextUtils.isEmpty(installConfig.getModule())) {
            throw new UnexpectException("can not install by InstallConfig module null or empty.");
        }
        if (installConfig.getConfig() == null) {
            throw new UnexpectException("can not install by InstallConfig config null.");
        }
        Object moduleObj = mModules.get(installConfig.getModule());
        if (moduleObj == null) {
            throw new UnexpectException("can not find module [" + installConfig.getModule() + "] to install.");
        }
        try {
            // noinspection unchecked
            Install<T> installableModule = (Install<T>) moduleObj;
            installableModule.install(installConfig.getConfig());
        } catch (Throwable e) {
            throw new UnexpectException("module [" + moduleObj.getClass().getSimpleName() + "] is not installable, maybe no necessary to install?");
        }
        return this;
    }

    /**
     * 卸载所有可卸载的模块
     *
     * @return
     */
    public GodEye uninstallAll() {
        for (Map.Entry<String, Object> entry : mModules.entrySet()) {
            if (entry.getValue() instanceof Install) {
                ((Install) entry.getValue()).uninstall();
            }
        }
        return this;
    }

    /**
     * 卸载模块
     *
     * @param moduleName
     * @return
     */
    public GodEye uninstall(@ModuleName String moduleName) {
        Object moduleObj = mModules.get(moduleName);
        if (moduleObj == null) {
            throw new UnexpectException("can not find module [" + moduleName + "] to uninstall.");
        }
        try {
            Install installableModule = (Install) moduleObj;
            installableModule.uninstall();
        } catch (Throwable e) {
            throw new UnexpectException("module [" + moduleObj.getClass().getSimpleName() + "] is not uninstallable, maybe no necessary to uninstall?");
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

    public ActivityStackSubject getActivityStackSubject() {
        return mActivityStackSubject;
    }

//    @Deprecated
//    private Map<Class, Object> mCachedModules = new ArrayMap<>();
//
//    /**
//     * @param clz
//     * @param <T>
//     * @return
//     * @deprecated use {@link GodEye#getModule(String)} instead
//     */
//    @SuppressWarnings("unchecked")
//    public <T> T getModule(Class<T> clz) {
//        Object module = mCachedModules.get(clz);
//        if (module != null) {
//            if (!clz.isInstance(module)) {
//                throw new IllegalStateException(clz.getName() + " must be instance of " + String.valueOf(module));
//            }
//            return (T) module;
//        }
//        try {
//            T createdModule;
//            if (LeakDetector.class.equals(clz)) {
//                createdModule = (T) LeakDetector.instance();
//            } else if (Sm.class.equals(clz)) {
//                createdModule = (T) Sm.instance();
//            } else {
//                createdModule = clz.newInstance();
//            }
//            mCachedModules.put(clz, createdModule);
//            return createdModule;
//        } catch (Throwable e) {
//            throw new IllegalStateException("Can not create instance of " + clz.getName() + ", " + String.valueOf(e));
//        }
//    }
//
//    /**
//     * @param clz
//     * @param config
//     * @param <T>
//     * @return
//     * @deprecated use {@link GodEye#install(InstallConfig)} instead
//     */
//    public final <T> GodEye install(Class<? extends Install<T>> clz, T config) {
//        getModule(clz).install(config);
//        return this;
//    }
//
//    /**
//     * @param pairs
//     * @param <T>
//     * @return
//     * @deprecated use {@link GodEye#install(InstallConfig)} instead
//     */
//    @SafeVarargs
//    public final <T> GodEye install(Pair<Class<? extends Install<T>>, T>... pairs) {
//        for (Pair<Class<? extends Install<T>>, T> p : pairs) {
//            install(p.first, p.second);
//        }
//        return this;
//    }
}

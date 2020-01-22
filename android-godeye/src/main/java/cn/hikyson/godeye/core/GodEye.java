package cn.hikyson.godeye.core;


import android.app.Application;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.hikyson.godeye.core.exceptions.UnexpectException;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.SubjectSupport;
import cn.hikyson.godeye.core.internal.modules.appsize.AppSize;
import cn.hikyson.godeye.core.internal.modules.battery.Battery;
import cn.hikyson.godeye.core.internal.modules.cpu.Cpu;
import cn.hikyson.godeye.core.internal.modules.crash.Crash;
import cn.hikyson.godeye.core.internal.modules.fps.Fps;
import cn.hikyson.godeye.core.internal.modules.imagecanary.ImageCanary;
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
import cn.hikyson.godeye.core.internal.modules.viewcanary.ViewCanary;
import cn.hikyson.godeye.core.utils.ActivityStackUtil;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * core entrance
 * install then module.subject() then uninstall
 * Created by kysonchao on 2017/11/22.
 */
public class GodEye {
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ModuleName.CPU, ModuleName.BATTERY, ModuleName.FPS, ModuleName.LEAK,
            ModuleName.HEAP, ModuleName.PSS, ModuleName.TRAFFIC, ModuleName.CRASH,
            ModuleName.THREAD, ModuleName.RAM, ModuleName.NETWORK, ModuleName.SM,
            ModuleName.STARTUP, ModuleName.PAGELOAD, ModuleName.METHOD_CANARY, ModuleName.APP_SIZE, ModuleName.VIEW_CANARY, ModuleName.IMAGE_CANARY
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
        public static final String PAGELOAD = "PAGELOAD";
        public static final String METHOD_CANARY = "METHOD_CANARY";
        public static final String APP_SIZE = "APP_SIZE";
        public static final String VIEW_CANARY = "VIEW_CANARY";
        public static final String IMAGE_CANARY = "IMAGE_CANARY";
    }

    public static final Set<String> ALL_MODULE_NAMES = new HashSet<>();

    static {
        ALL_MODULE_NAMES.add(GodEye.ModuleName.CPU);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.BATTERY);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.FPS);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.LEAK);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.HEAP);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.PSS);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.RAM);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.NETWORK);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.SM);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.STARTUP);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.TRAFFIC);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.CRASH);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.THREAD);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.PAGELOAD);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.METHOD_CANARY);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.APP_SIZE);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.VIEW_CANARY);
        ALL_MODULE_NAMES.add(GodEye.ModuleName.IMAGE_CANARY);
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
     * init
     *
     * @param application
     */
    public void init(Application application) {
        mApplication = application;
        ActivityStackUtil.register(application);

//        new Thread(new Runnable() {// TODO KYSON DEL
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    NativeHeapInfo nativeHeapInfo = MemoryUtil.getAppNativeHeap();
//                    L.d("native:" + nativeHeapInfo);
//                    L.d("pss:" + Debug.getPss());
//                    L.d("pss2:" + MemoryUtil.getAppPssInfo(GodEye.instance().getApplication()));
//                }
//            }
//        }).start();


        L.d("GodEye init.");
    }

    /**
     * install modules
     *
     * @param godEyeConfig
     * @return
     */
    public synchronized GodEye install(final GodEyeConfig godEyeConfig) {
        long startTime = System.currentTimeMillis();
        if (godEyeConfig.getCpuConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.CPU);
            if (moduleObj == null) {
                moduleObj = new Cpu();
                mModules.put(ModuleName.CPU, moduleObj);
            }
            ((Cpu) moduleObj).install(godEyeConfig.getCpuConfig());
        }
        if (godEyeConfig.getBatteryConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.BATTERY);
            if (moduleObj == null) {
                moduleObj = new Battery();
                mModules.put(ModuleName.BATTERY, moduleObj);
            }
            ((Battery) moduleObj).install(godEyeConfig.getBatteryConfig());
        }
        if (godEyeConfig.getFpsConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.FPS);
            if (moduleObj == null) {
                moduleObj = new Fps();
                mModules.put(ModuleName.FPS, moduleObj);
            }
            ((Fps) moduleObj).install(godEyeConfig.getFpsConfig());
        }
        if (godEyeConfig.getLeakConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.LEAK);
            if (moduleObj == null) {
                moduleObj = LeakDetector.instance();
                mModules.put(ModuleName.LEAK, moduleObj);
            }
            ((LeakDetector) moduleObj).install(godEyeConfig.getLeakConfig());
        }
        if (godEyeConfig.getHeapConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.HEAP);
            if (moduleObj == null) {
                moduleObj = new Heap();
                mModules.put(ModuleName.HEAP, moduleObj);
            }
            ((Heap) moduleObj).install(godEyeConfig.getHeapConfig());
        }
        if (godEyeConfig.getPssConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.PSS);
            if (moduleObj == null) {
                moduleObj = new Pss();
                mModules.put(ModuleName.PSS, moduleObj);
            }
            ((Pss) moduleObj).install(godEyeConfig.getPssConfig());
        }
        if (godEyeConfig.getRamConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.RAM);
            if (moduleObj == null) {
                moduleObj = new Ram();
                mModules.put(ModuleName.RAM, moduleObj);
            }
            ((Ram) moduleObj).install(godEyeConfig.getRamConfig());
        }
        if (godEyeConfig.getNetworkConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.NETWORK);
            if (moduleObj == null) {
                moduleObj = new Network();
                mModules.put(ModuleName.NETWORK, moduleObj);
            }
            ((Network) moduleObj).install(godEyeConfig.getNetworkConfig());
        }
        if (godEyeConfig.getSmConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.SM);
            if (moduleObj == null) {
                moduleObj = new Sm();
                mModules.put(ModuleName.SM, moduleObj);
            }
            ((Sm) moduleObj).install(godEyeConfig.getSmConfig());
        }
        if (godEyeConfig.getStartupConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.STARTUP);
            if (moduleObj == null) {
                moduleObj = new Startup();
                mModules.put(ModuleName.STARTUP, moduleObj);
            }
            ((Startup) moduleObj).install(godEyeConfig.getStartupConfig());
        }
        if (godEyeConfig.getTrafficConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.TRAFFIC);
            if (moduleObj == null) {
                moduleObj = new Traffic();
                mModules.put(ModuleName.TRAFFIC, moduleObj);
            }
            ((Traffic) moduleObj).install(godEyeConfig.getTrafficConfig());
        }
        if (godEyeConfig.getCrashConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.CRASH);
            if (moduleObj == null) {
                moduleObj = new Crash();
                mModules.put(ModuleName.CRASH, moduleObj);
            }
            ((Crash) moduleObj).install(godEyeConfig.getCrashConfig());
        }
        if (godEyeConfig.getThreadConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.THREAD);
            if (moduleObj == null) {
                moduleObj = new ThreadDump();
                mModules.put(ModuleName.THREAD, moduleObj);
            }
            ((ThreadDump) moduleObj).install(godEyeConfig.getThreadConfig());
        }
        if (godEyeConfig.getPageloadConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.PAGELOAD);
            if (moduleObj == null) {
                moduleObj = new Pageload();
                mModules.put(ModuleName.PAGELOAD, moduleObj);
            }
            ((Pageload) moduleObj).install(godEyeConfig.getPageloadConfig());
        }
        if (godEyeConfig.getMethodCanaryConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.METHOD_CANARY);
            if (moduleObj == null) {
                moduleObj = new MethodCanary();
                mModules.put(ModuleName.METHOD_CANARY, moduleObj);
            }
            ((MethodCanary) moduleObj).install(godEyeConfig.getMethodCanaryConfig());
        }
        if (godEyeConfig.getAppSizeConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.APP_SIZE);
            if (moduleObj == null) {
                moduleObj = new AppSize();
                mModules.put(ModuleName.APP_SIZE, moduleObj);
            }
            ((AppSize) moduleObj).install(godEyeConfig.getAppSizeConfig());
        }
        if (godEyeConfig.getViewCanaryConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.VIEW_CANARY);
            if (moduleObj == null) {
                moduleObj = new ViewCanary();
                mModules.put(ModuleName.VIEW_CANARY, moduleObj);
            }
            ((ViewCanary) moduleObj).install(godEyeConfig.getViewCanaryConfig());
        }
        if (godEyeConfig.getImageCanaryConfig() != null) {
            Object moduleObj = mModules.get(ModuleName.IMAGE_CANARY);
            if (moduleObj == null) {
                moduleObj = new ImageCanary();
                mModules.put(ModuleName.IMAGE_CANARY, moduleObj);
            }
            ((ImageCanary) moduleObj).install(godEyeConfig.getImageCanaryConfig());
        }
        L.d("GodEye install, godEyeConfig: %s, cost %s ms", godEyeConfig, (System.currentTimeMillis() - startTime));
        return this;
    }

    /**
     * 卸载所有可卸载的模块
     *
     * @return
     */
    public synchronized GodEye uninstall() {
        long startTime = System.currentTimeMillis();
        for (Map.Entry<String, Object> entry : mModules.entrySet()) {
            if (entry.getValue() instanceof Install) {
                ((Install) entry.getValue()).uninstall();
            }
        }
        mModules.clear();
        L.d("GodEye uninstall success, cost %s ms", (System.currentTimeMillis() - startTime));
        return this;
    }

    /**
     * get module if exist
     *
     * @param moduleName
     * @param <T>
     * @return
     * @throws UninstallException if not exist module
     */
    public synchronized <T> T getModule(@ModuleName String moduleName) throws UninstallException {
        Object moduleObj = mModules.get(moduleName);
        if (moduleObj == null) {
            throw new UninstallException("module [" + moduleName + "] is not installed.");
        }
        try {
            // noinspection unchecked
            return (T) moduleObj;
        } catch (Throwable e) {
            throw new UnexpectException("module [" + moduleName + "] has wrong instance type");
        }
    }

    public synchronized @ModuleName
    Set<String> getInstalledModuleNames() {
        return mModules.keySet();
    }

    /**
     * get observable of module
     *
     * @param moduleName
     * @param <S>
     * @param <M>
     * @return
     * @throws UninstallException
     */
    public <S extends SubjectSupport<M>, M> Observable<M> moduleObservable(@ModuleName String moduleName) throws UninstallException {
        return this.<S>getModule(moduleName).subject();
    }

    /**
     * observe module
     *
     * @param moduleName
     * @param consumer
     * @param <S>
     * @param <M>
     * @return
     * @throws UninstallException
     */
    public <S extends SubjectSupport<M>, M> Disposable observeModule(@ModuleName String moduleName, Consumer<M> consumer) throws UninstallException {
        return this.<S>getModule(moduleName).subject().subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation()).subscribe(consumer);
    }

    public Application getApplication() {
        return mApplication;
    }
}

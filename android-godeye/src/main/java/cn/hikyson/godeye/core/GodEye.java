package cn.hikyson.godeye.core;


import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pair;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Type;

import cn.hikyson.godeye.core.helper.PermissionContext;
import cn.hikyson.godeye.core.helper.PermissionRequest;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.internal.modules.battery.Battery;
import cn.hikyson.godeye.core.internal.modules.cpu.Cpu;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuContext;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuContextImpl;
import cn.hikyson.godeye.core.internal.modules.crash.Crash;
import cn.hikyson.godeye.core.internal.modules.crash.CrashProvider;
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
import cn.hikyson.godeye.core.internal.modules.thread.ThreadFilter;
import cn.hikyson.godeye.core.internal.modules.thread.deadlock.DeadLock;
import cn.hikyson.godeye.core.internal.modules.thread.deadlock.DeadlockDefaultThreadFilter;
import cn.hikyson.godeye.core.internal.modules.traffic.Traffic;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * 入口
 * install -> module.subject() -> uninstall
 * Created by kysonchao on 2017/11/22.
 */
public class GodEye {
    private Cpu mCpu;
    private Battery mBattery;
    private Fps mFps;
    private LeakDetector mLeakDetector;
    private Heap mHeap;
    private Pss mPss;
    private Ram mRam;
    private Network mNetwork;
    private Sm mSm;
    private Startup mStartup;
    private Traffic mTraffic;
    private Crash mCrash;
    private ThreadDump mThreadDump;
    private DeadLock mDeadLock;
    private Pageload mPageload;

    private GodEye() {
    }

    private static class InstanceHolder {
        private static final GodEye sInstance = new GodEye();
    }

    public static GodEye instance() {
        return InstanceHolder.sInstance;
    }

    public <T> GodEye install(Install<T> install, T config) {
        install.install(config);
        return this;
    }

    public <T> GodEye installz(Class<? extends Install<T>> clz, T config) {
        getModule(clz).install(config);
        return this;
    }

    public <T> GodEye install2(Pair<Class<? extends Install<T>>, T>... pairs) {
        for (Pair<Class<? extends Install<T>>, T> p : pairs) {
            installz(p.first, p.second);
        }
        return this;
    }

    public <T> GodEye installz(@Modules.ModuleName String moduleName, T config) {
        Modules.sModules.get(moduleName)
        getModule(clz).install(config);
        return this;
    }

//    public void e() {
//        install2(new Pair<Class<? extends Install<CpuContext>>, CpuContext>(Cpu.class,new CpuContextImpl()));
//        installz(Cpu.class, new CpuContextImpl());
//    }

    public Cpu cpu2() {
        return getModule(Cpu.class);
    }

    public <T> T getModule(Class<T> clz) {
        try {
            return clz.newInstance();
        } catch (InstantiationException e) {
            L.e(String.valueOf(e));
        } catch (IllegalAccessException e) {
            L.e(String.valueOf(e));
        }
        return null;
    }

    public <T> T getModule(@Modules.ModuleName String moduleName){
        getModule(Modules.sModules.get(moduleName));
    }

    public void installAll(Application c, PermissionRequest permissionRequest, CrashProvider crashProvider) {
        installAll(c, permissionRequest, crashProvider, new DeadlockDefaultThreadFilter());
    }

    public void installAll(final Application c, final PermissionRequest permissionRequest, CrashProvider crashProvider, ThreadFilter deadLockThreadFilter) {
        Context context = c.getApplicationContext();
        cpu().install();
        battery().install(context);
        fps().install(context);
        heap().install();
        pss().install(context);
        ram().install(context);
        sm().install(context);
        traffic().install();
        crash().install(crashProvider);
        threadDump().install();
        deadLock().install(threadDump().subject(), deadLockThreadFilter);
        pageload().install(c);
        PermissionContext.getAttachedActivity(c, new PermissionContext.OnAttachActivityCallback() {
            @Override
            public void onAttachActivity(Activity activity) {
                permissionRequest.dispatchRequest(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            leakDetector().install(c);
                        }
                    }
                });
            }
        });
    }

    public void uninstallAll() {
        cpu().uninstall();
        battery().uninstall();
        fps().uninstall();
        heap().uninstall();
        pss().uninstall();
        ram().uninstall();
        sm().uninstall();
        traffic().uninstall();
        crash().uninstall();
        threadDump().uninstall();
        deadLock().uninstall();
        pageload().uninstall();
    }

    public Cpu cpu() {
        if (mCpu == null) {
            mCpu = new Cpu();
        }
        return mCpu;
    }

    public Battery battery() {
        if (mBattery == null) {
            mBattery = new Battery();
        }
        return mBattery;
    }

    public Fps fps() {
        if (mFps == null) {
            mFps = new Fps();
        }
        return mFps;
    }

    public LeakDetector leakDetector() {
        if (mLeakDetector == null) {
            mLeakDetector = LeakDetector.instance();
        }
        return mLeakDetector;
    }

    public Heap heap() {
        if (mHeap == null) {
            mHeap = new Heap();
        }
        return mHeap;
    }

    public Pss pss() {
        if (mPss == null) {
            mPss = new Pss();
        }
        return mPss;
    }

    public Ram ram() {
        if (mRam == null) {
            mRam = new Ram();
        }
        return mRam;
    }

    public Network network() {
        if (mNetwork == null) {
            mNetwork = new Network();
        }
        return mNetwork;
    }

    public Sm sm() {
        if (mSm == null) {
            mSm = Sm.instance();
        }
        return mSm;
    }

    public Startup startup() {
        if (mStartup == null) {
            mStartup = new Startup();
        }
        return mStartup;
    }

    public Traffic traffic() {
        if (mTraffic == null) {
            mTraffic = new Traffic();
        }
        return mTraffic;
    }

    public Crash crash() {
        if (mCrash == null) {
            mCrash = new Crash();
        }
        return mCrash;
    }

    public ThreadDump threadDump() {
        if (mThreadDump == null) {
            mThreadDump = new ThreadDump();
        }
        return mThreadDump;
    }

    public DeadLock deadLock() {
        if (mDeadLock == null) {
            mDeadLock = new DeadLock();
        }
        return mDeadLock;
    }

    public Pageload pageload() {
        if (mPageload == null) {
            mPageload = new Pageload();
        }
        return mPageload;
    }


//    @SuppressWarnings("unchecked")
//    private <T> T getModule(@ModuleName String moduleName, Class<T> clz) {
//        if (mModules == null) {
//            throw new IllegalStateException("None modules were installed.");
//        }
//        Install module = mModules.get(moduleName);
//        if (module == null) {
//            throw new IllegalStateException(moduleName + " module has not installed.");
//        }
//        if (!clz.isInstance(module)) {
//            throw new IllegalStateException(moduleName + " type " + clz.getName() + " does not match, expect " + module.getClass().getName());
//        }
//        return (T) module;
//    }
}

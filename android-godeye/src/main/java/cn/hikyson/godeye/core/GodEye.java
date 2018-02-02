package cn.hikyson.godeye.core;


import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pair;

import java.util.Map;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakDetector;
import cn.hikyson.godeye.core.internal.modules.sm.Sm;

/**
 * 入口
 * install -> module.subject() -> uninstall
 * Created by kysonchao on 2017/11/22.
 */
public class GodEye {
//    private Cpu mCpu;
//    private Battery mBattery;
//    private Fps mFps;
//    private LeakDetector mLeakDetector;
//    private Heap mHeap;
//    private Pss mPss;
//    private Ram mRam;
//    private Network mNetwork;
//    private Sm mSm;
//    private Startup mStartup;
//    private Traffic mTraffic;
//    private Crash mCrash;
//    private ThreadDump mThreadDump;
//    private DeadLock mDeadLock;
//    private Pageload mPageload;

    private GodEye() {
    }

    private static class InstanceHolder {
        private static final GodEye sInstance = new GodEye();
    }

    public static GodEye instance() {
        return InstanceHolder.sInstance;
    }

    public final <T> GodEye install(Class<? extends Install<T>> clz, T config) {
        getModule(clz).install(config);
        return this;
    }

    @SafeVarargs
    public final <T> GodEye install(Pair<Class<? extends Install<T>>, T>... pairs) {
        for (Pair<Class<? extends Install<T>>, T> p : pairs) {
            install(p.first, p.second);
        }
        return this;
    }

    private Map<Class, Object> mCachedModules = new ArrayMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getModule(Class<T> clz) {
        Object module = mCachedModules.get(clz);
        if (module != null) {
            if (!clz.isInstance(module)) {
                throw new IllegalStateException(clz.getName() + " must be instance of " + String.valueOf(module));
            }
            return (T) module;
        }
        try {
            T createdModule;
            if (LeakDetector.class.equals(clz)) {
                createdModule = (T) LeakDetector.instance();
            } else if (Sm.class.equals(clz)) {
                createdModule = (T) Sm.instance();
            } else {
                createdModule = clz.newInstance();
            }
            mCachedModules.put(clz, createdModule);
            return createdModule;
        } catch (Throwable e) {
            throw new IllegalStateException("Can not create instance of " + clz.getName() + ", " + String.valueOf(e));
        }
    }

//    public void installAll(Application c, PermissionRequest permissionRequest, CrashProvider crashProvider) {
//        installAll(c, permissionRequest, crashProvider, new DeadlockDefaultThreadFilter());
//    }
//
//    public void installAll(final Application c, final PermissionRequest permissionRequest, CrashProvider crashProvider, ThreadFilter deadLockThreadFilter) {
//        Context context = c.getApplicationContext();
//        cpu().install();
//        battery().install(context);
//        fps().install(context);
//        heap().install();
//        pss().install(context);
//        ram().install(context);
//        sm().install(context);
//        traffic().install();
//        crash().install(crashProvider);
//        threadDump().install();
//        deadLock().install(threadDump().subject(), deadLockThreadFilter);
//        pageload().install(c);
//        PermissionContext.getAttachedActivity(c, new PermissionContext.OnAttachActivityCallback() {
//            @Override
//            public void onAttachActivity(Activity activity) {
//                permissionRequest.dispatchRequest(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean aBoolean) throws Exception {
//                        if (aBoolean) {
//                            leakDetector().install(c);
//                        }
//                    }
//                });
//            }
//        });
//    }

//    public void uninstallAll() {
//        cpu().uninstall();
//        battery().uninstall();
//        fps().uninstall();
//        heap().uninstall();
//        pss().uninstall();
//        ram().uninstall();
//        sm().uninstall();
//        traffic().uninstall();
//        crash().uninstall();
//        threadDump().uninstall();
//        deadLock().uninstall();
//        pageload().uninstall();
//    }

//    public Cpu cpu() {
//        if (mCpu == null) {
//            mCpu = new Cpu();
//        }
//        return mCpu;
//    }
//
//    public Battery battery() {
//        if (mBattery == null) {
//            mBattery = new Battery();
//        }
//        return mBattery;
//    }
//
//    public Fps fps() {
//        if (mFps == null) {
//            mFps = new Fps();
//        }
//        return mFps;
//    }
//
//    public LeakDetector leakDetector() {
//        if (mLeakDetector == null) {
//            mLeakDetector = LeakDetector.instance();
//        }
//        return mLeakDetector;
//    }
//
//    public Heap heap() {
//        if (mHeap == null) {
//            mHeap = new Heap();
//        }
//        return mHeap;
//    }
//
//    public Pss pss() {
//        if (mPss == null) {
//            mPss = new Pss();
//        }
//        return mPss;
//    }
//
//    public Ram ram() {
//        if (mRam == null) {
//            mRam = new Ram();
//        }
//        return mRam;
//    }
//
//    public Network network() {
//        if (mNetwork == null) {
//            mNetwork = new Network();
//        }
//        return mNetwork;
//    }
//
//    public Sm sm() {
//        if (mSm == null) {
//            mSm = Sm.instance();
//        }
//        return mSm;
//    }
//
//    public Startup startup() {
//        if (mStartup == null) {
//            mStartup = new Startup();
//        }
//        return mStartup;
//    }
//
//    public Traffic traffic() {
//        if (mTraffic == null) {
//            mTraffic = new Traffic();
//        }
//        return mTraffic;
//    }
//
//    public Crash crash() {
//        if (mCrash == null) {
//            mCrash = new Crash();
//        }
//        return mCrash;
//    }
//
//    public ThreadDump threadDump() {
//        if (mThreadDump == null) {
//            mThreadDump = new ThreadDump();
//        }
//        return mThreadDump;
//    }
//
//    public DeadLock deadLock() {
//        if (mDeadLock == null) {
//            mDeadLock = new DeadLock();
//        }
//        return mDeadLock;
//    }
//
//    public Pageload pageload() {
//        if (mPageload == null) {
//            mPageload = new Pageload();
//        }
//        return mPageload;
//    }
}

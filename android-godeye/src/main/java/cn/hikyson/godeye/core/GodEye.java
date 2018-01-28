package cn.hikyson.godeye.core;


import android.app.Application;
import android.content.Context;

import cn.hikyson.godeye.core.internal.modules.battery.Battery;
import cn.hikyson.godeye.core.internal.modules.cpu.Cpu;
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

    public void installAll(Application c, CrashProvider crashProvider) {
        installAll(c, crashProvider, new DeadlockDefaultThreadFilter());
    }

    public void installAll(Application c, CrashProvider crashProvider, ThreadFilter deadLockThreadFilter) {
        Context context = c.getApplicationContext();
        cpu().install();
        battery().install(context);
        fps().install(context);
        leakDetector().install(c);
        heap().install();
        pss().install(context);
        ram().install(context);
        // network().install(null);
        sm().install(context);
        // startup().install(null);
        traffic().install();
        crash().install(crashProvider);
        threadDump().install();
        deadLock().install(threadDump().subject(), deadLockThreadFilter);
        pageload().install(c);
    }

    public void uninstallAll() {
        cpu().uninstall();
        battery().uninstall();
        fps().uninstall();
        // leakDetector().uninstall();
        heap().uninstall();
        pss().uninstall();
        ram().uninstall();
        // network().install(null);
        sm().uninstall();
        // startup().install(null);
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
}

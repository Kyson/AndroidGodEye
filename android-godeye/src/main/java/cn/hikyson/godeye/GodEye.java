package cn.hikyson.godeye;


import android.app.Application;
import android.content.Context;

import cn.hikyson.godeye.internal.modules.battery.Battery;
import cn.hikyson.godeye.internal.modules.cpu.Cpu;
import cn.hikyson.godeye.internal.modules.fps.Fps;
import cn.hikyson.godeye.internal.modules.leakdetector.LeakDetector;
import cn.hikyson.godeye.internal.modules.memory.Heap;
import cn.hikyson.godeye.internal.modules.memory.Pss;
import cn.hikyson.godeye.internal.modules.memory.Ram;
import cn.hikyson.godeye.internal.modules.network.Network;
import cn.hikyson.godeye.internal.modules.sm.Sm;
import cn.hikyson.godeye.internal.modules.startup.Startup;
import cn.hikyson.godeye.internal.modules.traffic.Traffic;

/**
 * 入口
 * install -> module.consume() -> uninstall
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

    public void installAll(Application c) {
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
}

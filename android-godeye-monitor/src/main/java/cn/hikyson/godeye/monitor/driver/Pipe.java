package cn.hikyson.godeye.monitor.driver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import cn.hikyson.godeye.core.internal.modules.battery.BatteryInfo;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuInfo;
import cn.hikyson.godeye.core.internal.modules.fps.FpsInfo;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakQueue;
import cn.hikyson.godeye.core.internal.modules.memory.HeapInfo;
import cn.hikyson.godeye.core.internal.modules.memory.PssInfo;
import cn.hikyson.godeye.core.internal.modules.memory.RamInfo;
import cn.hikyson.godeye.core.internal.modules.network.RequestBaseInfo;
import cn.hikyson.godeye.core.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficInfo;

/**
 * 数据管道，用于将引擎生产的数据输送到monitor
 * Created by kysonchao on 2017/11/21.
 */
public class Pipe {

    private Pipe() {
    }

    private static class InstanceHolder {
        private static final Pipe sInstance = new Pipe();
    }

    public static Pipe instance() {
        return InstanceHolder.sInstance;
    }

    private BatteryInfo mBatteryInfo;

    public void pushBatteryInfo(BatteryInfo batteryInfo) {
        mBatteryInfo = batteryInfo;
    }

    public BatteryInfo popBatteryInfo() {
        return mBatteryInfo;
    }

    private List<CpuInfo> mCpuInfos = new ArrayList<>();
    private final Object mLockForCpu = new Object();

    public void pushCpuInfo(CpuInfo cpuInfo) {
        synchronized (mLockForCpu) {
            mCpuInfos.add(cpuInfo);
        }
    }

    public Collection<CpuInfo> popCpuInfo() {
        synchronized (mLockForCpu) {
            final Collection<CpuInfo> cpuInfos = cloneList(mCpuInfos);
            mCpuInfos.clear();
            return cpuInfos;
        }
    }

    private List<TrafficInfo> mTrafficInfos = new ArrayList<>();
    private final Object mLockForTraffic = new Object();

    public void pushTrafficInfo(TrafficInfo trafficInfo) {
        synchronized (mLockForTraffic) {
            mTrafficInfos.add(trafficInfo);
        }
    }

    public Collection<TrafficInfo> popTrafficInfo() {
        synchronized (mLockForTraffic) {
            final Collection<TrafficInfo> trafficInfos = cloneList(mTrafficInfos);
            mTrafficInfos.clear();
            return trafficInfos;
        }
    }

    private FpsInfo mFpsInfo;

    public void pushFpsInfo(FpsInfo fpsInfo) {
        mFpsInfo = fpsInfo;
    }

    public FpsInfo popFpsInfo() {
        return mFpsInfo;
    }


    private LinkedHashSet<LeakQueue.LeakMemoryInfo> mLeakMemoryInfos = new LinkedHashSet<>();
    private final Object mLockForLeak = new Object();

    public void pushLeakMemoryInfos(LeakQueue.LeakMemoryInfo leakMemoryInfo) {
        synchronized (mLockForLeak) {
            mLeakMemoryInfos.remove(leakMemoryInfo);
            mLeakMemoryInfos.add(leakMemoryInfo);
        }
    }

    public Collection<LeakQueue.LeakMemoryInfo> popLeakMemoryInfos() {
        synchronized (mLockForLeak) {
            return cloneList(mLeakMemoryInfos);
        }
    }

    private List<BlockInfo> mBlockInfos = new ArrayList<>();
    private final Object mLockForBlock = new Object();

    public void pushBlockInfos(BlockInfo blockInfo) {
        synchronized (mLockForBlock) {
            mBlockInfos.add(blockInfo);
        }
    }

    public Collection<BlockInfo> popBlockInfos() {
        synchronized (mLockForBlock) {
            final Collection<BlockInfo> blockInfos = cloneList(mBlockInfos);
            mBlockInfos.clear();
            return blockInfos;
        }
    }


    private List<RequestBaseInfo> mRequestBaseInfos = new ArrayList<>();
    private final Object mLockForRequest = new Object();

    public void pushRequestBaseInfos(RequestBaseInfo requestBaseInfo) {
        synchronized (mLockForRequest) {
            mRequestBaseInfos.add(requestBaseInfo);
        }
    }

    public Collection<RequestBaseInfo> popRequestBaseInfos() {
        synchronized (mLockForRequest) {
            final Collection<RequestBaseInfo> requestBaseInfos = cloneList(mRequestBaseInfos);
            mRequestBaseInfos.clear();
            return requestBaseInfos;
        }
    }


    private StartupInfo mStartupInfo;

    public void pushStartupInfo(StartupInfo startupInfo) {
        mStartupInfo = startupInfo;
    }

    public StartupInfo popStartupInfo() {
        return mStartupInfo;
    }

    private RamInfo mRamInfo;

    public void pushRamInfo(RamInfo ramInfo) {
        mRamInfo = ramInfo;
    }

    public RamInfo popRamInfo() {
        return mRamInfo;
    }

    private PssInfo mPssInfo;

    public void pushPssInfo(PssInfo pssInfo) {
        mPssInfo = pssInfo;
    }

    public PssInfo popPssInfo() {
        return mPssInfo;
    }

    private List<HeapInfo> mHeapInfos = new ArrayList<>();
    private final Object mLockForHeapInfo = new Object();

    public void pushHeapInfo(HeapInfo heapInfo) {
        synchronized (mLockForHeapInfo) {
            mHeapInfos.add(heapInfo);
        }
    }

    public Collection<HeapInfo> popHeapInfo() {
        synchronized (mLockForHeapInfo) {
            final Collection<HeapInfo> heapInfos = cloneList(mHeapInfos);
            mHeapInfos.clear();
            return heapInfos;
        }
    }

    private static <T> Collection<T> cloneList(Collection<T> originList) {
        List<T> dest = new ArrayList<>();
        if (originList == null || originList.isEmpty()) {
            return dest;
        }
        dest.addAll(originList);
        return dest;
    }

}

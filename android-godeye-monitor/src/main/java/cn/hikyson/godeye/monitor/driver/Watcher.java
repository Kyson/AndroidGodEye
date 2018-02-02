package cn.hikyson.godeye.monitor.driver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.modules.battery.Battery;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryInfo;
import cn.hikyson.godeye.core.internal.modules.cpu.Cpu;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuInfo;
import cn.hikyson.godeye.core.internal.modules.crash.Crash;
import cn.hikyson.godeye.core.internal.modules.crash.CrashInfo;
import cn.hikyson.godeye.core.internal.modules.fps.Fps;
import cn.hikyson.godeye.core.internal.modules.fps.FpsInfo;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakDetector;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakQueue;
import cn.hikyson.godeye.core.internal.modules.memory.Heap;
import cn.hikyson.godeye.core.internal.modules.memory.HeapInfo;
import cn.hikyson.godeye.core.internal.modules.memory.Pss;
import cn.hikyson.godeye.core.internal.modules.memory.PssInfo;
import cn.hikyson.godeye.core.internal.modules.memory.Ram;
import cn.hikyson.godeye.core.internal.modules.memory.RamInfo;
import cn.hikyson.godeye.core.internal.modules.network.Network;
import cn.hikyson.godeye.core.internal.modules.network.RequestBaseInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.Pageload;
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadInfo;
import cn.hikyson.godeye.core.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.core.internal.modules.sm.Sm;
import cn.hikyson.godeye.core.internal.modules.startup.Startup;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadDump;
import cn.hikyson.godeye.core.internal.modules.thread.deadlock.DeadLock;
import cn.hikyson.godeye.core.internal.modules.traffic.Traffic;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficInfo;
import cn.hikyson.godeye.monitor.modules.ThreadInfo;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * monitor数据引擎，用于生产各项数据
 * Created by kysonchao on 2017/11/21.
 */
public class Watcher {
    private Pipe mPipe;
    private CompositeDisposable mCompositeDisposable;

    public Watcher() {
        mPipe = Pipe.instance();
        mCompositeDisposable = new CompositeDisposable();
    }

    /**
     * 监听所有的数据
     */
    public void observeAll() {
        GodEye godEye = GodEye.instance();
        mCompositeDisposable.add(godEye.getModule(Battery.class).subject().subscribe(new Consumer<BatteryInfo>() {
            @Override
            public void accept(BatteryInfo batteryInfo) throws Exception {
                mPipe.pushBatteryInfo(batteryInfo);
            }
        }));
        mCompositeDisposable.add(godEye.getModule(Cpu.class).subject().subscribe(new Consumer<CpuInfo>() {
            @Override
            public void accept(CpuInfo cpuInfo) throws Exception {
                mPipe.pushCpuInfo(cpuInfo);
            }
        }));
        mCompositeDisposable.add(godEye.getModule(Traffic.class).subject().subscribe(new Consumer<TrafficInfo>() {
            @Override
            public void accept(TrafficInfo trafficInfo) throws Exception {
                mPipe.pushTrafficInfo(trafficInfo);
            }
        }));
        mCompositeDisposable.add(godEye.getModule(Fps.class).subject().subscribe(new Consumer<FpsInfo>() {
            @Override
            public void accept(FpsInfo fpsInfo) throws Exception {
                mPipe.pushFpsInfo(fpsInfo);
            }
        }));
        mCompositeDisposable.add(godEye.getModule(LeakDetector.class).subject().subscribe(new Consumer<LeakQueue.LeakMemoryInfo>() {
            @Override
            public void accept(LeakQueue.LeakMemoryInfo leakMemoryInfo) throws Exception {
                mPipe.pushLeakMemoryInfos(leakMemoryInfo);
            }
        }));
        mCompositeDisposable.add(godEye.getModule(Sm.class).subject().subscribe(new Consumer<BlockInfo>() {
            @Override
            public void accept(BlockInfo blockInfo) throws Exception {
                mPipe.pushBlockInfos(blockInfo);
            }
        }));
        mCompositeDisposable.add(godEye.getModule(Network.class).subject().subscribe(new Consumer<RequestBaseInfo>() {
            @Override
            public void accept(RequestBaseInfo requestBaseInfo) throws Exception {
                mPipe.pushRequestBaseInfos(requestBaseInfo);
            }
        }));
        mCompositeDisposable.add(godEye.getModule(Startup.class).subject().subscribe(new Consumer<StartupInfo>() {
            @Override
            public void accept(StartupInfo startupInfo) throws Exception {
                mPipe.pushStartupInfo(startupInfo);
            }
        }));
        mCompositeDisposable.add(godEye.getModule(Ram.class).subject().subscribe(new Consumer<RamInfo>() {
            @Override
            public void accept(RamInfo ramInfo) throws Exception {
                mPipe.pushRamInfo(ramInfo);
            }
        }));
        mCompositeDisposable.add(godEye.getModule(Pss.class).subject().subscribe(new Consumer<PssInfo>() {
            @Override
            public void accept(PssInfo pssInfo) throws Exception {
                mPipe.pushPssInfo(pssInfo);
            }
        }));
        mCompositeDisposable.add(godEye.getModule(Heap.class).subject().subscribe(new Consumer<HeapInfo>() {
            @Override
            public void accept(HeapInfo heapInfo) throws Exception {
                mPipe.pushHeapInfo(heapInfo);
            }
        }));
        mCompositeDisposable.add(godEye.getModule(ThreadDump.class).subject().map(new Function<List<Thread>, List<ThreadInfo>>() {
            @Override
            public List<ThreadInfo> apply(List<Thread> threads) throws Exception {
                return ThreadInfo.convert(threads);
            }
        }).subscribe(new Consumer<List<ThreadInfo>>() {
            @Override
            public void accept(List<ThreadInfo> threads) throws Exception {
                mPipe.pushThreadInfo(threads);
            }
        }));
        mCompositeDisposable.add(godEye.getModule(DeadLock.class).subject().map(new Function<List<Thread>, List<Long>>() {
            @Override
            public List<Long> apply(List<Thread> threads) throws Exception {
                List<Long> threadIds = new ArrayList<>();
                for (Thread thread : threads) {
                    if (thread == null) {
                        continue;
                    }
                    threadIds.add(thread.getId());
                }
                return threadIds;
            }
        }).subscribe(new Consumer<List<Long>>() {
            @Override
            public void accept(List<Long> threads) throws Exception {
                mPipe.pushDeadLocks(threads);
            }
        }));
        mCompositeDisposable.add(godEye.getModule(Crash.class).subject().map(new Function<List<CrashInfo>, CrashInfo>() {
            @Override
            public CrashInfo apply(List<CrashInfo> crashInfos) throws Exception {
                //获取最近的一次崩溃
                if (crashInfos == null || crashInfos.isEmpty()) {
                    return CrashInfo.INVALID;
                }
                Collections.sort(crashInfos, new Comparator<CrashInfo>() {
                    @Override
                    public int compare(CrashInfo o1, CrashInfo o2) {
                        if (o1.timestampMillis < o2.timestampMillis) {
                            return 1;
                        }
                        if (o1.timestampMillis > o2.timestampMillis) {
                            return -1;
                        }
                        return 0;
                    }
                });
                return crashInfos.get(0);
            }
        }).subscribe(new Consumer<CrashInfo>() {
            @Override
            public void accept(CrashInfo crashInfo) throws Exception {
                if (crashInfo == CrashInfo.INVALID) {
                    return;
                }
                mPipe.pushCrashInfo(crashInfo);
            }
        }));
        mCompositeDisposable.add(godEye.getModule(Pageload.class).subject().subscribe(new Consumer<List<PageloadInfo>>() {
            @Override
            public void accept(List<PageloadInfo> pageloadInfos) throws Exception {
                mPipe.pushPageloadInfo(pageloadInfos);
            }
        }));
    }

    public void cancelAllObserve() {
        mCompositeDisposable.dispose();
    }
}

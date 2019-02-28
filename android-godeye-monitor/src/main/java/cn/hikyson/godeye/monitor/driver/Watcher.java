package cn.hikyson.godeye.monitor.driver;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import cn.hikyson.godeye.core.internal.modules.traffic.Traffic;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficInfo;
import cn.hikyson.godeye.monitor.modulemodel.AppInfo;
import cn.hikyson.godeye.monitor.modulemodel.BlockSimpleInfo;
import cn.hikyson.godeye.monitor.modulemodel.ThreadInfo;
import cn.hikyson.godeye.monitor.processors.Messager;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * monitor数据引擎，用于生产各项数据
 * Created by kysonchao on 2017/11/21.
 */
public class Watcher {
    private CompositeDisposable mCompositeDisposable;
    private Messager mMessager;

    public Watcher(Messager messager) {
        mCompositeDisposable = new CompositeDisposable();
        mMessager = messager;
    }

    /**
     * 监听所有的数据
     */
    public void observeAll() {
        GodEye godEye = GodEye.instance();
        mCompositeDisposable.add(Observable.interval(5, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                mMessager.sendMessage(new ServerMessage("appInfo", new AppInfo()).toString());
            }
        }));
        mCompositeDisposable.add(godEye.<Battery>getModule(GodEye.ModuleName.BATTERY).subject().subscribe(new Consumer<BatteryInfo>() {
            @Override
            public void accept(BatteryInfo batteryInfo) throws Exception {
                mMessager.sendMessage(new ServerMessage("batteryInfo", batteryInfo).toString());
            }
        }));
        mCompositeDisposable.add(godEye.<Cpu>getModule(GodEye.ModuleName.CPU).subject().subscribe(new Consumer<CpuInfo>() {
            @Override
            public void accept(CpuInfo cpuInfo) throws Exception {
                mMessager.sendMessage(new ServerMessage("cpuInfo", cpuInfo).toString());
            }
        }));
        mCompositeDisposable.add(godEye.<Traffic>getModule(GodEye.ModuleName.TRAFFIC).subject().subscribe(new Consumer<TrafficInfo>() {
            @Override
            public void accept(TrafficInfo trafficInfo) throws Exception {
                mMessager.sendMessage(new ServerMessage("trafficInfo", trafficInfo).toString());
            }
        }));
        mCompositeDisposable.add(godEye.<Fps>getModule(GodEye.ModuleName.FPS).subject().subscribe(new Consumer<FpsInfo>() {
            @Override
            public void accept(FpsInfo fpsInfo) throws Exception {
                mMessager.sendMessage(new ServerMessage("fpsInfo", fpsInfo).toString());
            }
        }));
        mCompositeDisposable.add(godEye.<LeakDetector>getModule(GodEye.ModuleName.LEAK).subject().subscribe(new Consumer<LeakQueue.LeakMemoryInfo>() {
            @Override
            public void accept(LeakQueue.LeakMemoryInfo leakMemoryInfo) throws Exception {
                mMessager.sendMessage(new ServerMessage("leakInfo", leakMemoryInfo).toString());
            }
        }));
        mCompositeDisposable.add(godEye.<Sm>getModule(GodEye.ModuleName.SM).subject().subscribe(new Consumer<BlockInfo>() {
            @Override
            public void accept(BlockInfo blockInfo) throws Exception {
                mMessager.sendMessage(new ServerMessage("blockInfo", new BlockSimpleInfo(blockInfo)).toString());
            }
        }));
        mCompositeDisposable.add(godEye.<Network>getModule(GodEye.ModuleName.NETWORK).subject().subscribe(new Consumer<RequestBaseInfo>() {
            @Override
            public void accept(RequestBaseInfo requestBaseInfo) throws Exception {
                mMessager.sendMessage(new ServerMessage("networkInfo", requestBaseInfo).toString());
            }
        }));
        mCompositeDisposable.add(godEye.<Startup>getModule(GodEye.ModuleName.STARTUP).subject().subscribe(new Consumer<StartupInfo>() {
            @Override
            public void accept(final StartupInfo startupInfo) throws Exception {
                mCompositeDisposable.add(Observable.interval(5, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mMessager.sendMessage(new ServerMessage("startupInfo", startupInfo).toString());
                    }
                }));
            }
        }));
        mCompositeDisposable.add(godEye.<Ram>getModule(GodEye.ModuleName.RAM).subject().subscribe(new Consumer<RamInfo>() {
            @Override
            public void accept(RamInfo ramInfo) throws Exception {
                mMessager.sendMessage(new ServerMessage("ramInfo", ramInfo).toString());
            }
        }));
        mCompositeDisposable.add(godEye.<Pss>getModule(GodEye.ModuleName.PSS).subject().subscribe(new Consumer<PssInfo>() {
            @Override
            public void accept(PssInfo pssInfo) throws Exception {
                mMessager.sendMessage(new ServerMessage("pssInfo", pssInfo).toString());
            }
        }));
        mCompositeDisposable.add(godEye.<Heap>getModule(GodEye.ModuleName.HEAP).subject().subscribe(new Consumer<HeapInfo>() {
            @Override
            public void accept(HeapInfo heapInfo) throws Exception {
                mMessager.sendMessage(new ServerMessage("heapInfo", heapInfo).toString());
            }
        }));
        mCompositeDisposable.add(godEye.<ThreadDump>getModule(GodEye.ModuleName.THREAD).subject().map(new Function<List<Thread>, List<ThreadInfo>>() {
            @Override
            public List<ThreadInfo> apply(List<Thread> threads) throws Exception {
                return ThreadInfo.convert(threads);
            }
        }).subscribe(new Consumer<List<ThreadInfo>>() {
            @Override
            public void accept(List<ThreadInfo> threadInfos) throws Exception {
                mMessager.sendMessage(new ServerMessage("threadInfo", threadInfos).toString());
            }
        }));
//        mCompositeDisposable.add(godEye.<DeadLock>getModule(GodEye.ModuleName.DEADLOCK).subject().map(new Function<List<Thread>, List<Long>>() {
//            @Override
//            public List<Long> apply(List<Thread> threads) throws Exception {
//                List<Long> threadIds = new ArrayList<>();
//                for (Thread thread : threads) {
//                    if (thread == null) {
//                        continue;
//                    }
//                    threadIds.add(thread.getId());
//                }
//                return threadIds;
//            }
//        }).subscribe(new Consumer<List<Long>>() {
//            @Override
//            public void accept(List<Long> threads) throws Exception {
//
//            }
//        }));
        mCompositeDisposable.add(godEye.<Crash>getModule(GodEye.ModuleName.CRASH).subject()
                .filter(new Predicate<List<CrashInfo>>() {
                    @Override
                    public boolean test(List<CrashInfo> crashInfos) throws Exception {
                        return crashInfos != null && !crashInfos.isEmpty();
                    }
                })
                .map(new Function<List<CrashInfo>, CrashInfo>() {
                    @Override
                    public CrashInfo apply(List<CrashInfo> crashInfos) throws Exception {
                        //获取最近的一次崩溃
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
                    public void accept(final CrashInfo crashInfo) throws Exception {
                        mCompositeDisposable.add(Observable.interval(5, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                mMessager.sendMessage(new ServerMessage("crashInfo", crashInfo).toString());
                            }
                        }));
                    }
                }));
        mCompositeDisposable.add(godEye.<Pageload>getModule(GodEye.ModuleName.PAGELOAD).subject().subscribe(new Consumer<PageloadInfo>() {
            @Override
            public void accept(PageloadInfo pageloadInfo) throws Exception {
                mMessager.sendMessage(new ServerMessage("pageloadInfo", pageloadInfo).toString());
            }
        }));
    }

    public void cancelAllObserve() {
        mCompositeDisposable.dispose();
    }
}

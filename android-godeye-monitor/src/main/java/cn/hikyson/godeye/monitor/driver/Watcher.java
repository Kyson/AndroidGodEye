package cn.hikyson.godeye.monitor.driver;

import java.util.List;

import cn.hikyson.godeye.core.GodEye;
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
import cn.hikyson.godeye.monitor.modules.ThreadModule;
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
        mCompositeDisposable.add(godEye.battery().subject().subscribe(new Consumer<BatteryInfo>() {
            @Override
            public void accept(BatteryInfo batteryInfo) throws Exception {
                mPipe.pushBatteryInfo(batteryInfo);
            }
        }));
        mCompositeDisposable.add(godEye.cpu().subject().subscribe(new Consumer<CpuInfo>() {
            @Override
            public void accept(CpuInfo cpuInfo) throws Exception {
                mPipe.pushCpuInfo(cpuInfo);
            }
        }));
        mCompositeDisposable.add(godEye.traffic().subject().subscribe(new Consumer<TrafficInfo>() {
            @Override
            public void accept(TrafficInfo trafficInfo) throws Exception {
                mPipe.pushTrafficInfo(trafficInfo);
            }
        }));
        mCompositeDisposable.add(godEye.fps().subject().subscribe(new Consumer<FpsInfo>() {
            @Override
            public void accept(FpsInfo fpsInfo) throws Exception {
                mPipe.pushFpsInfo(fpsInfo);
            }
        }));
        mCompositeDisposable.add(godEye.leakDetector().subject().subscribe(new Consumer<LeakQueue.LeakMemoryInfo>() {
            @Override
            public void accept(LeakQueue.LeakMemoryInfo leakMemoryInfo) throws Exception {
                mPipe.pushLeakMemoryInfos(leakMemoryInfo);
            }
        }));
        mCompositeDisposable.add(godEye.sm().subject().subscribe(new Consumer<BlockInfo>() {
            @Override
            public void accept(BlockInfo blockInfo) throws Exception {
                mPipe.pushBlockInfos(blockInfo);
            }
        }));
        mCompositeDisposable.add(godEye.network().subject().subscribe(new Consumer<RequestBaseInfo>() {
            @Override
            public void accept(RequestBaseInfo requestBaseInfo) throws Exception {
                mPipe.pushRequestBaseInfos(requestBaseInfo);
            }
        }));
        mCompositeDisposable.add(godEye.startup().subject().subscribe(new Consumer<StartupInfo>() {
            @Override
            public void accept(StartupInfo startupInfo) throws Exception {
                mPipe.pushStartupInfo(startupInfo);
            }
        }));
        mCompositeDisposable.add(godEye.ram().subject().subscribe(new Consumer<RamInfo>() {
            @Override
            public void accept(RamInfo ramInfo) throws Exception {
                mPipe.pushRamInfo(ramInfo);
            }
        }));
        mCompositeDisposable.add(godEye.pss().subject().subscribe(new Consumer<PssInfo>() {
            @Override
            public void accept(PssInfo pssInfo) throws Exception {
                mPipe.pushPssInfo(pssInfo);
            }
        }));
        mCompositeDisposable.add(godEye.heap().subject().subscribe(new Consumer<HeapInfo>() {
            @Override
            public void accept(HeapInfo heapInfo) throws Exception {
                mPipe.pushHeapInfo(heapInfo);
            }
        }));
        mCompositeDisposable.add(godEye.threadDump().subject().map(new Function<List<Thread>, List<ThreadModule.ThreadInfo>>() {
            @Override
            public List<ThreadModule.ThreadInfo> apply(List<Thread> threads) throws Exception {
                return ThreadModule.ThreadInfo.convert(threads);
            }
        }).subscribe(new Consumer<List<ThreadModule.ThreadInfo>>() {
            @Override
            public void accept(List<ThreadModule.ThreadInfo> threads) throws Exception {
                mPipe.pushThreadInfo(threads);
            }
        }));
    }

    public void cancelAllObserve() {
        mCompositeDisposable.dispose();
    }
}

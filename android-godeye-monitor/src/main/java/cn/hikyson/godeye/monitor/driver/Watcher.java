package cn.hikyson.godeye.monitor.driver;

import cn.hikyson.godeye.GodEye;
import cn.hikyson.godeye.internal.modules.battery.BatteryInfo;
import cn.hikyson.godeye.internal.modules.cpu.CpuInfo;
import cn.hikyson.godeye.internal.modules.fps.FpsInfo;
import cn.hikyson.godeye.internal.modules.leakdetector.LeakQueue;
import cn.hikyson.godeye.internal.modules.memory.HeapInfo;
import cn.hikyson.godeye.internal.modules.memory.PssInfo;
import cn.hikyson.godeye.internal.modules.memory.RamInfo;
import cn.hikyson.godeye.internal.modules.network.RequestBaseInfo;
import cn.hikyson.godeye.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.internal.modules.traffic.TrafficInfo;
import cn.hikyson.godeye.monitor.modules.PssModule;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * monitor数据引擎，用于生产各项数据
 * Created by kysonchao on 2017/11/21.
 */
public class Watcher {
    private Pipe mPipe;

    public Watcher() {
        mPipe = Pipe.instance();
    }

    /**
     * 监听所有的数据
     */
    public void watchAll() {
        GodEye godEye = GodEye.instance();
        godEye.battery().consume().subscribe(new Consumer<BatteryInfo>() {
            @Override
            public void accept(BatteryInfo batteryInfo) throws Exception {
                mPipe.pushBatteryInfo(batteryInfo);
            }
        });
        godEye.cpu().consume().subscribe(new Consumer<CpuInfo>() {
            @Override
            public void accept(CpuInfo cpuInfo) throws Exception {
                mPipe.pushCpuInfo(cpuInfo);
            }
        });
        godEye.traffic().consume().subscribe(new Consumer<TrafficInfo>() {
            @Override
            public void accept(TrafficInfo trafficInfo) throws Exception {
                mPipe.pushTrafficInfo(trafficInfo);
            }
        });
        godEye.fps().consume().subscribe(new Consumer<FpsInfo>() {
            @Override
            public void accept(FpsInfo fpsInfo) throws Exception {
                mPipe.pushFpsInfo(fpsInfo);
            }
        });
        godEye.leakDetector().consume().subscribe(new Consumer<LeakQueue.LeakMemoryInfo>() {
            @Override
            public void accept(LeakQueue.LeakMemoryInfo leakMemoryInfo) throws Exception {
                mPipe.pushLeakMemoryInfos(leakMemoryInfo);
            }
        });
        godEye.sm().consume().subscribe(new Consumer<BlockInfo>() {
            @Override
            public void accept(BlockInfo blockInfo) throws Exception {
                mPipe.pushBlockInfos(blockInfo);
            }
        });
        godEye.network().consume().subscribe(new Consumer<RequestBaseInfo>() {
            @Override
            public void accept(RequestBaseInfo requestBaseInfo) throws Exception {
                mPipe.pushRequestBaseInfos(requestBaseInfo);
            }
        });
        godEye.startup().consume().subscribe(new Consumer<StartupInfo>() {
            @Override
            public void accept(StartupInfo startupInfo) throws Exception {
                mPipe.pushStartupInfo(startupInfo);
            }
        });
        Observable<RamInfo> ramInfoObservable = godEye.ram().consume();
        Observable<PssInfo> pssInfoObservable = godEye.pss().consume();
        Observable.zip(ramInfoObservable, pssInfoObservable, new BiFunction<RamInfo, PssInfo, PssModule.RamWithPssInfo>() {
            @Override
            public PssModule.RamWithPssInfo apply(RamInfo ramInfo, PssInfo pssInfo) throws Exception {
                return new PssModule.RamWithPssInfo(ramInfo, pssInfo);
            }
        }).subscribe(new Consumer<PssModule.RamWithPssInfo>() {
            @Override
            public void accept(PssModule.RamWithPssInfo ramWithPssInfo) throws Exception {
                mPipe.pushRamWithPssInfo(ramWithPssInfo);
            }
        });
        godEye.heap().consume().subscribe(new Consumer<HeapInfo>() {
            @Override
            public void accept(HeapInfo heapInfo) throws Exception {
                mPipe.pushHeapInfo(heapInfo);
            }
        });


    }
}

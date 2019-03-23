package cn.hikyson.godeye.monitor.driver;

import android.util.Pair;

import com.koushikdutta.async.http.WebSocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
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
import cn.hikyson.godeye.core.utils.ThreadUtil;
import cn.hikyson.godeye.monitor.modulemodel.AppInfo;
import cn.hikyson.godeye.monitor.modulemodel.BlockSimpleInfo;
import cn.hikyson.godeye.monitor.modulemodel.ThreadInfo;
import cn.hikyson.godeye.monitor.processors.Messager;
import cn.hikyson.godeye.monitor.processors.Processor;
import cn.hikyson.godeye.monitor.utils.GsonUtil;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * monitor数据引擎，用于生产各项数据
 * Created by kysonchao on 2017/11/21.
 */
public class Watcher implements Processor {
    private CompositeDisposable mCompositeDisposable;
    private Messager mMessager;
    //cache lastest message
    private MessageCache mMessageCache;

    public Watcher(Messager messager) {
        mCompositeDisposable = new CompositeDisposable();
        mMessager = messager;
        mMessageCache = new MessageCache();
    }

    /**
     * 监听所有的数据
     */
    public void observeAll() {
        GodEye godEye = GodEye.instance();
        mCompositeDisposable.addAll(
                //应用基础数据发射不一定在子线程，这里强制切换到子线程
                Observable.just(new AppInfo())
                        .map(this.<AppInfo>createConvertServerMessageFunction("appInfo"))
                        .subscribeOn(ThreadUtil.sComputationScheduler)
                        .observeOn(ThreadUtil.sComputationScheduler)
                        .subscribe(createSendMessageConsumer()),
                godEye.<Battery>getModule(GodEye.ModuleName.BATTERY).subject()
                        .map(this.<BatteryInfo>createConvertServerMessageFunction("batteryInfo"))
                        .subscribeOn(ThreadUtil.sComputationScheduler)
                        .observeOn(ThreadUtil.sComputationScheduler)
                        .subscribe(this.createSendMessageConsumer()),
                godEye.<Cpu>getModule(GodEye.ModuleName.CPU).subject()
                        .map(this.<CpuInfo>createConvertServerMessageFunction("cpuInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                godEye.<Traffic>getModule(GodEye.ModuleName.TRAFFIC).subject()
                        .map(this.<TrafficInfo>createConvertServerMessageFunction("trafficInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                godEye.<Fps>getModule(GodEye.ModuleName.FPS).subject()
                        .map(this.<FpsInfo>createConvertServerMessageFunction("fpsInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                godEye.<LeakDetector>getModule(GodEye.ModuleName.LEAK).subject()
                        .map(this.<LeakQueue.LeakMemoryInfo>createConvertServerMessageFunction("leakInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                godEye.<Sm>getModule(GodEye.ModuleName.SM).subject()
                        .map(blockMap())
                        .map(this.<BlockSimpleInfo>createConvertServerMessageFunction("blockInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                //网络数据发射不一定在子线程，这里强制切换到子线程
                godEye.<Network>getModule(GodEye.ModuleName.NETWORK).subject()
                        .map(this.<RequestBaseInfo>createConvertServerMessageFunction("networkInfo"))
                        .subscribeOn(ThreadUtil.sComputationScheduler)
                        .observeOn(ThreadUtil.sComputationScheduler)
                        .subscribe(this.createSendMessageConsumer()),
                //启动数据发射不一定在子线程，这里强制切换到子线程
                godEye.<Startup>getModule(GodEye.ModuleName.STARTUP).subject()
                        .map(this.<StartupInfo>createConvertServerMessageFunction("startupInfo"))
                        .subscribeOn(ThreadUtil.sComputationScheduler)
                        .observeOn(ThreadUtil.sComputationScheduler)
                        .subscribe(this.createSendMessageConsumer()),
                godEye.<Ram>getModule(GodEye.ModuleName.RAM).subject()
                        .map(this.<RamInfo>createConvertServerMessageFunction("ramInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                godEye.<Pss>getModule(GodEye.ModuleName.PSS).subject()
                        .map(this.<PssInfo>createConvertServerMessageFunction("pssInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                godEye.<Heap>getModule(GodEye.ModuleName.HEAP).subject()
                        .map(this.<HeapInfo>createConvertServerMessageFunction("heapInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                godEye.<ThreadDump>getModule(GodEye.ModuleName.THREAD).subject()
                        .map(threadMap())
                        .map(this.<List<ThreadInfo>>createConvertServerMessageFunction("threadInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                //crash发射数据不一定是子线程，所以这里强制切换到子线程
                godEye.<Crash>getModule(GodEye.ModuleName.CRASH).subject()
                        .subscribeOn(ThreadUtil.sComputationScheduler)
                        .observeOn(ThreadUtil.sComputationScheduler)
                        .filter(crashPredicate())
                        .map(firstCrashMap())
                        .map(this.<CrashInfo>createConvertServerMessageFunction("crashInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                godEye.<Pageload>getModule(GodEye.ModuleName.PAGELOAD).subject()
                        .map(this.<PageloadInfo>createConvertServerMessageFunction("pageloadInfo"))
                        .subscribe(this.createSendMessageConsumer())
        );
    }

    public void cancelAllObserve() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void process(WebSocket webSocket, String msg) {
        ThreadUtil.ensureWorkThread("Watcher process:" + msg);
        ClientMessage clientMessage = GsonUtil.fromJson(msg, ClientMessage.class);
        if ("clientOnline".equals(clientMessage.moduleName)) {//if a client get online,send init message to it
            for (Map.Entry<String, Object> entry : mMessageCache.copy().entrySet()) {
                webSocket.send(new ServerMessage(entry.getKey(), entry.getValue()).toString());
            }
        }
    }

    private SendMessageConsumer createSendMessageConsumer() {
        return new SendMessageConsumer(mMessager);
    }

    private <T> ConvertServerMessageFunction<T> createConvertServerMessageFunction(String module) {
        return new ConvertServerMessageFunction<T>(mMessageCache, module);
    }

    private Predicate<List<CrashInfo>> crashPredicate() {
        return new Predicate<List<CrashInfo>>() {
            @Override
            public boolean test(List<CrashInfo> crashInfos) throws Exception {
                return crashInfos != null && !crashInfos.isEmpty();
            }
        };
    }

    private Function<List<CrashInfo>, CrashInfo> firstCrashMap() {
        return new Function<List<CrashInfo>, CrashInfo>() {
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
        };
    }

    private Function<List<Thread>, List<ThreadInfo>> threadMap() {
        return new Function<List<Thread>, List<ThreadInfo>>() {
            @Override
            public List<ThreadInfo> apply(List<Thread> threads) throws Exception {
                return ThreadInfo.convert(threads);
            }
        };
    }

    private Function<BlockInfo, BlockSimpleInfo> blockMap() {
        return new Function<BlockInfo, BlockSimpleInfo>() {
            @Override
            public BlockSimpleInfo apply(BlockInfo blockInfo) throws Exception {
                return new BlockSimpleInfo(blockInfo);
            }
        };
    }

}

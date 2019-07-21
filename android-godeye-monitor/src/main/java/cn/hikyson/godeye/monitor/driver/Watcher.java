package cn.hikyson.godeye.monitor.driver;

import com.koushikdutta.async.http.WebSocket;

import java.util.Collections;
import java.util.Comparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanary;
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodsRecordInfo;
import cn.hikyson.godeye.core.internal.modules.network.Network;
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.ActivityLifecycleEvent;
import cn.hikyson.godeye.core.internal.modules.pageload.FragmentLifecycleEvent;
import cn.hikyson.godeye.core.internal.modules.pageload.PageLifecycleEventInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.Pageload;
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadUtil;
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
import cn.hikyson.godeye.monitor.modulemodel.MethodCanaryStatus;
import cn.hikyson.godeye.monitor.modulemodel.NetworkSummaryInfo;
import cn.hikyson.godeye.monitor.modulemodel.PageLifecycleProcessedEvent;
import cn.hikyson.godeye.monitor.modulemodel.ThreadInfo;
import cn.hikyson.godeye.monitor.processors.Messager;
import cn.hikyson.godeye.monitor.processors.Processor;
import cn.hikyson.godeye.monitor.utils.GsonUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;

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

    private <T> Observable<T> wrapThreadComputationObservable(Observable<T> observable) {
        if (observable == null) {
            return null;
        }
        return observable.subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation());
    }

    /**
     * 监听所有的数据
     */
    public void observeAll() {
        GodEye godEye = GodEye.instance();
        mCompositeDisposable.addAll(
                wrapThreadComputationObservable(godEye.<Battery>getModule(GodEye.ModuleName.BATTERY).subject())
                        .map(this.<BatteryInfo>createConvertServerMessageFunction("batteryInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                wrapThreadComputationObservable(godEye.<Cpu>getModule(GodEye.ModuleName.CPU).subject())
                        .map(this.<CpuInfo>createConvertServerMessageFunction("cpuInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                wrapThreadComputationObservable(godEye.<Traffic>getModule(GodEye.ModuleName.TRAFFIC).subject())
                        .map(this.<TrafficInfo>createConvertServerMessageFunction("trafficInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                wrapThreadComputationObservable(godEye.<Fps>getModule(GodEye.ModuleName.FPS).subject())
                        .map(this.<FpsInfo>createConvertServerMessageFunction("fpsInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                wrapThreadComputationObservable(godEye.<LeakDetector>getModule(GodEye.ModuleName.LEAK).subject())
                        .map(this.<LeakQueue.LeakMemoryInfo>createConvertServerMessageFunction("leakInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                wrapThreadComputationObservable(godEye.<Sm>getModule(GodEye.ModuleName.SM).subject())
                        .map(blockMap())
                        .map(this.<BlockSimpleInfo>createConvertServerMessageFunction("blockInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                wrapThreadComputationObservable(godEye.<Network>getModule(GodEye.ModuleName.NETWORK).subject())
                        .map(this.networkMap())
                        .map(this.createConvertServerMessageFunction("networkInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                wrapThreadComputationObservable(godEye.<Startup>getModule(GodEye.ModuleName.STARTUP).subject())
                        .map(this.createConvertServerMessageFunction("startupInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                wrapThreadComputationObservable(godEye.<Ram>getModule(GodEye.ModuleName.RAM).subject())
                        .map(this.<RamInfo>createConvertServerMessageFunction("ramInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                wrapThreadComputationObservable(godEye.<Pss>getModule(GodEye.ModuleName.PSS).subject())
                        .map(this.<PssInfo>createConvertServerMessageFunction("pssInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                wrapThreadComputationObservable(godEye.<Heap>getModule(GodEye.ModuleName.HEAP).subject())
                        .map(this.<HeapInfo>createConvertServerMessageFunction("heapInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                wrapThreadComputationObservable(godEye.<ThreadDump>getModule(GodEye.ModuleName.THREAD).subject())
                        .map(threadMap())
                        .map(this.<List<ThreadInfo>>createConvertServerMessageFunction("threadInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                wrapThreadComputationObservable(godEye.<Crash>getModule(GodEye.ModuleName.CRASH).subject())
                        .filter(crashPredicate())
                        .map(firstCrashMap())
                        .map(this.createConvertServerMessageFunction("crashInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                wrapThreadComputationObservable(godEye.<Pageload>getModule(GodEye.ModuleName.PAGELOAD).subject())
                        .map(this.pageLifecycleMap())
                        .map(this.<PageLifecycleProcessedEvent>createConvertServerMessageFunction("pageLifecycle"))
                        .subscribe(this.createSendMessageConsumer()),
                wrapThreadComputationObservable(godEye.<MethodCanary>getModule(GodEye.ModuleName.METHOD_CANARY).subject())
                        .map(this.<MethodsRecordInfo>createConvertServerMessageFunction("methodCanary"))
                        .subscribe(this.createSendMessageConsumer())
        );
        Observable<String> methodCanaryStatusSubject = wrapThreadComputationObservable(godEye.<MethodCanary>getModule(GodEye.ModuleName.METHOD_CANARY).statusSubject());
        if (methodCanaryStatusSubject != null) {
            mCompositeDisposable.add(methodCanaryStatusSubject.map(s -> {
                MethodCanary methodCanary = GodEye.instance().<MethodCanary>getModule(GodEye.ModuleName.METHOD_CANARY);
                return new ServerMessage("MethodCanaryStatus",
                        new MethodCanaryStatus(methodCanary.getMethodCanaryContext(), methodCanary.isInstalled(), methodCanary.isMonitoring()));
            }).subscribe(this.createSendMessageConsumer()));
        }
    }

    public void cancelAllObserve() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void process(final WebSocket webSocket, String msg) {
        ThreadUtil.ensureWorkThread("Watcher process:" + msg);
        ClientMessage clientMessage = GsonUtil.fromJson(msg, ClientMessage.class);
        if ("clientOnline".equals(clientMessage.moduleName)) {//if a client get online,send init message to it
            for (Map.Entry<String, Object> entry : mMessageCache.copy().entrySet()) {
                webSocket.send(new ServerMessage(entry.getKey(), entry.getValue()).toString());
            }
        } else if ("appInfo".equals(clientMessage.moduleName)) {
            webSocket.send(new ServerMessage("appInfo", new AppInfo()).toString());
        } else if ("methodCanary".equals(clientMessage.moduleName)) {
            final MethodCanary methodCanary = GodEye.instance().<MethodCanary>getModule(GodEye.ModuleName.METHOD_CANARY);
            if ("start".equals(String.valueOf(clientMessage.payload))) {
                methodCanary.startMonitor();
            } else if ("stop".equals(String.valueOf(clientMessage.payload))) {
                methodCanary.stopMonitor();
            }
        } else if ("MethodCanaryStatus".equals(clientMessage.moduleName)) {
            Subject<String> methodCanaryStatusSubject = GodEye.instance().<MethodCanary>getModule(GodEye.ModuleName.METHOD_CANARY).statusSubject();
            if (methodCanaryStatusSubject != null && !methodCanaryStatusSubject.hasComplete() && !methodCanaryStatusSubject.hasThrowable()) {
                methodCanaryStatusSubject.onNext("GET");
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
        return crashInfos -> crashInfos != null && !crashInfos.isEmpty();
    }

    private Function<PageLifecycleEventInfo, PageLifecycleProcessedEvent> pageLifecycleMap() {
        return tPageLifecycleEventInfo -> {
            PageLifecycleProcessedEvent pageLifecycleProcessedEvent = new PageLifecycleProcessedEvent<>();
            pageLifecycleProcessedEvent.pageInfo = tPageLifecycleEventInfo.pageInfo;
            pageLifecycleProcessedEvent.pageLifecycleEventWithTime = tPageLifecycleEventInfo.currentEvent;
            pageLifecycleProcessedEvent.processedInfo = new HashMap<>();
            if (pageLifecycleProcessedEvent.pageLifecycleEventWithTime != null
                    && (pageLifecycleProcessedEvent.pageLifecycleEventWithTime.lifecycleEvent == ActivityLifecycleEvent.ON_DRAW
                    || pageLifecycleProcessedEvent.pageLifecycleEventWithTime.lifecycleEvent == FragmentLifecycleEvent.ON_DRAW)) {
                long drawTime = PageloadUtil.parsePageDrawTimeMillis(tPageLifecycleEventInfo.allEvents);
                pageLifecycleProcessedEvent.processedInfo.put("drawTime", drawTime);
            }
            if (pageLifecycleProcessedEvent.pageLifecycleEventWithTime != null
                    && (pageLifecycleProcessedEvent.pageLifecycleEventWithTime.lifecycleEvent == ActivityLifecycleEvent.ON_LOAD
                    || pageLifecycleProcessedEvent.pageLifecycleEventWithTime.lifecycleEvent == FragmentLifecycleEvent.ON_LOAD)) {
                long loadTime = PageloadUtil.parsePageloadTimeMillis(tPageLifecycleEventInfo.allEvents);
                pageLifecycleProcessedEvent.processedInfo.put("loadTime", loadTime);
            }
            return pageLifecycleProcessedEvent;
        };
    }

    private Function<List<CrashInfo>, CrashInfo> firstCrashMap() {
        return crashInfos -> {
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
        };
    }

    private Function<List<Thread>, List<ThreadInfo>> threadMap() {
        return ThreadInfo::convert;
    }

    private Function<BlockInfo, BlockSimpleInfo> blockMap() {
        return BlockSimpleInfo::new;
    }

    private Function<NetworkInfo, NetworkSummaryInfo> networkMap() {
        return NetworkSummaryInfo::convert;
    }
}

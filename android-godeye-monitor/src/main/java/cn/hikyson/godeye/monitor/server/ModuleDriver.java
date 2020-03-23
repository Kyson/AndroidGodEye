package cn.hikyson.godeye.monitor.server;

import java.util.HashMap;
import java.util.List;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.helper.CrashStore;
import cn.hikyson.godeye.core.helper.RxModule;
import cn.hikyson.godeye.core.internal.modules.appsize.AppSizeInfo;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryInfo;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuInfo;
import cn.hikyson.godeye.core.internal.modules.crash.CrashInfo;
import cn.hikyson.godeye.core.internal.modules.fps.FpsInfo;
import cn.hikyson.godeye.core.internal.modules.imagecanary.ImageIssue;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakInfo;
import cn.hikyson.godeye.core.internal.modules.memory.HeapInfo;
import cn.hikyson.godeye.core.internal.modules.memory.PssInfo;
import cn.hikyson.godeye.core.internal.modules.memory.RamInfo;
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodsRecordInfo;
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.ActivityLifecycleEvent;
import cn.hikyson.godeye.core.internal.modules.pageload.FragmentLifecycleEvent;
import cn.hikyson.godeye.core.internal.modules.pageload.PageLifecycleEventInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadUtil;
import cn.hikyson.godeye.core.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadInfo;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficInfo;
import cn.hikyson.godeye.core.internal.modules.viewcanary.ViewIssueInfo;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.monitor.modules.BlockSimpleInfo;
import cn.hikyson.godeye.monitor.modules.NetworkSummaryInfo;
import cn.hikyson.godeye.monitor.modules.PageLifecycleProcessedEvent;
import cn.hikyson.godeye.monitor.modules.battery.BatteryInfoFactory;
import cn.hikyson.godeye.monitor.modules.leak.LeakConverter;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * monitor数据引擎，用于生产各项数据
 * Created by kysonchao on 2017/11/21.
 */
public class ModuleDriver {
    private CompositeDisposable mCompositeDisposable;
    private Messager mMessager;
    private boolean mIsRunning;

    private static class InstanceHolder {
        private static ModuleDriver sInstance = new ModuleDriver();
    }

    public static ModuleDriver instance() {
        return InstanceHolder.sInstance;
    }

    private ModuleDriver() {
    }

    /**
     * 监听所有的数据
     */
    public synchronized void start(Messager messager) {
        if (mIsRunning) {
            return;
        }
        mIsRunning = true;
        L.d("ModuleDriver start running.");
        mCompositeDisposable = new CompositeDisposable();
        mMessager = messager;
        mCompositeDisposable.addAll(
                RxModule.<BatteryInfo>wrapThreadComputationObservable(GodEye.ModuleName.BATTERY)
                        .map(BatteryInfoFactory.converter())
                        .map(this.createConvertServerMessageFunction("batteryInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<CpuInfo>wrapThreadComputationObservable(GodEye.ModuleName.CPU)
                        .map(this.createConvertServerMessageFunction("cpuInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<TrafficInfo>wrapThreadComputationObservable(GodEye.ModuleName.TRAFFIC)
                        .map(this.createConvertServerMessageFunction("trafficInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<FpsInfo>wrapThreadComputationObservable(GodEye.ModuleName.FPS)
                        .map(this.createConvertServerMessageFunction("fpsInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<LeakInfo>wrapThreadComputationObservable(GodEye.ModuleName.LEAK_CANARY)
                        .map(LeakConverter.leakConverter())
                        .map(this.createConvertServerMessageFunction("leakInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<BlockInfo>wrapThreadComputationObservable(GodEye.ModuleName.SM)
                        .map(blockMap())
                        .map(this.createConvertServerMessageFunction("blockInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<NetworkInfo>wrapThreadComputationObservable(GodEye.ModuleName.NETWORK)
                        .map(this.networkMap())
                        .map(this.createConvertServerMessageFunction("networkInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<StartupInfo>wrapThreadComputationObservable(GodEye.ModuleName.STARTUP)
                        .map(this.createConvertServerMessageFunction("startupInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<RamInfo>wrapThreadComputationObservable(GodEye.ModuleName.RAM)
                        .map(this.createConvertServerMessageFunction("ramInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<PssInfo>wrapThreadComputationObservable(GodEye.ModuleName.PSS)
                        .map(this.createConvertServerMessageFunction("pssInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<HeapInfo>wrapThreadComputationObservable(GodEye.ModuleName.HEAP)
                        .map(this.createConvertServerMessageFunction("heapInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<List<ThreadInfo>>wrapThreadComputationObservable(GodEye.ModuleName.THREAD)
                        .map(this.createConvertServerMessageFunction("threadInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<PageLifecycleEventInfo>wrapThreadComputationObservable(GodEye.ModuleName.PAGELOAD)
                        .map(this.pageLifecycleMap())
                        .map(this.createConvertServerMessageFunction("pageLifecycle"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<MethodsRecordInfo>wrapThreadComputationObservable(GodEye.ModuleName.METHOD_CANARY)
                        .map(this.createConvertServerMessageFunction("methodCanary"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<AppSizeInfo>wrapThreadComputationObservable(GodEye.ModuleName.APP_SIZE)
                        .map(this.createConvertServerMessageFunction("appSizeInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<ViewIssueInfo>wrapThreadComputationObservable(GodEye.ModuleName.VIEW_CANARY)
                        .map(this.createConvertServerMessageFunction("viewIssueInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<ImageIssue>wrapThreadComputationObservable(GodEye.ModuleName.IMAGE_CANARY)
                        .map(this.createConvertServerMessageFunction("imageIssue"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.wrapThreadComputationObservable(CrashStore.observeCrashAndCache(GodEye.instance().getApplication(), "crash_of_debug_monitor"))
                        .filter(crashPredicate())
                        .map(this.createConvertServerMessageFunction("crashInfo"))
                        .subscribe(this.createSendMessageConsumer())
        );
    }

    public synchronized void stop() {
        if (!mIsRunning) {
            return;
        }
        mIsRunning = false;
        L.d("ModuleDriver has stopped.");
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
        mMessager = null;
    }

    private SendMessageConsumer createSendMessageConsumer() {
        return new SendMessageConsumer(mMessager);
    }

    private <T> ConvertServerMessageFunction<T> createConvertServerMessageFunction(String module) {
        return new ConvertServerMessageFunction<T>(module);
    }

    private Function<PageLifecycleEventInfo, PageLifecycleProcessedEvent> pageLifecycleMap() {
        return tPageLifecycleEventInfo -> {
            PageLifecycleProcessedEvent pageLifecycleProcessedEvent = new PageLifecycleProcessedEvent();
            pageLifecycleProcessedEvent.pageType = tPageLifecycleEventInfo.pageInfo.pageType;
            pageLifecycleProcessedEvent.pageHashCode = tPageLifecycleEventInfo.pageInfo.pageHashCode;
            pageLifecycleProcessedEvent.pageClassName = tPageLifecycleEventInfo.pageInfo.pageClassName;
            pageLifecycleProcessedEvent.lifecycleEvent = tPageLifecycleEventInfo.currentEvent.lifecycleEvent;
            pageLifecycleProcessedEvent.startTimeMillis = tPageLifecycleEventInfo.currentEvent.startTimeMillis;
            pageLifecycleProcessedEvent.endTimeMillis = tPageLifecycleEventInfo.currentEvent.endTimeMillis;
            pageLifecycleProcessedEvent.processedInfo = new HashMap<>();
            if ((pageLifecycleProcessedEvent.lifecycleEvent == ActivityLifecycleEvent.ON_DRAW
                    || pageLifecycleProcessedEvent.lifecycleEvent == FragmentLifecycleEvent.ON_DRAW)) {
                long drawTime = PageloadUtil.parsePageDrawTimeMillis(tPageLifecycleEventInfo.allEvents);
                pageLifecycleProcessedEvent.processedInfo.put("drawTime", drawTime);
            }
            if ((pageLifecycleProcessedEvent.lifecycleEvent == ActivityLifecycleEvent.ON_LOAD
                    || pageLifecycleProcessedEvent.lifecycleEvent == FragmentLifecycleEvent.ON_LOAD)) {
                long loadTime = PageloadUtil.parsePageloadTimeMillis(tPageLifecycleEventInfo.allEvents);
                pageLifecycleProcessedEvent.processedInfo.put("loadTime", loadTime);
            }
            return pageLifecycleProcessedEvent;
        };
    }

    private Predicate<List<CrashInfo>> crashPredicate() {
        return crashInfos -> crashInfos != null && !crashInfos.isEmpty();
    }

    private Function<BlockInfo, BlockSimpleInfo> blockMap() {
        return BlockSimpleInfo::new;
    }

    private Function<NetworkInfo, NetworkSummaryInfo> networkMap() {
        return NetworkSummaryInfo::convert;
    }
}

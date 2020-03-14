package cn.hikyson.godeye.core.internal.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeNotificationConfigImpl;
import cn.hikyson.godeye.core.helper.PredicateConverterKt;
import cn.hikyson.godeye.core.helper.RxModule;
import cn.hikyson.godeye.core.internal.modules.appsize.AppSizeInfo;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryInfo;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuInfo;
import cn.hikyson.godeye.core.internal.modules.fps.FpsInfo;
import cn.hikyson.godeye.core.internal.modules.imagecanary.ImageIssue;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakQueue;
import cn.hikyson.godeye.core.internal.modules.memory.HeapInfo;
import cn.hikyson.godeye.core.internal.modules.memory.PssInfo;
import cn.hikyson.godeye.core.internal.modules.memory.RamInfo;
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodsRecordInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.ActivityLifecycleEvent;
import cn.hikyson.godeye.core.internal.modules.pageload.FragmentLifecycleEvent;
import cn.hikyson.godeye.core.internal.modules.pageload.PageLifecycleEventInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadUtil;
import cn.hikyson.godeye.core.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficInfo;
import cn.hikyson.godeye.core.internal.modules.viewcanary.ViewIssueInfo;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

public class ModuleObserverNotifier {
    private CompositeDisposable mCompositeDisposable;
    private List<Notification> mNotifications = new ArrayList<>();
    private boolean mIsRunning;

    public void addNotification(Notification notification) {
        mNotifications.add(notification);
    }

    public void removeNotification(Notification notification) {
        mNotifications.remove(notification);
    }

    /**
     * 监听所有的数据
     */
    public synchronized void start() {
        if (mIsRunning) {
            return;
        }
        mIsRunning = true;
        NotificationConsumer notificationConsumer = new NotificationConsumer(this.mNotifications);
        GodEyeNotificationConfigImpl godEyeNotificationConfig = new GodEyeNotificationConfigImpl();

        L.d("ModuleDriver start running.");
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.addAll(
                // TODO KYSON IMPL
                RxModule.<BatteryInfo>wrapThreadComputationObservable(GodEye.ModuleName.BATTERY)
                        .map(BatteryInfoFactory.converter())
                        .map(this.createConvertServerMessageFunction("batteryInfo"))
                        .subscribe(notificationConsumer),
                RxModule.<CpuInfo>wrapThreadComputationObservable(GodEye.ModuleName.CPU)
                        .filter(PredicateConverterKt.convert(godEyeNotificationConfig.cpuPredicate()))
                        .map(cpuInfoFunction())
                        .subscribe(notificationConsumer),
                RxModule.<TrafficInfo>wrapThreadComputationObservable(GodEye.ModuleName.TRAFFIC)
                        .map(this.createConvertServerMessageFunction("trafficInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<FpsInfo>wrapThreadComputationObservable(GodEye.ModuleName.FPS)
                        .map(this.createConvertServerMessageFunction("fpsInfo"))
                        .subscribe(this.createSendMessageConsumer()),
                RxModule.<LeakQueue.LeakMemoryInfo>wrapThreadComputationObservable(GodEye.ModuleName.LEAK)
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
                RxModule.<List<Thread>>wrapThreadComputationObservable(GodEye.ModuleName.THREAD)
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
                RxModule.wrapThreadComputationObservable(CrashStore.observeCrashAndCache(GodEye.instance().getApplication()))
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

    private Function<CpuInfo, NotificationContent> cpuInfoFunction() {
        return new Function<CpuInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(CpuInfo cpuInfo) throws Exception {
                return new NotificationContent("Cpu too high", null);
            }
        };
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

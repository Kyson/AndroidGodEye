package cn.hikyson.godeye.core.internal.notification;

import java.util.ArrayList;
import java.util.List;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.helper.CrashStore;
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
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.PageLifecycleEventInfo;
import cn.hikyson.godeye.core.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadInfo;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficInfo;
import cn.hikyson.godeye.core.internal.modules.viewcanary.ViewIssueInfo;
import io.reactivex.disposables.CompositeDisposable;

public class NotificationObserver {
    private CompositeDisposable mCompositeDisposable;
    private LocalNotificationListener mLocalNotificationListener;
    private final List<NotificationListener> mNotificationListeners = new ArrayList<>();

    private NotificationObserver() {
    }

    private static class InstanceHolder {
        private static NotificationObserver sInstance = new NotificationObserver();
    }

    public static NotificationObserver get() {
        return InstanceHolder.sInstance;
    }

    public void addNotificationListener(NotificationListener notification) {
        synchronized (mNotificationListeners) {
            mNotificationListeners.add(notification);
        }
    }

    public void removeNotificationListener(NotificationListener notification) {
        synchronized (mNotificationListeners) {
            mNotificationListeners.remove(notification);
        }
    }

    public synchronized void install(NotificationConfig godEyeNotificationConfig) {
        mLocalNotificationListener = new LocalNotificationListener();
        addNotificationListener(mLocalNotificationListener);
        mLocalNotificationListener.start();
        NotificationConsumer notificationConsumer = new NotificationConsumer(this.mNotificationListeners);
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.addAll(
                RxModule.<BatteryInfo>wrapThreadComputationObservable(GodEye.ModuleName.BATTERY)
                        .filter(godEyeNotificationConfig.batteryPredicate())
                        .map(godEyeNotificationConfig.batteryConverter())
                        .subscribe(notificationConsumer),
                RxModule.<CpuInfo>wrapThreadComputationObservable(GodEye.ModuleName.CPU)
                        .filter(godEyeNotificationConfig.cpuPredicate())
                        .map(godEyeNotificationConfig.cpuConverter())
                        .subscribe(notificationConsumer),
                RxModule.<TrafficInfo>wrapThreadComputationObservable(GodEye.ModuleName.TRAFFIC)
                        .filter(godEyeNotificationConfig.trafficPredicate())
                        .map(godEyeNotificationConfig.trafficConverter())
                        .subscribe(notificationConsumer),
                RxModule.<FpsInfo>wrapThreadComputationObservable(GodEye.ModuleName.FPS)
                        .filter(godEyeNotificationConfig.fpsPredicate())
                        .map(godEyeNotificationConfig.fpsConverter())
                        .subscribe(notificationConsumer),
                RxModule.<LeakQueue.LeakMemoryInfo>wrapThreadComputationObservable(GodEye.ModuleName.LEAK)
                        .filter(godEyeNotificationConfig.leakPredicate())
                        .map(godEyeNotificationConfig.leakConverter())
                        .subscribe(notificationConsumer),
                RxModule.<BlockInfo>wrapThreadComputationObservable(GodEye.ModuleName.SM)
                        .filter(godEyeNotificationConfig.smPredicate())
                        .map(godEyeNotificationConfig.smConverter())
                        .subscribe(notificationConsumer),
                RxModule.<NetworkInfo>wrapThreadComputationObservable(GodEye.ModuleName.NETWORK)
                        .filter(godEyeNotificationConfig.networkPredicate())
                        .map(godEyeNotificationConfig.networkConverter())
                        .subscribe(notificationConsumer),
                RxModule.<StartupInfo>wrapThreadComputationObservable(GodEye.ModuleName.STARTUP)
                        .filter(godEyeNotificationConfig.startupPredicate())
                        .map(godEyeNotificationConfig.startupConverter())
                        .subscribe(notificationConsumer),
                RxModule.<RamInfo>wrapThreadComputationObservable(GodEye.ModuleName.RAM)
                        .filter(godEyeNotificationConfig.ramPredicate())
                        .map(godEyeNotificationConfig.ramConverter())
                        .subscribe(notificationConsumer),
                RxModule.<PssInfo>wrapThreadComputationObservable(GodEye.ModuleName.PSS)
                        .filter(godEyeNotificationConfig.pssPredicate())
                        .map(godEyeNotificationConfig.pssConverter())
                        .subscribe(notificationConsumer),
                RxModule.<HeapInfo>wrapThreadComputationObservable(GodEye.ModuleName.HEAP)
                        .filter(godEyeNotificationConfig.heapPredicate())
                        .map(godEyeNotificationConfig.heapConverter())
                        .subscribe(notificationConsumer),
                RxModule.<List<ThreadInfo>>wrapThreadComputationObservable(GodEye.ModuleName.THREAD)
                        .filter(godEyeNotificationConfig.threadPredicate())
                        .map(godEyeNotificationConfig.threadConverter())
                        .subscribe(notificationConsumer),
                RxModule.<PageLifecycleEventInfo>wrapThreadComputationObservable(GodEye.ModuleName.PAGELOAD)
                        .filter(godEyeNotificationConfig.pageloadPredicate())
                        .map(godEyeNotificationConfig.pageloadConverter())
                        .subscribe(notificationConsumer),
                RxModule.<MethodsRecordInfo>wrapThreadComputationObservable(GodEye.ModuleName.METHOD_CANARY)
                        .filter(godEyeNotificationConfig.methodCanaryPredicate())
                        .map(godEyeNotificationConfig.methodCanaryConverter())
                        .subscribe(notificationConsumer),
                RxModule.<AppSizeInfo>wrapThreadComputationObservable(GodEye.ModuleName.APP_SIZE)
                        .filter(godEyeNotificationConfig.appSizePredicate())
                        .map(godEyeNotificationConfig.appSizeConverter())
                        .subscribe(notificationConsumer),
                RxModule.<ViewIssueInfo>wrapThreadComputationObservable(GodEye.ModuleName.VIEW_CANARY)
                        .filter(godEyeNotificationConfig.viewCanaryPredicate())
                        .map(godEyeNotificationConfig.viewCanaryConverter())
                        .subscribe(notificationConsumer),
                RxModule.<ImageIssue>wrapThreadComputationObservable(GodEye.ModuleName.IMAGE_CANARY)
                        .filter(godEyeNotificationConfig.imageCanaryPredicate())
                        .map(godEyeNotificationConfig.imageCanaryConverter())
                        .subscribe(notificationConsumer),
                RxModule.wrapThreadComputationObservable(CrashStore.observeCrashAndCache(GodEye.instance().getApplication(), "crash_of_notification"))
                        .filter(godEyeNotificationConfig.crashPredicate())
                        .map(godEyeNotificationConfig.crashConverter())
                        .subscribe(notificationConsumer)
        );
    }

    public synchronized void uninstall() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
        if (mLocalNotificationListener != null) {
            removeNotificationListener(mLocalNotificationListener);
            mLocalNotificationListener.stop();
            mLocalNotificationListener = null;
        }
    }
}

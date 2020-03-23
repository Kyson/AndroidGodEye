package cn.hikyson.godeye.core.internal.notification;


import androidx.annotation.NonNull;

import java.util.List;

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
import cn.hikyson.godeye.core.internal.modules.pageload.PageLifecycleEventInfo;
import cn.hikyson.godeye.core.internal.modules.sm.BlockInfo;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadInfo;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficInfo;
import cn.hikyson.godeye.core.internal.modules.viewcanary.ViewIssueInfo;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public interface NotificationConfig {
    @NonNull
    Predicate<CpuInfo> cpuPredicate();

    @NonNull
    Function<CpuInfo, NotificationContent> cpuConverter();

    @NonNull
    Predicate<BatteryInfo> batteryPredicate();

    @NonNull
    Function<BatteryInfo, NotificationContent> batteryConverter();

    @NonNull
    Predicate<FpsInfo> fpsPredicate();

    @NonNull
    Function<FpsInfo, NotificationContent> fpsConverter();

    @NonNull
    Predicate<LeakInfo> leakPredicate();

    @NonNull
    Function<LeakInfo, NotificationContent> leakConverter();

    @NonNull
    Predicate<HeapInfo> heapPredicate();

    @NonNull
    Function<HeapInfo, NotificationContent> heapConverter();

    @NonNull
    Predicate<PssInfo> pssPredicate();

    @NonNull
    Function<PssInfo, NotificationContent> pssConverter();

    @NonNull
    Predicate<RamInfo> ramPredicate();

    @NonNull
    Function<RamInfo, NotificationContent> ramConverter();

    @NonNull
    Predicate<NetworkInfo> networkPredicate();

    @NonNull
    Function<NetworkInfo, NotificationContent> networkConverter();

    @NonNull
    Predicate<BlockInfo> smPredicate();

    @NonNull
    Function<BlockInfo, NotificationContent> smConverter();

    @NonNull
    Predicate<StartupInfo> startupPredicate();

    @NonNull
    Function<StartupInfo, NotificationContent> startupConverter();

    @NonNull
    Predicate<TrafficInfo> trafficPredicate();

    @NonNull
    Function<TrafficInfo, NotificationContent> trafficConverter();

    @NonNull
    Predicate<List<CrashInfo>> crashPredicate();

    @NonNull
    Function<List<CrashInfo>, NotificationContent> crashConverter();

    @NonNull
    Predicate<List<ThreadInfo>> threadPredicate();

    @NonNull
    Function<List<ThreadInfo>, NotificationContent> threadConverter();

    @NonNull
    Predicate<PageLifecycleEventInfo> pageloadPredicate();

    @NonNull
    Function<PageLifecycleEventInfo, NotificationContent> pageloadConverter();

    @NonNull
    Predicate<MethodsRecordInfo> methodCanaryPredicate();

    @NonNull
    Function<MethodsRecordInfo, NotificationContent> methodCanaryConverter();

    @NonNull
    Predicate<AppSizeInfo> appSizePredicate();

    @NonNull
    Function<AppSizeInfo, NotificationContent> appSizeConverter();

    @NonNull
    Predicate<ViewIssueInfo> viewCanaryPredicate();

    @NonNull
    Function<ViewIssueInfo, NotificationContent> viewCanaryConverter();

    @NonNull
    Predicate<ImageIssue> imageCanaryPredicate();

    @NonNull
    Function<ImageIssue, NotificationContent> imageCanaryConverter();

}

package cn.hikyson.godeye.core.internal.notification;


import androidx.annotation.NonNull;

import java.util.List;
import java.util.Locale;

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
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class DefaultNotificationConfig implements NotificationConfig {

    @Override
    public @NonNull
    Predicate<CpuInfo> cpuPredicate() {
        return new Predicate<CpuInfo>() {
            @Override
            public boolean test(CpuInfo info) throws Exception {
                return info.appCpuRatio > 0.8;
            }
        };
    }

    @NonNull
    @Override
    public Function<CpuInfo, NotificationContent> cpuConverter() {
        return new Function<CpuInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(CpuInfo info) {
                return new NotificationContent(String.format("CPU usage too high(Cpu占用过高): %s%%", info.appCpuRatio * 100), null);
            }
        };
    }

    @Override
    public @NonNull
    Predicate<BatteryInfo> batteryPredicate() {
        return new Predicate<BatteryInfo>() {
            @Override
            public boolean test(BatteryInfo info) throws Exception {
                return false;
            }
        };
    }


    @NonNull
    @Override
    public Function<BatteryInfo, NotificationContent> batteryConverter() {
        return new Function<BatteryInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(BatteryInfo info) {
                return new NotificationContent("", null);
            }
        };
    }


    @Override
    public @NonNull
    Predicate<FpsInfo> fpsPredicate() {
        return new Predicate<FpsInfo>() {
            @Override
            public boolean test(FpsInfo info) throws Exception {
                return (info.currentFps * 1.0 / info.systemFps) < 0.5f;
            }
        };
    }

    @NonNull
    @Override
    public Function<FpsInfo, NotificationContent> fpsConverter() {
        return new Function<FpsInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(FpsInfo info) {
                return new NotificationContent(String.format("Fps too low(帧率过低): %s/%s", info.currentFps, info.systemFps), null);
            }
        };
    }


    @Override
    public @NonNull
    Predicate<LeakInfo> leakPredicate() {
        return new Predicate<LeakInfo>() {
            @Override
            public boolean test(LeakInfo info) throws Exception {
                return true;
            }
        };
    }

    @NonNull
    @Override
    public Function<LeakInfo, NotificationContent> leakConverter() {
        return new Function<LeakInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(LeakInfo info) {
                int leakSize = info.info.getTotalRetainedHeapByteSize() == null ? 0 : info.info.getTotalRetainedHeapByteSize();
                return new NotificationContent(String.format("Memory leak %s, %s bytes", info.info.getLeakTraces().get(0).getLeakingObject().getClassSimpleName(), leakSize), null);
            }
        };
    }

    @Override
    public @NonNull
    Predicate<HeapInfo> heapPredicate() {
        return new Predicate<HeapInfo>() {
            @Override
            public boolean test(HeapInfo info) throws Exception {
                return info.allocatedKb * 1.0 / info.maxMemKb > 0.9;
            }
        };
    }

    @NonNull
    @Override
    public Function<HeapInfo, NotificationContent> heapConverter() {
        return new Function<HeapInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(HeapInfo info) {
                return new NotificationContent(String.format(Locale.US, "Heap usage too high(堆内存使用过高): %.1f%%, %s/%s(KB)", (info.allocatedKb * 100.0 / info.maxMemKb), info.allocatedKb, info.maxMemKb), null);
            }
        };
    }

    @Override
    public @NonNull
    Predicate<PssInfo> pssPredicate() {
        return new Predicate<PssInfo>() {
            @Override
            public boolean test(PssInfo info) throws Exception {
                return false;
            }
        };
    }

    @NonNull
    @Override
    public Function<PssInfo, NotificationContent> pssConverter() {
        return new Function<PssInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(PssInfo info) {
                return new NotificationContent("", null);
            }
        };
    }


    @Override
    public @NonNull
    Predicate<RamInfo> ramPredicate() {
        return new Predicate<RamInfo>() {
            @Override
            public boolean test(RamInfo info) throws Exception {
                return info.isLowMemory;
            }
        };
    }

    @NonNull
    @Override
    public Function<RamInfo, NotificationContent> ramConverter() {
        return new Function<RamInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(RamInfo info) {
                return new NotificationContent(String.format(Locale.US, "Ram usage too high(手机内存使用过高): %.1f%%, Total: %.1f(GB)", ((info.totalMemKb - info.availMemKb) * 100.0 / info.totalMemKb), info.totalMemKb / (1024f * 1024)), null);
            }
        };
    }

    @Override
    public @NonNull
    Predicate<NetworkInfo> networkPredicate() {
        return new Predicate<NetworkInfo>() {
            @Override
            public boolean test(NetworkInfo info) throws Exception {
                return !info.isSuccessful;
            }
        };
    }

    @NonNull
    @Override
    public Function<NetworkInfo, NotificationContent> networkConverter() {
        return new Function<NetworkInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(NetworkInfo networkInfo) throws Exception {
                return new NotificationContent(String.format(Locale.US, "Network request failed.(网络请求失败): %s", networkInfo.summary), null);
            }
        };
    }

    @Override
    public @NonNull
    Predicate<BlockInfo> smPredicate() {
        return new Predicate<BlockInfo>() {
            @Override
            public boolean test(BlockInfo blockInfo) throws Exception {
                return blockInfo.blockType.equals(BlockInfo.BlockType.LONG);
            }
        };
    }

    @NonNull
    @Override
    public Function<BlockInfo, NotificationContent> smConverter() {
        return new Function<BlockInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(BlockInfo blockInfo) throws Exception {
                return new NotificationContent(String.format(Locale.US, "Jank happened.(发生长卡顿): %sms", blockInfo.blockTime()), null);
            }
        };
    }

    @Override
    public @NonNull
    Predicate<StartupInfo> startupPredicate() {
        return new Predicate<StartupInfo>() {
            @Override
            public boolean test(StartupInfo info) throws Exception {
                return info.startupTime > 5000;
            }
        };
    }

    @NonNull
    @Override
    public Function<StartupInfo, NotificationContent> startupConverter() {
        return new Function<StartupInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(StartupInfo info) throws Exception {
                return new NotificationContent(String.format(Locale.US, "Startup timeout.(启动超时): %sms", info.startupTime), null);
            }
        };
    }


    @Override
    public @NonNull
    Predicate<TrafficInfo> trafficPredicate() {
        return new Predicate<TrafficInfo>() {
            @Override
            public boolean test(TrafficInfo info) throws Exception {
                return false;
            }
        };
    }

    @NonNull
    @Override
    public Function<TrafficInfo, NotificationContent> trafficConverter() {
        return new Function<TrafficInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(TrafficInfo info) throws Exception {
                return new NotificationContent("", null);
            }
        };
    }

    @Override
    public @NonNull
    Predicate<List<CrashInfo>> crashPredicate() {
        return new Predicate<List<CrashInfo>>() {
            @Override
            public boolean test(List<CrashInfo> info) throws Exception {
                return info != null && !info.isEmpty();
            }
        };
    }

    @NonNull
    @Override
    public Function<List<CrashInfo>, NotificationContent> crashConverter() {
        return new Function<List<CrashInfo>, NotificationContent>() {
            @Override
            public NotificationContent apply(List<CrashInfo> info) throws Exception {
                return new NotificationContent(String.format(Locale.US, "Crash happened last usage!(上次使用发生崩溃！): %s", info.get(0).crashMessage), null);
            }
        };
    }

    @Override
    public @NonNull
    Predicate<List<ThreadInfo>> threadPredicate() {
        return new Predicate<List<ThreadInfo>>() {
            @Override
            public boolean test(List<ThreadInfo> info) throws Exception {
                return info.size() > 500;
            }
        };
    }

    @NonNull
    @Override
    public Function<List<ThreadInfo>, NotificationContent> threadConverter() {
        return new Function<List<ThreadInfo>, NotificationContent>() {
            @Override
            public NotificationContent apply(List<ThreadInfo> info) throws Exception {
                return new NotificationContent(String.format(Locale.US, "Threads too many（线程数太多): %s", info.size()), null);
            }
        };
    }

    @Override
    public @NonNull
    Predicate<PageLifecycleEventInfo> pageloadPredicate() {
        return new Predicate<PageLifecycleEventInfo>() {
            @Override
            public boolean test(PageLifecycleEventInfo info) throws Exception {
                if (info.currentEvent.lifecycleEvent.isSystemLifecycle()) {
                    return getPageLifecycleEventCostTime(info) > 2000;
                }
                if ((info.currentEvent.lifecycleEvent == ActivityLifecycleEvent.ON_DRAW
                        || info.currentEvent.lifecycleEvent == FragmentLifecycleEvent.ON_DRAW)) {
                    return getPageLifecycleEventCostTime(info) > 2000;
                }
                if ((info.currentEvent.lifecycleEvent == ActivityLifecycleEvent.ON_LOAD
                        || info.currentEvent.lifecycleEvent == FragmentLifecycleEvent.ON_LOAD)) {
                    return getPageLifecycleEventCostTime(info) > 8000;
                }
                return false;
            }
        };
    }

    private long getPageLifecycleEventCostTime(PageLifecycleEventInfo info) {
        if (info.currentEvent.lifecycleEvent.isSystemLifecycle()) {
            return info.currentEvent.endTimeMillis - info.currentEvent.startTimeMillis;
        }
        if ((info.currentEvent.lifecycleEvent == ActivityLifecycleEvent.ON_DRAW
                || info.currentEvent.lifecycleEvent == FragmentLifecycleEvent.ON_DRAW)) {
            long drawTime = PageloadUtil.parsePageDrawTimeMillis(info.allEvents);
            return drawTime;
        }
        if ((info.currentEvent.lifecycleEvent == ActivityLifecycleEvent.ON_LOAD
                || info.currentEvent.lifecycleEvent == FragmentLifecycleEvent.ON_LOAD)) {
            long loadTime = PageloadUtil.parsePageloadTimeMillis(info.allEvents);
            return loadTime;
        }
        return 0;
    }

    @NonNull
    @Override
    public Function<PageLifecycleEventInfo, NotificationContent> pageloadConverter() {
        return new Function<PageLifecycleEventInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(PageLifecycleEventInfo info) throws Exception {
                return new NotificationContent(String.format(Locale.US, "Page[%s.%s]timeout(页面[%s.%s]超时): %sms",
                        info.pageInfo.pageClassName, info.currentEvent.lifecycleEvent, info.pageInfo.pageClassName, info.currentEvent.lifecycleEvent, getPageLifecycleEventCostTime(info)), null);
            }
        };
    }

    @Override
    public @NonNull
    Predicate<MethodsRecordInfo> methodCanaryPredicate() {
        return new Predicate<MethodsRecordInfo>() {
            @Override
            public boolean test(MethodsRecordInfo info) throws Exception {
                return false;
            }
        };
    }

    @NonNull
    @Override
    public Function<MethodsRecordInfo, NotificationContent> methodCanaryConverter() {
        return new Function<MethodsRecordInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(MethodsRecordInfo methodsRecordInfo) throws Exception {
                return new NotificationContent("", null);
            }
        };
    }

    @Override
    public @NonNull
    Predicate<AppSizeInfo> appSizePredicate() {
        return new Predicate<AppSizeInfo>() {
            @Override
            public boolean test(AppSizeInfo info) throws Exception {
                return false;
            }
        };
    }

    @NonNull
    @Override
    public Function<AppSizeInfo, NotificationContent> appSizeConverter() {
        return new Function<AppSizeInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(AppSizeInfo appSizeInfo) throws Exception {
                return new NotificationContent("", null);
            }
        };
    }

    @Override
    public @NonNull
    Predicate<ViewIssueInfo> viewCanaryPredicate() {
        return new Predicate<ViewIssueInfo>() {
            @Override
            public boolean test(ViewIssueInfo info) throws Exception {
                return hasOverMaxDepthView(info) || overDraw(info);
            }
        };
    }

    @NonNull
    @Override
    public Function<ViewIssueInfo, NotificationContent> viewCanaryConverter() {
        return new Function<ViewIssueInfo, NotificationContent>() {
            @Override
            public NotificationContent apply(ViewIssueInfo viewIssueInfo) throws Exception {
                return new NotificationContent(String.format("Too many layouts nested or too much overdraw(布局层级过深或过度绘制严重): %s", viewIssueInfo.activityName), null);
            }
        };
    }

    private boolean hasOverMaxDepthView(ViewIssueInfo info) {
        for (ViewIssueInfo.ViewInfo viewInfo : info.views) {
            if (viewInfo.depth > info.maxDepth) {
                return true;
            }
        }
        return false;
    }

    private boolean overDraw(ViewIssueInfo info) {
        int overDrawAreaWeight = 0;
        for (ViewIssueInfo.OverDrawArea overDrawArea : info.overDrawAreas) {
            overDrawAreaWeight = overDrawAreaWeight + overDrawArea.rect.width() * overDrawArea.rect.height() * overDrawArea.overDrawTimes;
        }
        return overDrawAreaWeight > (info.screenWidth * info.screenHeight * 2);
    }

    @Override
    public @NonNull
    Predicate<ImageIssue> imageCanaryPredicate() {
        return new Predicate<ImageIssue>() {
            @Override
            public boolean test(ImageIssue info) throws Exception {
                return info.issueType != ImageIssue.IssueType.NONE;
            }
        };
    }

    @NonNull
    @Override
    public Function<ImageIssue, NotificationContent> imageCanaryConverter() {
        return new Function<ImageIssue, NotificationContent>() {
            @Override
            public NotificationContent apply(ImageIssue imageIssue) throws Exception {
                return new NotificationContent(String.format("Improper usage of bitmap memory(图片内存使用不当): %s", imageIssue.issueType), null);
            }
        };
    }
}

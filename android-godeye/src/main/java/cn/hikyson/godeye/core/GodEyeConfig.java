package cn.hikyson.godeye.core;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import cn.hikyson.godeye.core.internal.modules.battery.BatteryContext;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuContext;
import cn.hikyson.godeye.core.internal.modules.crash.CrashFileProvider;
import cn.hikyson.godeye.core.internal.modules.crash.CrashInfo;
import cn.hikyson.godeye.core.internal.modules.crash.CrashProvider;
import cn.hikyson.godeye.core.internal.modules.fps.FpsContext;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakContext;
import cn.hikyson.godeye.core.internal.modules.memory.HeapContext;
import cn.hikyson.godeye.core.internal.modules.memory.PssContext;
import cn.hikyson.godeye.core.internal.modules.memory.RamContext;
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadContext;
import cn.hikyson.godeye.core.internal.modules.sm.SmContext;
import cn.hikyson.godeye.core.internal.modules.thread.ExcludeSystemThreadFilter;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadContext;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadFilter;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficContext;

public class GodEyeConfig {
    public static GodEyeConfig fromAssert(Context context, String path) {
        GodEyeConfigBuilder builder = GodEyeConfigBuilder.godEyeConfig();
        try {
            Element root = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(context.getAssets().open(path)).getDocumentElement();
            Element element = getFirstElementByTagInRoot(root, "battery");
            if (element != null) {
                final String intervalMillisString = element.getAttribute("intervalMillis");
                BatteryConfig batteryConfig = new BatteryConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    batteryConfig.intervalMillis = Long.parseLong(intervalMillisString);
                }
                builder.withBatteryConfig(batteryConfig);
            }
            element = getFirstElementByTagInRoot(root, "cpu");
            if (element != null) {
                final String intervalMillisString = element.getAttribute("intervalMillis");
                final String sampleMillisString = element.getAttribute("sampleMillis");
                CpuConfig cpuConfig = new CpuConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    cpuConfig.intervalMillis = Long.parseLong(intervalMillisString);
                }
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    cpuConfig.sampleMillis = Long.parseLong(sampleMillisString);
                }
                builder.withCpuConfig(cpuConfig);
            }
            element = getFirstElementByTagInRoot(root, "crash");
            if (element != null) {
                final String crashProviderString = element.getAttribute("crashProvider");
                CrashConfig crashConfig = new CrashConfig();
                if (!TextUtils.isEmpty(crashProviderString)) {
                    crashConfig.crashProvider = (CrashProvider) Class.forName(crashProviderString).newInstance();
                }
                builder.withCrashConfig(crashConfig);
            }
            element = getFirstElementByTagInRoot(root, "fps");
            if (element != null) {
                final String intervalMillisString = element.getAttribute("intervalMillis");
                final String sampleMillisString = element.getAttribute("sampleMillis");
                FpsConfig fpsConfig = new FpsConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    fpsConfig.intervalMillis = Long.parseLong(intervalMillisString);
                }
                if (!TextUtils.isEmpty(sampleMillisString)) {
                    fpsConfig.sampleMillis = Long.parseLong(sampleMillisString);
                }
                builder.withFpsConfig(fpsConfig);
            }
            element = getFirstElementByTagInRoot(root, "heap");
            if (element != null) {
                final String intervalMillisString = element.getAttribute("intervalMillis");
                HeapConfig heapConfig = new HeapConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    heapConfig.intervalMillis = Long.parseLong(intervalMillisString);
                }
                builder.withHeapConfig(heapConfig);
            }
            element = getFirstElementByTagInRoot(root, "leakMemory");
            if (element != null) {
                final String debugNotifyString = element.getAttribute("debugNotify");
                LeakConfig leakConfig = new LeakConfig();
                if (!TextUtils.isEmpty(debugNotifyString)) {
                    leakConfig.debugNotify = Boolean.parseBoolean(debugNotifyString);
                }
                builder.withLeakConfig(leakConfig);
            }
            element = getFirstElementByTagInRoot(root, "pageload");
            if (element != null) {
                builder.withPageloadConfig(new PageloadConfig());
            }
            element = getFirstElementByTagInRoot(root, "pss");
            if (element != null) {
                final String intervalMillisString = element.getAttribute("intervalMillis");
                PssConfig pssConfig = new PssConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    pssConfig.intervalMillis = Long.parseLong(intervalMillisString);
                }
                builder.withPssConfig(pssConfig);
            }
            element = getFirstElementByTagInRoot(root, "ram");
            if (element != null) {
                final String intervalMillisString = element.getAttribute("intervalMillis");
                RamConfig ramConfig = new RamConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    ramConfig.intervalMillis = Long.parseLong(intervalMillisString);
                }
                builder.withRamConfig(ramConfig);
            }
            element = getFirstElementByTagInRoot(root, "sm");
            if (element != null) {
                final String longBlockThresholdMillisString = element.getAttribute("longBlockThresholdMillis");
                final String shortBlockThresholdMillisString = element.getAttribute("shortBlockThresholdMillis");
                final String dumpIntervalMillisString = element.getAttribute("dumpIntervalMillis");
                SmConfig smConfig = new SmConfig();
                if (!TextUtils.isEmpty(longBlockThresholdMillisString)) {
                    smConfig.longBlockThresholdMillis = Long.parseLong(longBlockThresholdMillisString);
                }
                if (!TextUtils.isEmpty(shortBlockThresholdMillisString)) {
                    smConfig.shortBlockThresholdMillis = Long.parseLong(shortBlockThresholdMillisString);
                }
                if (!TextUtils.isEmpty(dumpIntervalMillisString)) {
                    smConfig.dumpIntervalMillis = Long.parseLong(dumpIntervalMillisString);
                }
                builder.withSmConfig(smConfig);
            }
            element = getFirstElementByTagInRoot(root, "thread");
            if (element != null) {
                final String intervalMillisString = element.getAttribute("intervalMillis");
                final String threadFilterString = element.getAttribute("threadFilter");
                ThreadConfig threadConfig = new ThreadConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    threadConfig.intervalMillis = Long.parseLong(intervalMillisString);
                }
                if (!TextUtils.isEmpty(threadFilterString)) {
                    threadConfig.threadFilter = (ThreadFilter) Class.forName(threadFilterString).newInstance();
                }
                builder.withThreadConfig(threadConfig);
            }
            element = getFirstElementByTagInRoot(root, "traffic");
            if (element != null) {
                final String intervalMillisString = element.getAttribute("intervalMillis");
                final String sampleMillisString = element.getAttribute("sampleMillis");
                TrafficConfig trafficConfig = new TrafficConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    trafficConfig.intervalMillis = Long.parseLong(intervalMillisString);
                }
                if (!TextUtils.isEmpty(sampleMillisString)) {
                    trafficConfig.sampleMillis = Long.parseLong(sampleMillisString);
                }
                builder.withTrafficConfig(trafficConfig);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static class BatteryConfig implements BatteryContext {
        public long intervalMillis;

        public BatteryConfig(long intervalMillis) {
            this.intervalMillis = intervalMillis;
        }

        public BatteryConfig() {
            this.intervalMillis = 5000;
        }

        @Override
        public Context context() {
            return GodEye.instance().getApplication();
        }

        @Override
        public long intervalMillis() {
            return intervalMillis;
        }
    }

    public static class CpuConfig implements CpuContext {
        public long intervalMillis;
        public long sampleMillis;

        public CpuConfig(long intervalMillis, long sampleMillis) {
            this.intervalMillis = intervalMillis;
            this.sampleMillis = sampleMillis;
        }

        public CpuConfig() {
            this.intervalMillis = 2000;
            this.sampleMillis = 1000;
        }

        @Override
        public long intervalMillis() {
            return intervalMillis;
        }

        @Override
        public long sampleMillis() {
            return sampleMillis;
        }
    }

    public static class CrashConfig implements CrashProvider {
        public CrashProvider crashProvider;

        public CrashConfig(CrashProvider crashProvider) {
            this.crashProvider = crashProvider;
        }

        public CrashConfig() {
            this.crashProvider = new CrashFileProvider(GodEye.instance().getApplication());
        }

        @Override
        public void storeCrash(CrashInfo crashInfo) throws Throwable {
            crashProvider.storeCrash(crashInfo);
        }

        @Override
        public List<CrashInfo> restoreCrash() throws Throwable {
            return crashProvider.restoreCrash();
        }
    }

    public static class FpsConfig implements FpsContext {
        public long intervalMillis;
        //TODO KYSON IMPL
        public long sampleMillis;

        public FpsConfig(long intervalMillis, long sampleMillis) {
            this.intervalMillis = intervalMillis;
            this.sampleMillis = sampleMillis;
        }

        public FpsConfig() {
            this.intervalMillis = 2000;
            this.sampleMillis = 200;
        }

        @Override
        public Context context() {
            return GodEye.instance().getApplication();
        }

        @Override
        public long intervalMillis() {
            return intervalMillis;
        }

        @Override
        public long sampleMillis() {
            return sampleMillis;
        }
    }

    public static class HeapConfig implements HeapContext {
        public long intervalMillis;

        public HeapConfig(long intervalMillis) {
            this.intervalMillis = intervalMillis;
        }

        public HeapConfig() {
            this.intervalMillis = 2000;
        }

        @Override
        public long intervalMillis() {
            return intervalMillis;
        }
    }

    public static class LeakConfig implements LeakContext {
        //TODO KYSON IMPL
        public boolean debugNotify;

        public LeakConfig(boolean debugNotify) {
            this.debugNotify = debugNotify;
        }

        public LeakConfig() {
            this.debugNotify = true;
        }

        @Override
        public Application application() {
            return GodEye.instance().getApplication();
        }

        @Override
        public boolean debugNotify() {
            return debugNotify;
        }
    }

    public static class PageloadConfig implements PageloadContext {
        public PageloadConfig() {
        }

        @Override
        public Application application() {
            return GodEye.instance().getApplication();
        }
    }

    public static class PssConfig implements PssContext {
        public long intervalMillis;

        public PssConfig(long intervalMillis) {
            this.intervalMillis = intervalMillis;
        }

        public PssConfig() {
            this.intervalMillis = 2000;
        }

        @Override
        public Context context() {
            return GodEye.instance().getApplication();
        }

        @Override
        public long intervalMillis() {
            return intervalMillis;
        }
    }

    public static class RamConfig implements RamContext {
        public long intervalMillis;

        public RamConfig(long intervalMillis) {
            this.intervalMillis = intervalMillis;
        }

        public RamConfig() {
            this.intervalMillis = 3000;
        }

        @Override
        public Context context() {
            return GodEye.instance().getApplication();
        }

        @Override
        public long intervalMillis() {
            return intervalMillis;
        }
    }

    public static class SmConfig implements SmContext {
        public long longBlockThresholdMillis;
        public long shortBlockThresholdMillis;
        public long dumpIntervalMillis;

        public SmConfig(long longBlockThresholdMillis, long shortBlockThresholdMillis, long dumpIntervalMillis) {
            this.longBlockThresholdMillis = longBlockThresholdMillis;
            this.shortBlockThresholdMillis = shortBlockThresholdMillis;
            this.dumpIntervalMillis = dumpIntervalMillis;
        }

        public SmConfig() {
            this.longBlockThresholdMillis = 500;
            this.shortBlockThresholdMillis = 100;
            this.dumpIntervalMillis = 300;
        }

        @Override
        public Context context() {
            return GodEye.instance().getApplication();
        }

        @Override
        public long longBlockThreshold() {
            return longBlockThresholdMillis;
        }

        @Override
        public long shortBlockThreshold() {
            return shortBlockThresholdMillis;
        }

        @Override
        public long dumpInterval() {
            return dumpIntervalMillis;
        }
    }

    public static class ThreadConfig implements ThreadContext {
        public long intervalMillis;
        public ThreadFilter threadFilter;

        public ThreadConfig(long intervalMillis, ThreadFilter threadFilter) {
            this.intervalMillis = intervalMillis;
            this.threadFilter = threadFilter;
        }

        public ThreadConfig() {
            this.intervalMillis = 2000;
            this.threadFilter = new ExcludeSystemThreadFilter();
        }

        @Override
        public long intervalMillis() {
            return intervalMillis;
        }

        @Override
        public ThreadFilter threadFilter() {
            return threadFilter;
        }
    }

    public static class TrafficConfig implements TrafficContext {
        public long intervalMillis;
        public long sampleMillis;

        public TrafficConfig(long intervalMillis, long sampleMillis) {
            this.intervalMillis = intervalMillis;
            this.sampleMillis = sampleMillis;
        }

        public TrafficConfig() {
            this.intervalMillis = 2000;
            this.sampleMillis = 1000;
        }

        @Override
        public long intervalMillis() {
            return intervalMillis;
        }

        @Override
        public long sampleMillis() {
            return sampleMillis;
        }
    }

    private BatteryConfig mBatteryConfig;
    private CpuConfig mCpuConfig;
    private CrashConfig mCrashConfig;
    private FpsConfig mFpsConfig;
    private HeapConfig mHeapConfig;
    private LeakConfig mLeakConfig;
    private PageloadConfig mPageloadConfig;
    private PssConfig mPssConfig;
    private RamConfig mRamConfig;
    private SmConfig mSmConfig;
    private ThreadConfig mThreadConfig;
    private TrafficConfig mTrafficConfig;

    public BatteryConfig getBatteryConfig() {
        return mBatteryConfig;
    }

    public void setBatteryConfig(BatteryConfig batteryConfig) {
        mBatteryConfig = batteryConfig;
    }

    public CpuConfig getCpuConfig() {
        return mCpuConfig;
    }

    public void setCpuConfig(CpuConfig cpuConfig) {
        mCpuConfig = cpuConfig;
    }

    public CrashConfig getCrashConfig() {
        return mCrashConfig;
    }

    public void setCrashConfig(CrashConfig crashConfig) {
        mCrashConfig = crashConfig;
    }

    public FpsConfig getFpsConfig() {
        return mFpsConfig;
    }

    public void setFpsConfig(FpsConfig fpsConfig) {
        mFpsConfig = fpsConfig;
    }

    public HeapConfig getHeapConfig() {
        return mHeapConfig;
    }

    public void setHeapConfig(HeapConfig heapConfig) {
        mHeapConfig = heapConfig;
    }

    public LeakConfig getLeakConfig() {
        return mLeakConfig;
    }

    public void setLeakConfig(LeakConfig leakConfig) {
        mLeakConfig = leakConfig;
    }

    public PageloadConfig getPageloadConfig() {
        return mPageloadConfig;
    }

    public void setPageloadConfig(PageloadConfig pageloadConfig) {
        mPageloadConfig = pageloadConfig;
    }

    public PssConfig getPssConfig() {
        return mPssConfig;
    }

    public void setPssConfig(PssConfig pssConfig) {
        mPssConfig = pssConfig;
    }

    public RamConfig getRamConfig() {
        return mRamConfig;
    }

    public void setRamConfig(RamConfig ramConfig) {
        mRamConfig = ramConfig;
    }

    public SmConfig getSmConfig() {
        return mSmConfig;
    }

    public void setSmConfig(SmConfig smConfig) {
        mSmConfig = smConfig;
    }

    public ThreadConfig getThreadConfig() {
        return mThreadConfig;
    }

    public void setThreadConfig(ThreadConfig threadConfig) {
        mThreadConfig = threadConfig;
    }

    public TrafficConfig getTrafficConfig() {
        return mTrafficConfig;
    }

    public void setTrafficConfig(TrafficConfig trafficConfig) {
        mTrafficConfig = trafficConfig;
    }

    private static @Nullable
    Element getFirstElementByTagInRoot(Element root, String moduleName) {
        NodeList elements = root.getElementsByTagName(moduleName);
        if (elements != null && elements.getLength() == 1) {
            return (Element) elements.item(0);
        }
        return null;
    }

    public static final class GodEyeConfigBuilder {
        private BatteryConfig mBatteryConfig;
        private CpuConfig mCpuConfig;
        private CrashConfig mCrashConfig;
        private FpsConfig mFpsConfig;
        private HeapConfig mHeapConfig;
        private LeakConfig mLeakConfig;
        private PageloadConfig mPageloadConfig;
        private PssConfig mPssConfig;
        private RamConfig mRamConfig;
        private SmConfig mSmConfig;
        private ThreadConfig mThreadConfig;
        private TrafficConfig mTrafficConfig;

        private GodEyeConfigBuilder() {
        }

        public static GodEyeConfigBuilder godEyeConfig() {
            return new GodEyeConfigBuilder();
        }

        public GodEyeConfigBuilder withBatteryConfig(BatteryConfig BatteryConfig) {
            this.mBatteryConfig = BatteryConfig;
            return this;
        }

        public GodEyeConfigBuilder withCpuConfig(CpuConfig CpuConfig) {
            this.mCpuConfig = CpuConfig;
            return this;
        }

        public GodEyeConfigBuilder withCrashConfig(CrashConfig CrashConfig) {
            this.mCrashConfig = CrashConfig;
            return this;
        }

        public GodEyeConfigBuilder withFpsConfig(FpsConfig FpsConfig) {
            this.mFpsConfig = FpsConfig;
            return this;
        }

        public GodEyeConfigBuilder withHeapConfig(HeapConfig HeapConfig) {
            this.mHeapConfig = HeapConfig;
            return this;
        }

        public GodEyeConfigBuilder withLeakConfig(LeakConfig LeakConfig) {
            this.mLeakConfig = LeakConfig;
            return this;
        }

        public GodEyeConfigBuilder withPageloadConfig(PageloadConfig PageloadConfig) {
            this.mPageloadConfig = PageloadConfig;
            return this;
        }

        public GodEyeConfigBuilder withPssConfig(PssConfig PssConfig) {
            this.mPssConfig = PssConfig;
            return this;
        }

        public GodEyeConfigBuilder withRamConfig(RamConfig RamConfig) {
            this.mRamConfig = RamConfig;
            return this;
        }

        public GodEyeConfigBuilder withSmConfig(SmConfig SmConfig) {
            this.mSmConfig = SmConfig;
            return this;
        }

        public GodEyeConfigBuilder withThreadConfig(ThreadConfig ThreadConfig) {
            this.mThreadConfig = ThreadConfig;
            return this;
        }

        public GodEyeConfigBuilder withTrafficConfig(TrafficConfig TrafficConfig) {
            this.mTrafficConfig = TrafficConfig;
            return this;
        }

        public GodEyeConfig build() {
            GodEyeConfig godEyeConfig = new GodEyeConfig();
            godEyeConfig.mThreadConfig = this.mThreadConfig;
            godEyeConfig.mCpuConfig = this.mCpuConfig;
            godEyeConfig.mBatteryConfig = this.mBatteryConfig;
            godEyeConfig.mSmConfig = this.mSmConfig;
            godEyeConfig.mPageloadConfig = this.mPageloadConfig;
            godEyeConfig.mPssConfig = this.mPssConfig;
            godEyeConfig.mLeakConfig = this.mLeakConfig;
            godEyeConfig.mHeapConfig = this.mHeapConfig;
            godEyeConfig.mRamConfig = this.mRamConfig;
            godEyeConfig.mTrafficConfig = this.mTrafficConfig;
            godEyeConfig.mCrashConfig = this.mCrashConfig;
            godEyeConfig.mFpsConfig = this.mFpsConfig;
            return godEyeConfig;
        }
    }
}

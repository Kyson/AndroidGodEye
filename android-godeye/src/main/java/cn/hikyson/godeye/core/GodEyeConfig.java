package cn.hikyson.godeye.core;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import cn.hikyson.godeye.core.internal.modules.battery.BatteryContext;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuContext;
import cn.hikyson.godeye.core.internal.modules.crash.CrashFileProvider;
import cn.hikyson.godeye.core.internal.modules.crash.CrashInfo;
import cn.hikyson.godeye.core.internal.modules.crash.CrashProvider;
import cn.hikyson.godeye.core.internal.modules.fps.FpsContext;
import cn.hikyson.godeye.core.internal.modules.leakdetector.DefaultLeakRefInfoProvider;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakContext;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakRefInfoProvider;
import cn.hikyson.godeye.core.internal.modules.memory.HeapContext;
import cn.hikyson.godeye.core.internal.modules.memory.PssContext;
import cn.hikyson.godeye.core.internal.modules.memory.RamContext;
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanaryContext;
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadContext;
import cn.hikyson.godeye.core.internal.modules.sm.SmContext;
import cn.hikyson.godeye.core.internal.modules.thread.ExcludeSystemThreadFilter;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadContext;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadFilter;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficContext;
import cn.hikyson.godeye.core.utils.IoUtil;

public class GodEyeConfig {

    public static GodEyeConfig fromInputStream(InputStream is) {
        GodEyeConfigBuilder builder = GodEyeConfigBuilder.godEyeConfig();
        try {
            if (is == null) {
                throw new IllegalStateException("GodEyeConfig fromInputStream InputStream is null.");
            }
            Element root = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(is).getDocumentElement();
            Element element = getFirstElementByTagInRoot(root, "battery");
            if (element != null) {
                BatteryConfig batteryConfig = new BatteryConfig();
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
                FpsConfig fpsConfig = new FpsConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    fpsConfig.intervalMillis = Long.parseLong(intervalMillisString);
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
                final String debug = element.getAttribute("debug");
                final String debugNotification = element.getAttribute("debugNotification");
                final String leakRefInfoProvider = element.getAttribute("leakRefInfoProvider");
                LeakConfig leakConfig = new LeakConfig();
                if (!TextUtils.isEmpty(debug)) {
                    leakConfig.debug = Boolean.parseBoolean(debug);
                } else {
                    leakConfig.debug = true;
                }
                if (!TextUtils.isEmpty(debugNotification)) {
                    leakConfig.debugNotification = Boolean.parseBoolean(debugNotification);
                }
                if (!TextUtils.isEmpty(leakRefInfoProvider)) {
                    leakConfig.leakRefInfoProvider = (LeakRefInfoProvider) Class.forName(leakRefInfoProvider).newInstance();
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
                final String debugNotifyString = element.getAttribute("debugNotify");
                final String longBlockThresholdMillisString = element.getAttribute("longBlockThresholdMillis");
                final String shortBlockThresholdMillisString = element.getAttribute("shortBlockThresholdMillis");
                final String dumpIntervalMillisString = element.getAttribute("dumpIntervalMillis");
                SmConfig smConfig = new SmConfig();
                if (!TextUtils.isEmpty(debugNotifyString)) {
                    smConfig.debugNotify = Boolean.parseBoolean(debugNotifyString);
                }
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
            element = getFirstElementByTagInRoot(root, "methodCanary");
            if (element != null) {
                final String methodEventCountThresholdString = element.getAttribute("methodEventCountThreshold");
                final String lowCostMethodThresholdString = element.getAttribute("lowCostMethodThreshold");
                MethodCanaryConfig methodCanaryConfig = new MethodCanaryConfig();
                if (!TextUtils.isEmpty(methodEventCountThresholdString)) {
                    methodCanaryConfig.methodEventCountThreshold = Integer.parseInt(methodEventCountThresholdString);
                }
                if (!TextUtils.isEmpty(lowCostMethodThresholdString)) {
                    methodCanaryConfig.lowCostMethodThreshold = Long.parseLong(lowCostMethodThresholdString);
                }
                builder.withMethodCanaryConfig(methodCanaryConfig);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static GodEyeConfig fromAssets(String path) {
        InputStream is = null;
        try {
            is = GodEye.instance().getApplication().getAssets().open(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        GodEyeConfig godEyeConfig = fromInputStream(is);
        IoUtil.closeSilently(is);
        return godEyeConfig;
    }

    public static class BatteryConfig implements BatteryContext {

        public BatteryConfig() {
        }

        @Override
        public Context context() {
            return GodEye.instance().getApplication();
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
            this.crashProvider = new CrashFileProvider();
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

        public FpsConfig(long intervalMillis) {
            this.intervalMillis = intervalMillis;
        }

        public FpsConfig() {
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

        public boolean debug = true;
        public boolean debugNotification = true;
        public LeakRefInfoProvider leakRefInfoProvider;

        @NonNull
        @Override
        public Application application() {
            return GodEye.instance().getApplication();
        }

        @Override
        public boolean debug() {
            return debug;
        }

        @Override
        public boolean debugNotification() {
            return debugNotification;
        }

        @NonNull
        @Override
        public LeakRefInfoProvider leakRefInfoProvider() {
            return leakRefInfoProvider == null ? new DefaultLeakRefInfoProvider() : leakRefInfoProvider;
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
        public boolean debugNotify;

        public SmConfig(boolean debugNotify, long longBlockThresholdMillis, long shortBlockThresholdMillis, long dumpIntervalMillis) {
            this.debugNotify = debugNotify;
            this.longBlockThresholdMillis = longBlockThresholdMillis;
            this.shortBlockThresholdMillis = shortBlockThresholdMillis;
            this.dumpIntervalMillis = dumpIntervalMillis;
        }

        public SmConfig() {
            this.debugNotify = true;
            this.longBlockThresholdMillis = 500;
            this.shortBlockThresholdMillis = 100;
            this.dumpIntervalMillis = 300;
        }

        @Override
        public Context context() {
            return GodEye.instance().getApplication();
        }

        @Override
        public boolean debugNotify() {
            return debugNotify;
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

    public static class MethodCanaryConfig implements MethodCanaryContext {
        public int methodEventCountThreshold;
        public long lowCostMethodThreshold;

        public MethodCanaryConfig() {
            this.methodEventCountThreshold = 1000;
            this.lowCostMethodThreshold = 10L;
        }

        public MethodCanaryConfig(int methodEventCountThreshold, int lowCostMethodThreshold) {
            this.methodEventCountThreshold = methodEventCountThreshold;
            this.lowCostMethodThreshold = lowCostMethodThreshold;
        }

        @Override
        public int methodEventCountThreshold() {
            return methodEventCountThreshold;
        }

        @Override
        public long lowCostMethodThreshold() {
            return lowCostMethodThreshold;
        }

        @Override
        public Application app() {
            return GodEye.instance().getApplication();
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
    private MethodCanaryConfig mMethodCanaryConfig;

    public BatteryConfig getBatteryConfig() {
        return mBatteryConfig;
    }

    public void setBatteryConfig(BatteryConfig batteryConfig) {
        mBatteryConfig = batteryConfig;
    }

    public MethodCanaryConfig getMethodCanaryConfig() {
        return mMethodCanaryConfig;
    }

    public void setMethodCanaryConfig(MethodCanaryConfig methodCanaryConfig) {
        mMethodCanaryConfig = methodCanaryConfig;
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
        private MethodCanaryConfig mMethodCanaryConfig;

        private GodEyeConfigBuilder() {
        }

        public static GodEyeConfigBuilder godEyeConfig() {
            return new GodEyeConfigBuilder();
        }

        public GodEyeConfigBuilder withBatteryConfig(BatteryConfig mBatteryConfig) {
            this.mBatteryConfig = mBatteryConfig;
            return this;
        }

        public GodEyeConfigBuilder withCpuConfig(CpuConfig mCpuConfig) {
            this.mCpuConfig = mCpuConfig;
            return this;
        }

        public GodEyeConfigBuilder withCrashConfig(CrashConfig mCrashConfig) {
            this.mCrashConfig = mCrashConfig;
            return this;
        }

        public GodEyeConfigBuilder withFpsConfig(FpsConfig mFpsConfig) {
            this.mFpsConfig = mFpsConfig;
            return this;
        }

        public GodEyeConfigBuilder withHeapConfig(HeapConfig mHeapConfig) {
            this.mHeapConfig = mHeapConfig;
            return this;
        }

        public GodEyeConfigBuilder withLeakConfig(LeakConfig mLeakConfig) {
            this.mLeakConfig = mLeakConfig;
            return this;
        }

        public GodEyeConfigBuilder withPageloadConfig(PageloadConfig mPageloadConfig) {
            this.mPageloadConfig = mPageloadConfig;
            return this;
        }

        public GodEyeConfigBuilder withPssConfig(PssConfig mPssConfig) {
            this.mPssConfig = mPssConfig;
            return this;
        }

        public GodEyeConfigBuilder withRamConfig(RamConfig mRamConfig) {
            this.mRamConfig = mRamConfig;
            return this;
        }

        public GodEyeConfigBuilder withSmConfig(SmConfig mSmConfig) {
            this.mSmConfig = mSmConfig;
            return this;
        }

        public GodEyeConfigBuilder withThreadConfig(ThreadConfig mThreadConfig) {
            this.mThreadConfig = mThreadConfig;
            return this;
        }

        public GodEyeConfigBuilder withTrafficConfig(TrafficConfig mTrafficConfig) {
            this.mTrafficConfig = mTrafficConfig;
            return this;
        }

        public GodEyeConfigBuilder withMethodCanaryConfig(MethodCanaryConfig mMethodCanaryConfig) {
            this.mMethodCanaryConfig = mMethodCanaryConfig;
            return this;
        }

        public GodEyeConfig build() {
            GodEyeConfig godEyeConfig = new GodEyeConfig();
            godEyeConfig.mRamConfig = this.mRamConfig;
            godEyeConfig.mSmConfig = this.mSmConfig;
            godEyeConfig.mThreadConfig = this.mThreadConfig;
            godEyeConfig.mMethodCanaryConfig = this.mMethodCanaryConfig;
            godEyeConfig.mPssConfig = this.mPssConfig;
            godEyeConfig.mBatteryConfig = this.mBatteryConfig;
            godEyeConfig.mHeapConfig = this.mHeapConfig;
            godEyeConfig.mCpuConfig = this.mCpuConfig;
            godEyeConfig.mLeakConfig = this.mLeakConfig;
            godEyeConfig.mCrashConfig = this.mCrashConfig;
            godEyeConfig.mFpsConfig = this.mFpsConfig;
            godEyeConfig.mTrafficConfig = this.mTrafficConfig;
            godEyeConfig.mPageloadConfig = this.mPageloadConfig;
            return godEyeConfig;
        }
    }
}

package cn.hikyson.godeye.core;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cn.hikyson.godeye.core.internal.modules.crash.CrashFileProvider;
import cn.hikyson.godeye.core.internal.modules.crash.CrashProvider;
import cn.hikyson.godeye.core.internal.modules.thread.ExcludeSystemThreadFilter;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadFilter;

public class GodEyeConfig {

    public static class BatteryConfig {
        public long intervalMillis;

        public BatteryConfig(long intervalMillis) {
            this.intervalMillis = intervalMillis;
        }

        public BatteryConfig() {
            this.intervalMillis = 5000;
        }
    }

    public static class CpuConfig {
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
    }

    public static class CrashConfig {
        public CrashProvider crashProvider;

        public CrashConfig(CrashProvider crashProvider) {
            this.crashProvider = crashProvider;
        }

        public CrashConfig() {
            this.crashProvider = new CrashFileProvider(GodEye.instance().getApplication());
        }
    }

    public static class FpsConfig {
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
    }

    public static class HeapConfig {
        public long intervalMillis;

        public HeapConfig(long intervalMillis) {
            this.intervalMillis = intervalMillis;
        }

        public HeapConfig() {
            this.intervalMillis = 2000;
        }
    }

    public static class LeakConfig {
        //TODO KYSON IMPL
        public boolean debugNotify;

        public LeakConfig(boolean debugNotify) {
            this.debugNotify = debugNotify;
        }

        public LeakConfig() {
            this.debugNotify = true;
        }
    }

    public static class PageloadConfig {
        public PageloadConfig() {
        }
    }

    public static class PssConfig {
        public long intervalMillis;

        public PssConfig(long intervalMillis) {
            this.intervalMillis = intervalMillis;
        }

        public PssConfig() {
            this.intervalMillis = 2000;
        }
    }

    public static class RamConfig {
        public long intervalMillis;

        public RamConfig(long intervalMillis) {
            this.intervalMillis = intervalMillis;
        }

        public RamConfig() {
            this.intervalMillis = 3000;
        }
    }

    public static class SmConfig {
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
    }

    public static class ThreadConfig {
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
    }

    public static class TrafficConfig {
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

    public static GodEyeConfigBuilder fromAssert(Context context, String path) {
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
            //TODO KYSON IMPL
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return builder;
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

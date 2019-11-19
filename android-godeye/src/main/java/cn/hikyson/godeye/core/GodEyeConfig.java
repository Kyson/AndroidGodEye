package cn.hikyson.godeye.core;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilderFactory;

import cn.hikyson.godeye.core.internal.modules.appsize.AppSizeContext;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryContext;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuContext;
import cn.hikyson.godeye.core.internal.modules.crash.CrashContext;
import cn.hikyson.godeye.core.internal.modules.fps.FpsContext;
import cn.hikyson.godeye.core.internal.modules.imagecanary.DefaultImageCanaryConfigProvider;
import cn.hikyson.godeye.core.internal.modules.imagecanary.ImageCanaryContext;
import cn.hikyson.godeye.core.internal.modules.leakdetector.DefaultLeakRefInfoProvider;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakContext;
import cn.hikyson.godeye.core.internal.modules.memory.HeapContext;
import cn.hikyson.godeye.core.internal.modules.memory.PssContext;
import cn.hikyson.godeye.core.internal.modules.memory.RamContext;
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanaryContext;
import cn.hikyson.godeye.core.internal.modules.network.NetworkContext;
import cn.hikyson.godeye.core.internal.modules.pageload.DefaultPageInfoProvider;
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadContext;
import cn.hikyson.godeye.core.internal.modules.sm.SmContext;
import cn.hikyson.godeye.core.internal.modules.startup.StartupContext;
import cn.hikyson.godeye.core.internal.modules.thread.ExcludeSystemThreadFilter;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadContext;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficContext;
import cn.hikyson.godeye.core.internal.modules.viewcanary.ViewCanaryContext;
import cn.hikyson.godeye.core.utils.IoUtil;
import cn.hikyson.godeye.core.utils.L;

/**
 * core config/module config
 */
@Keep
public class GodEyeConfig implements Serializable {

    public static GodEyeConfigBuilder noneConfigBuilder() {
        GodEyeConfigBuilder builder = GodEyeConfigBuilder.godEyeConfig();
        return builder;
    }

    public static GodEyeConfig noneConfig() {
        return noneConfigBuilder().build();
    }

    public static GodEyeConfigBuilder defaultConfigBuilder() {
        GodEyeConfigBuilder builder = GodEyeConfigBuilder.godEyeConfig();
        builder.withCpuConfig(new CpuConfig());
        builder.withBatteryConfig(new BatteryConfig());
        builder.withFpsConfig(new FpsConfig());
        builder.withLeakConfig(new LeakConfig());
        builder.withHeapConfig(new HeapConfig());
        builder.withPssConfig(new PssConfig());
        builder.withRamConfig(new RamConfig());
        builder.withNetworkConfig(new NetworkConfig());
        builder.withSmConfig(new SmConfig());
        builder.withStartupConfig(new StartupConfig());
        builder.withTrafficConfig(new TrafficConfig());
        builder.withCrashConfig(new CrashConfig());
        builder.withThreadConfig(new ThreadConfig());
        builder.withPageloadConfig(new PageloadConfig());
        builder.withMethodCanaryConfig(new MethodCanaryConfig());
        builder.withAppSizeConfig(new AppSizeConfig());
        builder.withViewCanaryConfig(new ViewCanaryConfig());
        builder.withImageCanaryConfig(new ImageCanaryConfig());
        return builder;
    }

    public static GodEyeConfig defaultConfig() {
        return defaultConfigBuilder().build();
    }

    public static GodEyeConfig fromInputStream(InputStream is) {
        GodEyeConfigBuilder builder = GodEyeConfigBuilder.godEyeConfig();
        try {
            if (is == null) {
                throw new IllegalStateException("GodEyeConfig fromInputStream InputStream is null.");
            }
            Element root = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(is).getDocumentElement();
            // cpu
            Element element = getFirstElementByTagInRoot(root, "cpu");
            if (element != null) {
                final String intervalMillisString = element.getAttribute("intervalMillis");
                CpuConfig cpuConfig = new CpuConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    cpuConfig.intervalMillis = Long.parseLong(intervalMillisString);
                }
                builder.withCpuConfig(cpuConfig);
            }
            // battery
            element = getFirstElementByTagInRoot(root, "battery");
            if (element != null) {
                BatteryConfig batteryConfig = new BatteryConfig();
                builder.withBatteryConfig(batteryConfig);
            }
            // fps
            element = getFirstElementByTagInRoot(root, "fps");
            if (element != null) {
                final String intervalMillisString = element.getAttribute("intervalMillis");
                FpsConfig fpsConfig = new FpsConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    fpsConfig.intervalMillis = Long.parseLong(intervalMillisString);
                }
                builder.withFpsConfig(fpsConfig);
            }
            // leak
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
                    leakConfig.leakRefInfoProvider = leakRefInfoProvider;
                }
                builder.withLeakConfig(leakConfig);
            }
            // heap
            element = getFirstElementByTagInRoot(root, "heap");
            if (element != null) {
                final String intervalMillisString = element.getAttribute("intervalMillis");
                HeapConfig heapConfig = new HeapConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    heapConfig.intervalMillis = Long.parseLong(intervalMillisString);
                }
                builder.withHeapConfig(heapConfig);
            }
            // pss
            element = getFirstElementByTagInRoot(root, "pss");
            if (element != null) {
                final String intervalMillisString = element.getAttribute("intervalMillis");
                PssConfig pssConfig = new PssConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    pssConfig.intervalMillis = Long.parseLong(intervalMillisString);
                }
                builder.withPssConfig(pssConfig);
            }
            // ram
            element = getFirstElementByTagInRoot(root, "ram");
            if (element != null) {
                final String intervalMillisString = element.getAttribute("intervalMillis");
                RamConfig ramConfig = new RamConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    ramConfig.intervalMillis = Long.parseLong(intervalMillisString);
                }
                builder.withRamConfig(ramConfig);
            }
            // network
            element = getFirstElementByTagInRoot(root, "network");
            if (element != null) {
                builder.withNetworkConfig(new NetworkConfig());
            }
            // sm
            element = getFirstElementByTagInRoot(root, "sm");
            if (element != null) {
                final String debugNotifyString = element.getAttribute("debugNotification");
                final String longBlockThresholdMillisString = element.getAttribute("longBlockThresholdMillis");
                final String shortBlockThresholdMillisString = element.getAttribute("shortBlockThresholdMillis");
                final String dumpIntervalMillisString = element.getAttribute("dumpIntervalMillis");
                SmConfig smConfig = new SmConfig();
                if (!TextUtils.isEmpty(debugNotifyString)) {
                    smConfig.debugNotification = Boolean.parseBoolean(debugNotifyString);
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
            // startup
            element = getFirstElementByTagInRoot(root, "startup");
            if (element != null) {
                builder.withStartupConfig(new StartupConfig());
            }
            // traffic
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
            // crash
            element = getFirstElementByTagInRoot(root, "crash");
            if (element != null) {
                final String immediateString = element.getAttribute("immediate");
                CrashConfig crashConfig = new CrashConfig();
                if (!TextUtils.isEmpty(immediateString)) {
                    crashConfig.immediate = Boolean.parseBoolean(immediateString);
                }
                builder.withCrashConfig(crashConfig);
            }
            // thread
            element = getFirstElementByTagInRoot(root, "thread");
            if (element != null) {
                final String intervalMillisString = element.getAttribute("intervalMillis");
                final String threadFilterString = element.getAttribute("threadFilter");
                ThreadConfig threadConfig = new ThreadConfig();
                if (!TextUtils.isEmpty(intervalMillisString)) {
                    threadConfig.intervalMillis = Long.parseLong(intervalMillisString);
                }
                if (!TextUtils.isEmpty(threadFilterString)) {
                    threadConfig.threadFilter = threadFilterString;
                }
                builder.withThreadConfig(threadConfig);
            }
            // pageload
            element = getFirstElementByTagInRoot(root, "pageload");
            if (element != null) {
                final String pageInfoProvider = element.getAttribute("pageInfoProvider");
                PageloadConfig pageloadConfig = new PageloadConfig();
                if (!TextUtils.isEmpty(pageInfoProvider)) {
                    pageloadConfig.pageInfoProvider = pageInfoProvider;
                }
                builder.withPageloadConfig(pageloadConfig);
            }
            // methodCanary
            element = getFirstElementByTagInRoot(root, "methodCanary");
            if (element != null) {
                final String maxMethodCountSingleThreadByCostString = element.getAttribute("maxMethodCountSingleThreadByCost");
                final String lowCostMethodThresholdMillisString = element.getAttribute("lowCostMethodThresholdMillis");
                MethodCanaryConfig methodCanaryConfig = new MethodCanaryConfig();
                if (!TextUtils.isEmpty(maxMethodCountSingleThreadByCostString)) {
                    methodCanaryConfig.maxMethodCountSingleThreadByCost = Integer.parseInt(maxMethodCountSingleThreadByCostString);
                }
                if (!TextUtils.isEmpty(lowCostMethodThresholdMillisString)) {
                    methodCanaryConfig.lowCostMethodThresholdMillis = Long.parseLong(lowCostMethodThresholdMillisString);
                }
                builder.withMethodCanaryConfig(methodCanaryConfig);
            }
            // appSize
            element = getFirstElementByTagInRoot(root, "appSize");
            if (element != null) {
                final String delayMillisString = element.getAttribute("delayMillis");
                AppSizeConfig appSizeConfig = new AppSizeConfig();
                if (!TextUtils.isEmpty(delayMillisString)) {
                    appSizeConfig.delayMillis = Long.parseLong(delayMillisString);
                }
                builder.withAppSizeConfig(appSizeConfig);
            }
            // view canary
            element = getFirstElementByTagInRoot(root, "viewCanary");
            if (element != null) {
                final String maxDepth = element.getAttribute("maxDepth");
                ViewCanaryConfig viewCanaryConfig = new ViewCanaryConfig();
                if (!TextUtils.isEmpty(maxDepth)) {
                    viewCanaryConfig.maxDepth = Integer.parseInt(maxDepth);
                }
                builder.withViewCanaryConfig(viewCanaryConfig);
            }
            // image canary
            element = getFirstElementByTagInRoot(root, "imageCanary");
            if (element != null) {
                final String imageCanaryConfigProvider = element.getAttribute("imageCanaryConfigProvider");
                ImageCanaryConfig imageCanaryConfig = new ImageCanaryConfig();
                if (!TextUtils.isEmpty(imageCanaryConfigProvider)) {
                    imageCanaryConfig.imageCanaryConfigProvider = imageCanaryConfigProvider;
                }
                builder.withImageCanaryConfig(imageCanaryConfig);
            }
        } catch (Exception e) {
            L.e(e);
        }
        return builder.build();
    }

    private static @Nullable
    Element getFirstElementByTagInRoot(Element root, String moduleName) {
        NodeList elements = root.getElementsByTagName(moduleName);
        if (elements != null && elements.getLength() == 1) {
            return (Element) elements.item(0);
        }
        return null;
    }

    public static GodEyeConfig fromAssets(String assetsPath) {
        InputStream is = null;
        try {
            is = GodEye.instance().getApplication().getAssets().open(assetsPath);
            return fromInputStream(is);
        } catch (Exception e) {
            L.e(e);
            return GodEyeConfig.noneConfig();
        } finally {
            IoUtil.closeSilently(is);
        }
    }

    @Keep
    public static class CpuConfig implements CpuContext, Serializable {
        public long intervalMillis;

        public CpuConfig(long intervalMillis) {
            this.intervalMillis = intervalMillis;
        }

        public CpuConfig() {
            this.intervalMillis = 2000;
        }

        @Override
        public long intervalMillis() {
            return intervalMillis;
        }

        @Override
        public String toString() {
            return "CpuConfig{" +
                    "intervalMillis=" + intervalMillis +
                    '}';
        }
    }

    @Keep
    public static class BatteryConfig implements BatteryContext, Serializable {

        public BatteryConfig() {
        }

        @Override
        public Context context() {
            return GodEye.instance().getApplication();
        }

        @Override
        public String toString() {
            return "BatteryConfig{}";
        }
    }

    @Keep
    public static class FpsConfig implements FpsContext, Serializable {
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

        @Override
        public String toString() {
            return "FpsConfig{" +
                    "intervalMillis=" + intervalMillis +
                    '}';
        }
    }

    @Keep
    public static class LeakConfig implements LeakContext, Serializable {
        // if you want leak module work in production,set debug false
        public boolean debug;
        public boolean debugNotification;
        //LeakRefInfoProvider
        public String leakRefInfoProvider;

        public LeakConfig(boolean debug, boolean debugNotification, String leakRefInfoProvider) {
            this.debug = debug;
            this.debugNotification = debugNotification;
            this.leakRefInfoProvider = leakRefInfoProvider;
        }

        public LeakConfig() {
            this.debug = true;
            this.debugNotification = true;
            this.leakRefInfoProvider = DefaultLeakRefInfoProvider.class.getName();
        }

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

        // LeakRefInfoProvider
        @NonNull
        @Override
        public String leakRefInfoProvider() {
            return leakRefInfoProvider;
        }

        @Override
        public String toString() {
            return "LeakConfig{" +
                    "debug=" + debug +
                    ", debugNotification=" + debugNotification +
                    ", leakRefInfoProvider=" + leakRefInfoProvider +
                    '}';
        }
    }

    @Keep
    public static class HeapConfig implements HeapContext, Serializable {
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

        @Override
        public String toString() {
            return "HeapConfig{" +
                    "intervalMillis=" + intervalMillis +
                    '}';
        }
    }

    @Keep
    public static class PssConfig implements PssContext, Serializable {
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

        @Override
        public String toString() {
            return "PssConfig{" +
                    "intervalMillis=" + intervalMillis +
                    '}';
        }
    }

    @Keep
    public static class RamConfig implements RamContext, Serializable {
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

        @Override
        public String toString() {
            return "RamConfig{" +
                    "intervalMillis=" + intervalMillis +
                    '}';
        }
    }

    @Keep
    public static class NetworkConfig implements NetworkContext, Serializable {
        @Override
        public String toString() {
            return "NetworkConfig{}";
        }
    }

    @Keep
    public static class SmConfig implements SmContext, Serializable {
        public boolean debugNotification;
        public long longBlockThresholdMillis;
        public long shortBlockThresholdMillis;
        public long dumpIntervalMillis;

        public SmConfig(boolean debugNotification, long longBlockThresholdMillis, long shortBlockThresholdMillis, long dumpIntervalMillis) {
            this.debugNotification = debugNotification;
            this.longBlockThresholdMillis = longBlockThresholdMillis;
            this.shortBlockThresholdMillis = shortBlockThresholdMillis;
            this.dumpIntervalMillis = dumpIntervalMillis;
        }

        public SmConfig() {
            this.debugNotification = true;
            this.longBlockThresholdMillis = 500;
            this.shortBlockThresholdMillis = 500;
            this.dumpIntervalMillis = 1000;
        }

        @Override
        public Context context() {
            return GodEye.instance().getApplication();
        }

        @Override
        public boolean debugNotification() {
            return debugNotification;
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

        @Override
        public String toString() {
            return "SmConfig{" +
                    "debugNotification=" + debugNotification +
                    ", longBlockThresholdMillis=" + longBlockThresholdMillis +
                    ", shortBlockThresholdMillis=" + shortBlockThresholdMillis +
                    ", dumpIntervalMillis=" + dumpIntervalMillis +
                    '}';
        }

        public static class Factory {

            public static SmConfig convert(SmContext smContext) {
                if (smContext == null) {
                    return null;
                }
                return new SmConfig(smContext.debugNotification(), smContext.longBlockThreshold(), smContext.shortBlockThreshold(), smContext.dumpInterval());
            }

            public static SmContext convert(GodEyeConfig.SmConfig smConfig) {
                if (smConfig == null) {
                    return null;
                }
                return new SmContext() {
                    @Override
                    public Context context() {
                        return smConfig.context();
                    }

                    @Override
                    public boolean debugNotification() {
                        return smConfig.debugNotification();
                    }

                    @Override
                    public long longBlockThreshold() {
                        return smConfig.longBlockThreshold();
                    }

                    @Override
                    public long shortBlockThreshold() {
                        return smConfig.shortBlockThreshold();
                    }

                    @Override
                    public long dumpInterval() {
                        return smConfig.dumpInterval();
                    }
                };
            }
        }
    }

    @Keep
    public static class StartupConfig implements StartupContext, Serializable {
        @Override
        public String toString() {
            return "StartupConfig{}";
        }
    }

    @Keep
    public static class TrafficConfig implements TrafficContext, Serializable {
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

        @Override
        public String toString() {
            return "TrafficConfig{" +
                    "intervalMillis=" + intervalMillis +
                    ", sampleMillis=" + sampleMillis +
                    '}';
        }
    }

    @Keep
    public static class CrashConfig implements CrashContext, Serializable {
        public boolean immediate = false;

        public CrashConfig() {
        }

        public CrashConfig(boolean immediate) {
            this.immediate = immediate;
        }

        @Override
        public Context context() {
            return GodEye.instance().getApplication();
        }

        @Override
        public boolean immediate() {
            return immediate;
        }

        @Override
        public String toString() {
            return "CrashConfig{" +
                    "immediate=" + immediate +
                    '}';
        }
    }

    @Keep
    public static class ThreadConfig implements ThreadContext, Serializable {
        public long intervalMillis;
        //ThreadFilter
        public String threadFilter;

        public ThreadConfig(long intervalMillis, String threadFilter) {
            this.intervalMillis = intervalMillis;
            this.threadFilter = threadFilter;
        }

        public ThreadConfig() {
            this.intervalMillis = 2000;
            this.threadFilter = ExcludeSystemThreadFilter.class.getName();
        }

        @Override
        public long intervalMillis() {
            return intervalMillis;
        }

        @Override
        public String threadFilter() {
            return threadFilter;
        }

        @Override
        public String toString() {
            return "ThreadConfig{" +
                    "intervalMillis=" + intervalMillis +
                    ", threadFilter=" + threadFilter +
                    '}';
        }
    }

    @Keep
    public static class PageloadConfig implements PageloadContext, Serializable {
        public String pageInfoProvider;

        public PageloadConfig() {
            this.pageInfoProvider = DefaultPageInfoProvider.class.getName();
        }

        public PageloadConfig(String pageInfoProvider) {
            this.pageInfoProvider = pageInfoProvider;
        }

        @Override
        public Application application() {
            return GodEye.instance().getApplication();
        }

        @NonNull
        @Override
        public String pageInfoProvider() {
            return pageInfoProvider;
        }

        @Override
        public String toString() {
            return "PageloadConfig{" +
                    "pageInfoProvider=" + pageInfoProvider +
                    '}';
        }
    }

    @Keep
    public static class AppSizeConfig implements AppSizeContext, Serializable {
        public long delayMillis;

        public AppSizeConfig(long delayMillis) {
            this.delayMillis = delayMillis;
        }

        public AppSizeConfig() {
            this.delayMillis = 0;
        }

        @Override
        public Context context() {
            return GodEye.instance().getApplication();
        }

        @Override
        public long delayMillis() {
            return delayMillis;
        }
    }

    @Keep
    public static class MethodCanaryConfig implements MethodCanaryContext, Serializable {
        public int maxMethodCountSingleThreadByCost;
        public long lowCostMethodThresholdMillis;

        public MethodCanaryConfig() {
            this.maxMethodCountSingleThreadByCost = 300;
            this.lowCostMethodThresholdMillis = 10L;
        }

        public MethodCanaryConfig(int maxMethodCountSingleThreadByCost, long lowCostMethodThresholdMillis) {
            this.maxMethodCountSingleThreadByCost = maxMethodCountSingleThreadByCost;
            this.lowCostMethodThresholdMillis = lowCostMethodThresholdMillis;
        }

        @Override
        public long lowCostMethodThresholdMillis() {
            return lowCostMethodThresholdMillis;
        }

        @Override
        public int maxMethodCountSingleThreadByCost() {
            return maxMethodCountSingleThreadByCost;
        }

        @Override
        public Application app() {
            return GodEye.instance().getApplication();
        }

        @Override
        public String toString() {
            return "MethodCanaryConfig{" +
                    "maxMethodCountSingleThreadByCost=" + maxMethodCountSingleThreadByCost +
                    ", lowCostMethodThresholdMillis=" + lowCostMethodThresholdMillis +
                    '}';
        }
    }

    @Keep
    public static class ViewCanaryConfig implements ViewCanaryContext, Serializable {

        public int maxDepth;

        public ViewCanaryConfig() {
            this.maxDepth = 10;
        }

        public ViewCanaryConfig(int maxDepth) {
            this.maxDepth = maxDepth;
        }

        @Override
        public Application application() {
            return GodEye.instance().getApplication();
        }

        @Override
        public int maxDepth() {
            return maxDepth;
        }
    }

    @Keep
    public static class ImageCanaryConfig implements ImageCanaryContext, Serializable {

        public String imageCanaryConfigProvider;

        public ImageCanaryConfig() {
            this.imageCanaryConfigProvider = DefaultImageCanaryConfigProvider.class.getName();
        }

        public ImageCanaryConfig(String imageCanaryConfigProvider) {
            this.imageCanaryConfigProvider = imageCanaryConfigProvider;
        }

        @Override
        public Application getApplication() {
            return GodEye.instance().getApplication();
        }

        // ImageCanaryConfigProvider
        @Override
        public String getImageCanaryConfigProvider() {
            return imageCanaryConfigProvider;
        }
    }

    private CpuConfig mCpuConfig;
    private BatteryConfig mBatteryConfig;
    private FpsConfig mFpsConfig;
    private LeakConfig mLeakConfig;
    private HeapConfig mHeapConfig;
    private PssConfig mPssConfig;
    private RamConfig mRamConfig;
    private NetworkConfig mNetworkConfig;
    private SmConfig mSmConfig;
    private StartupConfig mStartupConfig;
    private TrafficConfig mTrafficConfig;
    private CrashConfig mCrashConfig;
    private ThreadConfig mThreadConfig;
    private PageloadConfig mPageloadConfig;
    private MethodCanaryConfig mMethodCanaryConfig;
    private AppSizeConfig mAppSizeConfig;
    private ViewCanaryConfig mViewCanaryConfig;
    private ImageCanaryConfig mImageCanaryConfig;

    private GodEyeConfig() {
    }

    public CpuConfig getCpuConfig() {
        return mCpuConfig;
    }

    public void setCpuConfig(CpuConfig cpuConfig) {
        mCpuConfig = cpuConfig;
    }

    public BatteryConfig getBatteryConfig() {
        return mBatteryConfig;
    }

    public void setBatteryConfig(BatteryConfig batteryConfig) {
        mBatteryConfig = batteryConfig;
    }

    public FpsConfig getFpsConfig() {
        return mFpsConfig;
    }

    public void setFpsConfig(FpsConfig fpsConfig) {
        mFpsConfig = fpsConfig;
    }

    public LeakConfig getLeakConfig() {
        return mLeakConfig;
    }

    public void setLeakConfig(LeakConfig leakConfig) {
        mLeakConfig = leakConfig;
    }

    public HeapConfig getHeapConfig() {
        return mHeapConfig;
    }

    public void setHeapConfig(HeapConfig heapConfig) {
        mHeapConfig = heapConfig;
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

    public NetworkConfig getNetworkConfig() {
        return mNetworkConfig;
    }

    public void setNetworkConfig(NetworkConfig networkConfig) {
        mNetworkConfig = networkConfig;
    }

    public SmConfig getSmConfig() {
        return mSmConfig;
    }

    public void setSmConfig(SmConfig smConfig) {
        mSmConfig = smConfig;
    }

    public StartupConfig getStartupConfig() {
        return mStartupConfig;
    }

    public void setStartupConfig(StartupConfig startupConfig) {
        mStartupConfig = startupConfig;
    }

    public TrafficConfig getTrafficConfig() {
        return mTrafficConfig;
    }

    public void setTrafficConfig(TrafficConfig trafficConfig) {
        mTrafficConfig = trafficConfig;
    }

    public CrashConfig getCrashConfig() {
        return mCrashConfig;
    }

    public void setCrashConfig(CrashConfig crashConfig) {
        mCrashConfig = crashConfig;
    }

    public ThreadConfig getThreadConfig() {
        return mThreadConfig;
    }

    public void setThreadConfig(ThreadConfig threadConfig) {
        mThreadConfig = threadConfig;
    }

    public PageloadConfig getPageloadConfig() {
        return mPageloadConfig;
    }


    public void setPageloadConfig(PageloadConfig pageloadConfig) {
        mPageloadConfig = pageloadConfig;
    }

    public void setAppSizeConfig(AppSizeConfig appSizeConfig) {
        mAppSizeConfig = appSizeConfig;
    }

    public AppSizeConfig getAppSizeConfig() {
        return mAppSizeConfig;
    }

    public MethodCanaryConfig getMethodCanaryConfig() {
        return mMethodCanaryConfig;
    }

    public ViewCanaryConfig getViewCanaryConfig() {
        return mViewCanaryConfig;
    }

    public ImageCanaryConfig getImageCanaryConfig() {
        return mImageCanaryConfig;
    }

    public void setViewCanaryConfig(ViewCanaryConfig viewCanaryConfig) {
        mViewCanaryConfig = viewCanaryConfig;
    }

    public void setMethodCanaryConfig(MethodCanaryConfig methodCanaryConfig) {
        mMethodCanaryConfig = methodCanaryConfig;
    }

    public void setImageCanaryConfig(ImageCanaryConfig imageCanaryConfig) {
        this.mImageCanaryConfig = imageCanaryConfig;
    }

    @Override
    public String toString() {
        return "GodEyeConfig{" +
                "mCpuConfig=" + mCpuConfig +
                ", mBatteryConfig=" + mBatteryConfig +
                ", mFpsConfig=" + mFpsConfig +
                ", mLeakConfig=" + mLeakConfig +
                ", mHeapConfig=" + mHeapConfig +
                ", mPssConfig=" + mPssConfig +
                ", mRamConfig=" + mRamConfig +
                ", mNetworkConfig=" + mNetworkConfig +
                ", mSmConfig=" + mSmConfig +
                ", mStartupConfig=" + mStartupConfig +
                ", mTrafficConfig=" + mTrafficConfig +
                ", mCrashConfig=" + mCrashConfig +
                ", mThreadConfig=" + mThreadConfig +
                ", mPageloadConfig=" + mPageloadConfig +
                ", mMethodCanaryConfig=" + mMethodCanaryConfig +
                ", mAppSizeConfig=" + mAppSizeConfig +
                ", mViewCanaryConfig=" + mViewCanaryConfig +
                ", mImageCanaryConfig=" + mImageCanaryConfig +
                '}';
    }

    public static final class GodEyeConfigBuilder {
        private CpuConfig mCpuConfig;
        private BatteryConfig mBatteryConfig;
        private FpsConfig mFpsConfig;
        private LeakConfig mLeakConfig;
        private HeapConfig mHeapConfig;
        private PssConfig mPssConfig;
        private RamConfig mRamConfig;
        private NetworkConfig mNetworkConfig;
        private SmConfig mSmConfig;
        private StartupConfig mStartupConfig;
        private TrafficConfig mTrafficConfig;
        private CrashConfig mCrashConfig;
        private ThreadConfig mThreadConfig;
        private PageloadConfig mPageloadConfig;
        private MethodCanaryConfig mMethodCanaryConfig;
        private AppSizeConfig mAppSizeConfig;
        private ViewCanaryConfig mViewCanaryConfig;
        private ImageCanaryConfig mImageCanaryConfig;

        public static GodEyeConfigBuilder godEyeConfig() {
            return new GodEyeConfigBuilder();
        }

        public GodEyeConfigBuilder withCpuConfig(CpuConfig CpuConfig) {
            this.mCpuConfig = CpuConfig;
            return this;
        }

        public GodEyeConfigBuilder withBatteryConfig(BatteryConfig BatteryConfig) {
            this.mBatteryConfig = BatteryConfig;
            return this;
        }

        public GodEyeConfigBuilder withFpsConfig(FpsConfig FpsConfig) {
            this.mFpsConfig = FpsConfig;
            return this;
        }

        public GodEyeConfigBuilder withLeakConfig(LeakConfig LeakConfig) {
            this.mLeakConfig = LeakConfig;
            return this;
        }

        public GodEyeConfigBuilder withHeapConfig(HeapConfig HeapConfig) {
            this.mHeapConfig = HeapConfig;
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

        public GodEyeConfigBuilder withNetworkConfig(NetworkConfig NetworkConfig) {
            this.mNetworkConfig = NetworkConfig;
            return this;
        }

        public GodEyeConfigBuilder withSmConfig(SmConfig SmConfig) {
            this.mSmConfig = SmConfig;
            return this;
        }

        public GodEyeConfigBuilder withStartupConfig(StartupConfig StartupConfig) {
            this.mStartupConfig = StartupConfig;
            return this;
        }

        public GodEyeConfigBuilder withTrafficConfig(TrafficConfig TrafficConfig) {
            this.mTrafficConfig = TrafficConfig;
            return this;
        }

        public GodEyeConfigBuilder withCrashConfig(CrashConfig CrashConfig) {
            this.mCrashConfig = CrashConfig;
            return this;
        }

        public GodEyeConfigBuilder withThreadConfig(ThreadConfig ThreadConfig) {
            this.mThreadConfig = ThreadConfig;
            return this;
        }

        public GodEyeConfigBuilder withPageloadConfig(PageloadConfig PageloadConfig) {
            this.mPageloadConfig = PageloadConfig;
            return this;
        }

        public GodEyeConfigBuilder withMethodCanaryConfig(MethodCanaryConfig MethodCanaryConfig) {
            this.mMethodCanaryConfig = MethodCanaryConfig;
            return this;
        }

        public GodEyeConfigBuilder withAppSizeConfig(AppSizeConfig appSizeConfig) {
            this.mAppSizeConfig = appSizeConfig;
            return this;
        }

        public GodEyeConfigBuilder withViewCanaryConfig(ViewCanaryConfig viewCanaryConfig) {
            this.mViewCanaryConfig = viewCanaryConfig;
            return this;
        }

        public GodEyeConfigBuilder withImageCanaryConfig(ImageCanaryConfig imageCanaryConfig) {
            this.mImageCanaryConfig = imageCanaryConfig;
            return this;
        }

        public GodEyeConfig build() {
            GodEyeConfig godEyeConfig = new GodEyeConfig();
            godEyeConfig.mStartupConfig = this.mStartupConfig;
            godEyeConfig.mMethodCanaryConfig = this.mMethodCanaryConfig;
            godEyeConfig.mHeapConfig = this.mHeapConfig;
            godEyeConfig.mFpsConfig = this.mFpsConfig;
            godEyeConfig.mNetworkConfig = this.mNetworkConfig;
            godEyeConfig.mLeakConfig = this.mLeakConfig;
            godEyeConfig.mTrafficConfig = this.mTrafficConfig;
            godEyeConfig.mPageloadConfig = this.mPageloadConfig;
            godEyeConfig.mPssConfig = this.mPssConfig;
            godEyeConfig.mSmConfig = this.mSmConfig;
            godEyeConfig.mRamConfig = this.mRamConfig;
            godEyeConfig.mBatteryConfig = this.mBatteryConfig;
            godEyeConfig.mThreadConfig = this.mThreadConfig;
            godEyeConfig.mCrashConfig = this.mCrashConfig;
            godEyeConfig.mCpuConfig = this.mCpuConfig;
            godEyeConfig.mAppSizeConfig = this.mAppSizeConfig;
            godEyeConfig.mViewCanaryConfig = this.mViewCanaryConfig;
            godEyeConfig.mImageCanaryConfig = this.mImageCanaryConfig;
            return godEyeConfig;
        }
    }
}

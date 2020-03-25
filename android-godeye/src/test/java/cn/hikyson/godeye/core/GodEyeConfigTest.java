package cn.hikyson.godeye.core;

import android.app.Application;
import android.content.res.AssetManager;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import cn.hikyson.godeye.core.helper.GodEyeConfigHelper;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.godeye.core.internal.modules.appsize.AppSizeConfig;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryConfig;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuConfig;
import cn.hikyson.godeye.core.internal.modules.crash.CrashConfig;
import cn.hikyson.godeye.core.internal.modules.fps.FpsConfig;
import cn.hikyson.godeye.core.internal.modules.imagecanary.ImageCanaryConfig;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakConfig;
import cn.hikyson.godeye.core.internal.modules.memory.HeapConfig;
import cn.hikyson.godeye.core.internal.modules.memory.PssConfig;
import cn.hikyson.godeye.core.internal.modules.memory.RamConfig;
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanaryConfig;
import cn.hikyson.godeye.core.internal.modules.network.NetworkConfig;
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadConfig;
import cn.hikyson.godeye.core.internal.modules.sm.SmConfig;
import cn.hikyson.godeye.core.internal.modules.startup.StartupConfig;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadConfig;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficConfig;
import cn.hikyson.godeye.core.internal.modules.viewcanary.ViewCanaryConfig;
import cn.hikyson.godeye.core.utils.JsonUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class GodEyeConfigTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void noneConfig() {
        GodEyeConfig config0 = GodEyeConfig.noneConfig();
        assertNull(config0.getAppSizeConfig());
        assertNull(config0.getBatteryConfig());
        assertNull(config0.getCpuConfig());
        assertNull(config0.getCrashConfig());
        assertNull(config0.getFpsConfig());
        assertNull(config0.getHeapConfig());
        assertNull(config0.getImageCanaryConfig());
        assertNull(config0.getLeakConfig());
        assertNull(config0.getMethodCanaryConfig());
        assertNull(config0.getNetworkConfig());
        assertNull(config0.getPageloadConfig());
        assertNull(config0.getPssConfig());
        assertNull(config0.getRamConfig());
        assertNull(config0.getSmConfig());
        assertNull(config0.getStartupConfig());
        assertNull(config0.getThreadConfig());
        assertNull(config0.getTrafficConfig());
        assertNull(config0.getViewCanaryConfig());
        GodEyeConfig config = GodEyeConfig.noneConfigBuilder().build();
        assertNull(config.getAppSizeConfig());
        assertNull(config.getBatteryConfig());
        assertNull(config.getCpuConfig());
        assertNull(config.getCrashConfig());
        assertNull(config.getFpsConfig());
        assertNull(config.getHeapConfig());
        assertNull(config.getImageCanaryConfig());
        assertNull(config.getLeakConfig());
        assertNull(config.getMethodCanaryConfig());
        assertNull(config.getNetworkConfig());
        assertNull(config.getPageloadConfig());
        assertNull(config.getPssConfig());
        assertNull(config.getRamConfig());
        assertNull(config.getSmConfig());
        assertNull(config.getStartupConfig());
        assertNull(config.getThreadConfig());
        assertNull(config.getTrafficConfig());
        assertNull(config.getViewCanaryConfig());
    }

    @Test
    public void defaultConfig() {
        GodEyeConfig config0 = GodEyeConfig.defaultConfig();
        assertEquals(JsonUtil.toJson(new AppSizeConfig()), JsonUtil.toJson(config0.getAppSizeConfig()));
        assertEquals(JsonUtil.toJson(new BatteryConfig()), JsonUtil.toJson(config0.getBatteryConfig()));
        assertEquals(JsonUtil.toJson(new CpuConfig()), JsonUtil.toJson(config0.getCpuConfig()));
        assertEquals(JsonUtil.toJson(new CrashConfig()), JsonUtil.toJson(config0.getCrashConfig()));
        assertEquals(JsonUtil.toJson(new FpsConfig()), JsonUtil.toJson(config0.getFpsConfig()));
        assertEquals(JsonUtil.toJson(new HeapConfig()), JsonUtil.toJson(config0.getHeapConfig()));
        assertEquals(JsonUtil.toJson(new ImageCanaryConfig()), JsonUtil.toJson(config0.getImageCanaryConfig()));
        assertEquals(JsonUtil.toJson(new LeakConfig()), JsonUtil.toJson(config0.getLeakConfig()));
        assertEquals(JsonUtil.toJson(new MethodCanaryConfig()), JsonUtil.toJson(config0.getMethodCanaryConfig()));
        assertEquals(JsonUtil.toJson(new NetworkConfig()), JsonUtil.toJson(config0.getNetworkConfig()));
        assertEquals(JsonUtil.toJson(new PageloadConfig()), JsonUtil.toJson(config0.getPageloadConfig()));
        assertEquals(JsonUtil.toJson(new PssConfig()), JsonUtil.toJson(config0.getPssConfig()));
        assertEquals(JsonUtil.toJson(new RamConfig()), JsonUtil.toJson(config0.getRamConfig()));
        assertEquals(JsonUtil.toJson(new SmConfig()), JsonUtil.toJson(config0.getSmConfig()));
        assertEquals(JsonUtil.toJson(new StartupConfig()), JsonUtil.toJson(config0.getStartupConfig()));
        assertEquals(JsonUtil.toJson(new ThreadConfig()), JsonUtil.toJson(config0.getThreadConfig()));
        assertEquals(JsonUtil.toJson(new TrafficConfig()), JsonUtil.toJson(config0.getTrafficConfig()));
        assertEquals(JsonUtil.toJson(new ViewCanaryConfig()), JsonUtil.toJson(config0.getViewCanaryConfig()));

        GodEyeConfig config = GodEyeConfig.defaultConfigBuilder().build();
        assertEquals(JsonUtil.toJson(new AppSizeConfig()), JsonUtil.toJson(config.getAppSizeConfig()));
        assertEquals(JsonUtil.toJson(new BatteryConfig()), JsonUtil.toJson(config.getBatteryConfig()));
        assertEquals(JsonUtil.toJson(new CpuConfig()), JsonUtil.toJson(config.getCpuConfig()));
        assertEquals(JsonUtil.toJson(new CrashConfig()), JsonUtil.toJson(config.getCrashConfig()));
        assertEquals(JsonUtil.toJson(new FpsConfig()), JsonUtil.toJson(config.getFpsConfig()));
        assertEquals(JsonUtil.toJson(new HeapConfig()), JsonUtil.toJson(config.getHeapConfig()));
        assertEquals(JsonUtil.toJson(new ImageCanaryConfig()), JsonUtil.toJson(config.getImageCanaryConfig()));
        assertEquals(JsonUtil.toJson(new LeakConfig()), JsonUtil.toJson(config.getLeakConfig()));
        assertEquals(JsonUtil.toJson(new MethodCanaryConfig()), JsonUtil.toJson(config.getMethodCanaryConfig()));
        assertEquals(JsonUtil.toJson(new NetworkConfig()), JsonUtil.toJson(config.getNetworkConfig()));
        assertEquals(JsonUtil.toJson(new PageloadConfig()), JsonUtil.toJson(config.getPageloadConfig()));
        assertEquals(JsonUtil.toJson(new PssConfig()), JsonUtil.toJson(config.getPssConfig()));
        assertEquals(JsonUtil.toJson(new RamConfig()), JsonUtil.toJson(config.getRamConfig()));
        assertEquals(JsonUtil.toJson(new SmConfig()), JsonUtil.toJson(config.getSmConfig()));
        assertEquals(JsonUtil.toJson(new StartupConfig()), JsonUtil.toJson(config.getStartupConfig()));
        assertEquals(JsonUtil.toJson(new ThreadConfig()), JsonUtil.toJson(config.getThreadConfig()));
        assertEquals(JsonUtil.toJson(new TrafficConfig()), JsonUtil.toJson(config.getTrafficConfig()));
        assertEquals(JsonUtil.toJson(new ViewCanaryConfig()), JsonUtil.toJson(config.getViewCanaryConfig()));
    }

    @Test
    public void fromInputStream() {
        GodEyeConfig config = GodEyeConfigHelper.createFromResource();
        assertConfig(config);
        try {
            GodEyeConfig.fromInputStream(null);
        } catch (Throwable ignore) {
            fail();
        }
    }

    @Test
    public void fromAssets() {
        Application originApplication = ApplicationProvider.getApplicationContext();
        Application application = Mockito.spy(originApplication);
        Application godeyeApplication = GodEye.instance().getApplication();
        GodEye.instance().internalInit(application);
        AssetManager assetManager = Mockito.spy(application.getAssets());
        Mockito.doReturn(assetManager).when(application).getAssets();
        try {
            Mockito.doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    Object[] args = invocation.getArguments();
                    Object object = this.getClass().getClassLoader().getResourceAsStream(String.valueOf(args[0]));
                    return object;
                }
            }).when(assetManager).open(Mockito.anyString());
        } catch (IOException e) {
            fail();
        }
        GodEyeConfig config = GodEyeConfig.fromAssets("install.config");
        assertConfig(config);
        GodEye.instance().internalInit(godeyeApplication);
    }

    private void assertConfig(GodEyeConfig config) {
        assertEquals(0, config.getAppSizeConfig().delayMillis());
        assertEquals(JsonUtil.toJson(new BatteryConfig()), JsonUtil.toJson(config.getBatteryConfig()));
        assertEquals(2000, config.getCpuConfig().intervalMillis());
        assertEquals(false, config.getCrashConfig().immediate());
        assertEquals(2000, config.getFpsConfig().intervalMillis());
        assertEquals(2000, config.getHeapConfig().intervalMillis());
        assertEquals("cn.hikyson.godeye.core.internal.modules.imagecanary.DefaultImageCanaryConfigProvider", config.getImageCanaryConfig().getImageCanaryConfigProvider());
        assertEquals(10, config.getMethodCanaryConfig().lowCostMethodThresholdMillis());
        assertEquals(300, config.getMethodCanaryConfig().maxMethodCountSingleThreadByCost());
        assertEquals(JsonUtil.toJson(new NetworkConfig()), JsonUtil.toJson(config.getNetworkConfig()));
        assertEquals("cn.hikyson.godeye.core.internal.modules.pageload.DefaultPageInfoProvider", config.getPageloadConfig().pageInfoProvider());
        assertEquals(2000, config.getPssConfig().intervalMillis());
        assertEquals(2000, config.getRamConfig().intervalMillis());
        assertEquals(1000, config.getSmConfig().dumpInterval());
        assertEquals(500, config.getSmConfig().longBlockThreshold());
        assertEquals(500, config.getSmConfig().shortBlockThreshold());
        assertEquals(true, config.getSmConfig().debugNotification());
        assertEquals(JsonUtil.toJson(new StartupConfig()), JsonUtil.toJson(config.getStartupConfig()));
        assertEquals(3000, config.getThreadConfig().intervalMillis);
        assertEquals("cn.hikyson.godeye.core.internal.modules.thread.ExcludeSystemThreadFilter", config.getThreadConfig().threadFilter);
        assertEquals("cn.hikyson.godeye.core.internal.modules.thread.DefaultThreadTagger", config.getThreadConfig().threadTagger);
        assertEquals(2000, config.getTrafficConfig().intervalMillis());
        assertEquals(1000, config.getTrafficConfig().sampleMillis());
        assertEquals(10, config.getViewCanaryConfig().maxDepth());
    }
}
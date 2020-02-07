package cn.hikyson.godeye.core;

import android.os.Build;
import android.view.Choreographer;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.helper.ChoreographerInjecor;
import cn.hikyson.godeye.core.helper.GodEyeConfigHelper;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.godeye.core.internal.modules.startup.StartupConfig;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import io.reactivex.functions.Consumer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class GodEyeTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void initInThread() {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                GodEye.instance().init(ApplicationProvider.getApplicationContext());
                countDownLatch.countDown();
            }
        }).start();
        try {
            countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail();
        }
    }

    @Test
    public void installAndUninstallInAnyThread() {
        // prepare
        Choreographer choreographer = Mockito.spy(Choreographer.getInstance());
        ChoreographerInjecor.setChoreographerProvider(() -> choreographer);
        Mockito.doAnswer(invocation -> null).when(choreographer).postFrameCallback(Mockito.any());
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().uninstall();
        GodEye.instance().install(GodEyeConfigHelper.createFromResource());
        Set<String> installedModules = GodEye.instance().getInstalledModuleNames();
        assertTrue(installedModules.containsAll(GodEye.ALL_MODULE_NAMES));
        assertTrue(GodEye.ALL_MODULE_NAMES.containsAll(installedModules));
        GodEye.instance().uninstall();
        assertTrue(installedModules.isEmpty());
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                GodEye.instance().install(GodEyeConfigHelper.createFromResource());
                countDownLatch.countDown();
            }
        }).start();
        try {
            countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail();
        }
        installedModules = GodEye.instance().getInstalledModuleNames();
        assertTrue(installedModules.containsAll(GodEye.ALL_MODULE_NAMES));
        assertTrue(GodEye.ALL_MODULE_NAMES.containsAll(installedModules));
    }

    @Test
    public void getModule() {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().uninstall();
        try {
            GodEye.instance().getModule(null);
            fail();
        } catch (UninstallException ignore) {
        }
        try {
            GodEye.instance().getModule("");
            fail();
        } catch (UninstallException ignore) {
        }
        try {
            GodEye.instance().getModule(GodEye.ModuleName.STARTUP);
            fail();
        } catch (UninstallException ignore) {
        }
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withStartupConfig(new StartupConfig()).build());
        try {
            GodEye.instance().getModule(GodEye.ModuleName.STARTUP);
        } catch (UninstallException e) {
            fail();
        }
    }

    @Test
    public void moduleObservable() {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().uninstall();
        try {
            GodEye.instance().moduleObservable(null);
            fail();
        } catch (UninstallException ignore) {
        }
        try {
            GodEye.instance().moduleObservable("");
            fail();
        } catch (UninstallException ignore) {
        }
        try {
            GodEye.instance().moduleObservable(GodEye.ModuleName.STARTUP);
            fail();
        } catch (UninstallException ignore) {
        }
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withStartupConfig(new StartupConfig()).build());
        try {
            GodEye.instance().moduleObservable(GodEye.ModuleName.STARTUP).test().assertNoValues();
        } catch (UninstallException e) {
            fail();
        }
    }

    @Test
    public void observeModule() {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().uninstall();
        try {
            GodEye.instance().observeModule(null, null);
            fail();
        } catch (UninstallException ignore) {
        }
        try {
            GodEye.instance().observeModule("", null);
            fail();
        } catch (UninstallException ignore) {
        }
        try {
            GodEye.instance().observeModule(GodEye.ModuleName.STARTUP, null);
            fail();
        } catch (UninstallException ignore) {
        }

        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withStartupConfig(new StartupConfig()).build());
        try {
            GodEye.instance().observeModule(GodEye.ModuleName.STARTUP, null);
            fail();
        } catch (NullPointerException ignore) {
        } catch (UninstallException e) {
            fail();
        }
        try {
            GodEyeHelper.onAppStartEnd(new StartupInfo(StartupInfo.StartUpType.COLD, 1000));
        } catch (UninstallException e) {
            fail();
        }
        try {
            GodEye.instance().observeModule(GodEye.ModuleName.STARTUP, new Consumer<StartupInfo>() {
                @Override
                public void accept(StartupInfo cpuInfo) throws Exception {
                }
            });
        } catch (UninstallException e) {
            fail();
        }
    }

    @Test
    public void getApplication() {
        try {
            GodEye.instance().init(null);
            fail();
        } catch (NullPointerException ignore) {
        }
        assertNull(GodEye.instance().getApplication());
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        assertNotNull(GodEye.instance().getApplication());
    }
}
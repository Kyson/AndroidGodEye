package cn.hikyson.godeye.core;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.helper.ChoreographerHelper;
import cn.hikyson.godeye.core.helper.GodEyeConfigHelper;
import cn.hikyson.godeye.core.helper.Log4Test;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.godeye.core.helper.ThreadHelper;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.modules.startup.StartupConfig;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.core.internal.notification.DefaultNotificationConfig;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.TestScheduler;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class GodEyeTest {

    @Before
    public void setUp() throws Exception {
        ChoreographerHelper.setup();
        ThreadHelper.setupRxjava();
    }

    @After
    public void tearDown() throws Exception {
        ThreadHelper.teardownRxjava();
        ChoreographerHelper.teardown();
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
        GodEye.instance().uninstall();
        GodEye.instance().install(GodEyeConfigHelper.createFromResource());
        // install twice
        GodEye.instance().install(GodEyeConfigHelper.createFromResource2());
        Set<String> installedModules = GodEye.instance().getInstalledModuleNames();
        assertTrue(installedModules.containsAll(GodEye.ALL_MODULE_NAMES));
        assertTrue(GodEye.ALL_MODULE_NAMES.containsAll(installedModules));
        GodEye.instance().uninstall();
        // uninstall twice
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
        // test event send
        ((TestScheduler) ThreadUtil.computationScheduler()).advanceTimeBy(10, TimeUnit.SECONDS);
        installedModules = GodEye.instance().getInstalledModuleNames();
        assertTrue(installedModules.containsAll(GodEye.ALL_MODULE_NAMES));
        assertTrue(GodEye.ALL_MODULE_NAMES.containsAll(installedModules));
        Iterator<String> stringIterator = installedModules.iterator();
        shadowOf(getMainLooper()).idle();
        while (stringIterator.hasNext()) {
            String moduleName = stringIterator.next();
            try {
                Install install = GodEye.instance().getModule(moduleName);
                Log4Test.d("Test module:" + moduleName);
                Assert.assertTrue(install.isInstalled());
                assertNotNull(install.config());
            } catch (UninstallException e) {
                Assert.fail();
            }
        }
        GodEye.instance().uninstall();
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
        assertNotNull(GodEye.instance().getApplication());
    }

    @Test
    public void installNotificationMultiThread() {
        GodEye.instance().installNotification(new DefaultNotificationConfig());
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    GodEye.instance().installNotification(new DefaultNotificationConfig());
                    GodEye.instance().uninstallNotification();
                }
                countDownLatch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    GodEye.instance().installNotification(new DefaultNotificationConfig());
                    GodEye.instance().uninstallNotification();
                }
                countDownLatch.countDown();
            }
        }).start();
        try {
            countDownLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GodEye.instance().uninstallNotification();
    }

    @Test
    public void installNotification() {
        GodEye.instance().uninstall();
        GodEye.instance().install(GodEyeConfigHelper.createFromResource());
        GodEye.instance().installNotification(new DefaultNotificationConfig());
        ((TestScheduler) ThreadUtil.computationScheduler()).advanceTimeBy(10, TimeUnit.SECONDS);
        GodEye.instance().uninstallNotification();
        GodEye.instance().uninstall();
    }

}
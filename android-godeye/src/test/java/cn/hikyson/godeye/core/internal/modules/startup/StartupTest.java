package cn.hikyson.godeye.core.internal.modules.startup;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.helper.Log4Test;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class StartupTest {

    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withStartupConfig(new StartupConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            GodEye.instance().<Startup>getModule(GodEye.ModuleName.STARTUP).produce(new StartupInfo(StartupInfo.StartUpType.COLD, 1));
            GodEye.instance().<Startup>getModule(GodEye.ModuleName.STARTUP).produce(new StartupInfo(StartupInfo.StartUpType.HOT, 2));
            TestObserver<StartupInfo> testObserver = GodEye.instance().<Startup, StartupInfo>moduleObservable(GodEye.ModuleName.STARTUP).test();
            GodEye.instance().<Startup>getModule(GodEye.ModuleName.STARTUP).produce(new StartupInfo(StartupInfo.StartUpType.COLD, 3));
            GodEye.instance().<Startup>getModule(GodEye.ModuleName.STARTUP).produce(new StartupInfo(StartupInfo.StartUpType.HOT, 4));
            testObserver.assertValueCount(3).assertValueAt(0, new Predicate<StartupInfo>() {
                @Override
                public boolean test(StartupInfo info) throws Exception {
                    Log4Test.d(info);
                    return StartupInfo.StartUpType.HOT.equals(info.startupType)
                            && info.startupTime == 2;
                }
            }).assertValueAt(1, new Predicate<StartupInfo>() {
                @Override
                public boolean test(StartupInfo info) throws Exception {
                    Log4Test.d(info);
                    return StartupInfo.StartUpType.COLD.equals(info.startupType)
                            && info.startupTime == 3;
                }
            }).assertValueAt(2, new Predicate<StartupInfo>() {
                @Override
                public boolean test(StartupInfo info) throws Exception {
                    Log4Test.d(info);
                    return StartupInfo.StartUpType.HOT.equals(info.startupType)
                            && info.startupTime == 4;
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
package cn.hikyson.godeye.core.internal.modules.crash;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class CrashTest {
    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withCrashConfig(new CrashConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            CrashInfo crashInfo0 = new CrashInfo();
            crashInfo0.crashMessage = "crashInfo0";
            CrashInfo crashInfo1 = new CrashInfo();
            crashInfo1.crashMessage = "crashInfo1";
            CrashInfo crashInfo2 = new CrashInfo();
            crashInfo2.crashMessage = "crashInfo2";
            CrashInfo crashInfo3 = new CrashInfo();
            crashInfo3.crashMessage = "crashInfo3";

            GodEye.instance().<Crash>getModule(GodEye.ModuleName.CRASH).produce(Collections.singletonList(crashInfo0));
            GodEye.instance().<Crash>getModule(GodEye.ModuleName.CRASH).produce(Collections.singletonList(crashInfo1));
            TestObserver<List<CrashInfo>> testObserver = GodEye.instance().<Crash, List<CrashInfo>>moduleObservable(GodEye.ModuleName.CRASH).test();
            GodEye.instance().<Crash>getModule(GodEye.ModuleName.CRASH).produce(Arrays.asList(crashInfo2, crashInfo3));
            testObserver.assertValueCount(3).assertValueAt(0, new Predicate<List<CrashInfo>>() {
                @Override
                public boolean test(List<CrashInfo> info) throws Exception {
                    return info.size() == 1 && info.get(0).crashMessage.equals("crashInfo0");
                }
            }).assertValueAt(1, new Predicate<List<CrashInfo>>() {
                @Override
                public boolean test(List<CrashInfo> info) throws Exception {
                    return info.size() == 1 && info.get(0).crashMessage.equals("crashInfo1");
                }
            }).assertValueAt(2, new Predicate<List<CrashInfo>>() {
                @Override
                public boolean test(List<CrashInfo> info) throws Exception {
                    return info.size() == 2 && info.get(0).crashMessage.equals("crashInfo2") && info.get(1).crashMessage.equals("crashInfo3");
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
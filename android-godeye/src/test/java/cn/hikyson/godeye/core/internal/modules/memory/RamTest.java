package cn.hikyson.godeye.core.internal.modules.memory;

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
public class RamTest {
    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withRamConfig(new RamConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            GodEye.instance().<Ram>getModule(GodEye.ModuleName.RAM).produce(new RamInfo(1, 1, 1, false));
            GodEye.instance().<Ram>getModule(GodEye.ModuleName.RAM).produce(new RamInfo(2, 1, 1, false));
            TestObserver<RamInfo> testObserver = GodEye.instance().<Ram, RamInfo>moduleObservable(GodEye.ModuleName.RAM).test();
            GodEye.instance().<Ram>getModule(GodEye.ModuleName.RAM).produce(new RamInfo(3, 1, 1, false));
            GodEye.instance().<Ram>getModule(GodEye.ModuleName.RAM).produce(new RamInfo(4, 1, 1, false));
            testObserver.assertValueCount(3).assertValueAt(0, new Predicate<RamInfo>() {
                @Override
                public boolean test(RamInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.availMemKb == 2;
                }
            }).assertValueAt(1, new Predicate<RamInfo>() {
                @Override
                public boolean test(RamInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.availMemKb == 3;
                }
            }).assertValueAt(2, new Predicate<RamInfo>() {
                @Override
                public boolean test(RamInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.availMemKb == 4;
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
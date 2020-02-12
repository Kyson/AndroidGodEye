package cn.hikyson.godeye.core.internal.modules.battery;

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
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class BatteryTest {
    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withBatteryConfig(new BatteryConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            GodEye.instance().<Battery>getModule(GodEye.ModuleName.BATTERY).produce(new BatteryInfo(1, 1, false, 1, 1, 1, 1, 1, ""));
            GodEye.instance().<Battery>getModule(GodEye.ModuleName.BATTERY).produce(new BatteryInfo(2, 1, false, 1, 1, 1, 1, 1, ""));
            TestObserver<BatteryInfo> testObserver = GodEye.instance().<Battery, BatteryInfo>moduleObservable(GodEye.ModuleName.BATTERY).test();
            GodEye.instance().<Battery>getModule(GodEye.ModuleName.BATTERY).produce(new BatteryInfo(3, 1, false, 1, 1, 1, 1, 1, ""));
            GodEye.instance().<Battery>getModule(GodEye.ModuleName.BATTERY).produce(new BatteryInfo(4, 1, false, 1, 1, 1, 1, 1, ""));
            testObserver.assertValueCount(3).assertValueAt(0, new Predicate<BatteryInfo>() {
                @Override
                public boolean test(BatteryInfo info) throws Exception {
                    return info.status == 2;
                }
            }).assertValueAt(1, new Predicate<BatteryInfo>() {
                @Override
                public boolean test(BatteryInfo info) throws Exception {
                    return info.status == 3;
                }
            }).assertValueAt(2, new Predicate<BatteryInfo>() {
                @Override
                public boolean test(BatteryInfo info) throws Exception {
                    return info.status == 4;
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
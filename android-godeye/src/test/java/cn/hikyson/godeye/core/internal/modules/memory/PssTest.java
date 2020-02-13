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
public class PssTest {
    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withPssConfig(new PssConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            GodEye.instance().<Pss>getModule(GodEye.ModuleName.PSS).produce(new PssInfo(1, 1, 1, 1));
            GodEye.instance().<Pss>getModule(GodEye.ModuleName.PSS).produce(new PssInfo(2, 2, 2, 2));
            TestObserver<PssInfo> testObserver = GodEye.instance().<Pss, PssInfo>moduleObservable(GodEye.ModuleName.PSS).test();
            GodEye.instance().<Pss>getModule(GodEye.ModuleName.PSS).produce(new PssInfo(3, 3, 3, 3));
            GodEye.instance().<Pss>getModule(GodEye.ModuleName.PSS).produce(new PssInfo(4, 4, 4, 4));
            testObserver.assertValueCount(3).assertValueAt(0, new Predicate<PssInfo>() {
                @Override
                public boolean test(PssInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.totalPssKb == 2;
                }
            }).assertValueAt(1, new Predicate<PssInfo>() {
                @Override
                public boolean test(PssInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.totalPssKb == 3;
                }
            }).assertValueAt(2, new Predicate<PssInfo>() {
                @Override
                public boolean test(PssInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.totalPssKb == 4;
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
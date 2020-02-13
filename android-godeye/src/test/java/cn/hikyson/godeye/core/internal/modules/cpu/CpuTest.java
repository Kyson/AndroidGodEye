package cn.hikyson.godeye.core.internal.modules.cpu;

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
public class CpuTest {
    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withCpuConfig(new CpuConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            GodEye.instance().<Cpu>getModule(GodEye.ModuleName.CPU).produce(new CpuInfo(0.6, 0.1, 0.1, 0.1, 0.1));
            GodEye.instance().<Cpu>getModule(GodEye.ModuleName.CPU).produce(new CpuInfo(0.7, 0.1, 0.1, 0.1, 0.1));
            TestObserver<CpuInfo> testObserver = GodEye.instance().<Cpu, CpuInfo>moduleObservable(GodEye.ModuleName.CPU).test();
            GodEye.instance().<Cpu>getModule(GodEye.ModuleName.CPU).produce(new CpuInfo(0.8, 0.1, 0.1, 0.1, 0.1));
            GodEye.instance().<Cpu>getModule(GodEye.ModuleName.CPU).produce(new CpuInfo(0.9, 0.1, 0.1, 0.1, 0.1));
            testObserver.assertValueCount(2).assertValueAt(0, new Predicate<CpuInfo>() {
                @Override
                public boolean test(CpuInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.totalUseRatio == 0.8;
                }
            }).assertValueAt(1, new Predicate<CpuInfo>() {
                @Override
                public boolean test(CpuInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.totalUseRatio == 0.9;
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
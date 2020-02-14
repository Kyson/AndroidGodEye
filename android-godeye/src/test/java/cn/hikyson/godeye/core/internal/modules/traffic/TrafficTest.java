package cn.hikyson.godeye.core.internal.modules.traffic;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.helper.Log4Test;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.godeye.core.helper.ThreadHelper;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class TrafficTest {
    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        ThreadHelper.setupRxjava();
        ThreadUtil.setNeedDetectRunningThread(false);
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withTrafficConfig(new TrafficConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        ThreadHelper.teardownRxjava();
        ThreadUtil.setNeedDetectRunningThread(true);
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            GodEye.instance().<Traffic>getModule(GodEye.ModuleName.TRAFFIC).produce(new TrafficInfo(0.1f, 0.1f, 0.1f, 0.1f));
            GodEye.instance().<Traffic>getModule(GodEye.ModuleName.TRAFFIC).produce(new TrafficInfo(0.2f, 0.1f, 0.1f, 0.1f));
            TestObserver<TrafficInfo> testObserver = GodEye.instance().<Traffic, TrafficInfo>moduleObservable(GodEye.ModuleName.TRAFFIC).test();
            GodEye.instance().<Traffic>getModule(GodEye.ModuleName.TRAFFIC).produce(new TrafficInfo(0.3f, 0.1f, 0.1f, 0.1f));
            GodEye.instance().<Traffic>getModule(GodEye.ModuleName.TRAFFIC).produce(new TrafficInfo(0.4f, 0.1f, 0.1f, 0.1f));
            testObserver.assertValueCount(3).assertValueAt(0, new Predicate<TrafficInfo>() {
                @Override
                public boolean test(TrafficInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.rxTotalRate == 0.2f;
                }
            }).assertValueAt(1, new Predicate<TrafficInfo>() {
                @Override
                public boolean test(TrafficInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.rxTotalRate == 0.3f;
                }
            }).assertValueAt(2, new Predicate<TrafficInfo>() {
                @Override
                public boolean test(TrafficInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.rxTotalRate == 0.4f;
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }

    @Test
    public void work2() {
        ((TestScheduler) ThreadUtil.computationScheduler()).advanceTimeBy(5, TimeUnit.SECONDS);
        try {
            List<TrafficInfo> trafficInfos = GodEye.instance().<Traffic, TrafficInfo>moduleObservable(GodEye.ModuleName.TRAFFIC).test().values();
            Assert.assertTrue(trafficInfos != null && !trafficInfos.isEmpty());
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
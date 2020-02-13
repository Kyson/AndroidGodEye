package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.List;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.helper.Log4Test;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.godeye.core.helper.TestLeak0Activity;
import cn.hikyson.godeye.core.helper.ThreadUtil;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class LeakDetectorTest {

    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withLeakConfig(new LeakConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    @Ignore
    public void work0() {
        ActivityController<TestLeak0Activity> activityController = Robolectric.buildActivity(TestLeak0Activity.class).create().start().resume();
        TestLeak0Activity activity = activityController.get();
        activityController.pause().stop().destroy();
        ThreadUtil.sleep(10000);
        TestObserver<LeakQueue.LeakMemoryInfo> testObserver = null;
        try {
            testObserver = GodEye.instance().<LeakDetector, LeakQueue.LeakMemoryInfo>moduleObservable(GodEye.ModuleName.LEAK).test();
            List<LeakQueue.LeakMemoryInfo> leakMemoryInfos = testObserver.values();
            Log4Test.d(leakMemoryInfos);
        } catch (UninstallException e) {
            Assert.fail();
        }
    }

    @Test
    public void work() {
        try {
            LeakQueue.LeakMemoryInfo leakMemoryInfo0 = new LeakQueue.LeakMemoryInfo("ref0");
            leakMemoryInfo0.status = LeakQueue.LeakMemoryInfo.Status.STATUS_DETECT;
            LeakQueue.LeakMemoryInfo leakMemoryInfo1 = new LeakQueue.LeakMemoryInfo("ref0");
            leakMemoryInfo1.status = LeakQueue.LeakMemoryInfo.Status.STATUS_PROGRESS;
            LeakQueue.LeakMemoryInfo leakMemoryInfo2 = new LeakQueue.LeakMemoryInfo("ref0");
            leakMemoryInfo2.status = LeakQueue.LeakMemoryInfo.Status.STATUS_DONE;
            LeakQueue.LeakMemoryInfo leakMemoryInfo3 = new LeakQueue.LeakMemoryInfo("ref1");
            leakMemoryInfo3.status = LeakQueue.LeakMemoryInfo.Status.STATUS_DETECT;

            GodEye.instance().<LeakDetector>getModule(GodEye.ModuleName.LEAK).produce(leakMemoryInfo0);
            GodEye.instance().<LeakDetector>getModule(GodEye.ModuleName.LEAK).produce(leakMemoryInfo1);
            TestObserver<LeakQueue.LeakMemoryInfo> testObserver = GodEye.instance().<LeakDetector, LeakQueue.LeakMemoryInfo>moduleObservable(GodEye.ModuleName.LEAK).test();
            GodEye.instance().<LeakDetector>getModule(GodEye.ModuleName.LEAK).produce(leakMemoryInfo2);
            GodEye.instance().<LeakDetector>getModule(GodEye.ModuleName.LEAK).produce(leakMemoryInfo3);

            testObserver.assertValueCount(4).assertValueAt(0, new Predicate<LeakQueue.LeakMemoryInfo>() {
                @Override
                public boolean test(LeakQueue.LeakMemoryInfo info) throws Exception {
                    Log4Test.d(info);
                    return "ref0".equals(info.referenceKey) && info.status == LeakQueue.LeakMemoryInfo.Status.STATUS_DETECT;
                }
            }).assertValueAt(1, new Predicate<LeakQueue.LeakMemoryInfo>() {
                @Override
                public boolean test(LeakQueue.LeakMemoryInfo info) throws Exception {
                    Log4Test.d(info);
                    return "ref0".equals(info.referenceKey) && info.status == LeakQueue.LeakMemoryInfo.Status.STATUS_PROGRESS;
                }
            }).assertValueAt(2, new Predicate<LeakQueue.LeakMemoryInfo>() {
                @Override
                public boolean test(LeakQueue.LeakMemoryInfo info) throws Exception {
                    Log4Test.d(info);
                    return "ref0".equals(info.referenceKey) && info.status == LeakQueue.LeakMemoryInfo.Status.STATUS_DONE;
                }
            }).assertValueAt(3, new Predicate<LeakQueue.LeakMemoryInfo>() {
                @Override
                public boolean test(LeakQueue.LeakMemoryInfo info) throws Exception {
                    Log4Test.d(info);
                    return "ref1".equals(info.referenceKey) && info.status == LeakQueue.LeakMemoryInfo.Status.STATUS_DETECT;
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
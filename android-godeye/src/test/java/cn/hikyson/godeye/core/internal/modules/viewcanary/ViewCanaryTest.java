package cn.hikyson.godeye.core.internal.modules.viewcanary;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.helper.Log4Test;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.godeye.core.helper.Test2Activity;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class ViewCanaryTest {

    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withViewCanaryConfig(new ViewCanaryConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }


    @Test
    public void work() {
        try {
            ViewIssueInfo viewIssueInfo0 = new ViewIssueInfo();
            viewIssueInfo0.activityName = "activityName0";
            ViewIssueInfo viewIssueInfo1 = new ViewIssueInfo();
            viewIssueInfo1.activityName = "activityName1";
            ViewIssueInfo viewIssueInfo2 = new ViewIssueInfo();
            viewIssueInfo2.activityName = "activityName2";
            ViewIssueInfo viewIssueInfo3 = new ViewIssueInfo();
            viewIssueInfo3.activityName = "activityName3";

            GodEye.instance().<ViewCanary>getModule(GodEye.ModuleName.VIEW_CANARY).produce(viewIssueInfo0);
            GodEye.instance().<ViewCanary>getModule(GodEye.ModuleName.VIEW_CANARY).produce(viewIssueInfo1);
            TestObserver<ViewIssueInfo> testObserver = GodEye.instance().<ViewCanary, ViewIssueInfo>moduleObservable(GodEye.ModuleName.VIEW_CANARY).test();
            GodEye.instance().<ViewCanary>getModule(GodEye.ModuleName.VIEW_CANARY).produce(viewIssueInfo2);
            GodEye.instance().<ViewCanary>getModule(GodEye.ModuleName.VIEW_CANARY).produce(viewIssueInfo3);
            testObserver.assertValueCount(4).assertValueAt(0, new Predicate<ViewIssueInfo>() {
                @Override
                public boolean test(ViewIssueInfo info) throws Exception {
                    Log4Test.d(info);
                    return "activityName0".equals(info.activityName);
                }
            }).assertValueAt(1, new Predicate<ViewIssueInfo>() {
                @Override
                public boolean test(ViewIssueInfo info) throws Exception {
                    Log4Test.d(info);
                    return "activityName1".equals(info.activityName);
                }
            }).assertValueAt(2, new Predicate<ViewIssueInfo>() {
                @Override
                public boolean test(ViewIssueInfo info) throws Exception {
                    Log4Test.d(info);
                    return "activityName2".equals(info.activityName);
                }
            }).assertValueAt(3, new Predicate<ViewIssueInfo>() {
                @Override
                public boolean test(ViewIssueInfo info) throws Exception {
                    Log4Test.d(info);
                    return "activityName3".equals(info.activityName);
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }

    @Test
    public void work2() {
        ActivityController<Test2Activity> activityController = Robolectric.buildActivity(Test2Activity.class).create().start().resume();
        activityController.pause().stop().destroy();
    }
}
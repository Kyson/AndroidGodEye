package cn.hikyson.godeye.core.internal.modules.imagecanary;

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
import cn.hikyson.godeye.core.helper.Test4ImageActivity;
import cn.hikyson.godeye.core.helper.ThreadUtil;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class ImageCanaryTest {

    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withImageCanaryConfig(new ImageCanaryConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    public void work0() {
        ActivityController<Test4ImageActivity> activityController = Robolectric.buildActivity(Test4ImageActivity.class).create().start().resume();
        ThreadUtil.sleep(1000);
        activityController.pause().stop().destroy();
    }

    @Test
    public void work1() {
        try {
            ImageIssue imageIssue0 = new ImageIssue();
            imageIssue0.imageViewHashCode = 6;
            ImageIssue imageIssue1 = new ImageIssue();
            imageIssue1.imageViewHashCode = 7;
            ImageIssue imageIssue2 = new ImageIssue();
            imageIssue2.imageViewHashCode = 8;
            ImageIssue imageIssue3 = new ImageIssue();
            imageIssue3.imageViewHashCode = 9;

            GodEye.instance().<ImageCanary>getModule(GodEye.ModuleName.IMAGE_CANARY).produce(imageIssue0);
            GodEye.instance().<ImageCanary>getModule(GodEye.ModuleName.IMAGE_CANARY).produce(imageIssue1);
            TestObserver<ImageIssue> testObserver = GodEye.instance().<ImageCanary, ImageIssue>moduleObservable(GodEye.ModuleName.IMAGE_CANARY).test();
            GodEye.instance().<ImageCanary>getModule(GodEye.ModuleName.IMAGE_CANARY).produce(imageIssue2);
            GodEye.instance().<ImageCanary>getModule(GodEye.ModuleName.IMAGE_CANARY).produce(imageIssue3);
            testObserver.assertValueCount(4).assertValueAt(0, new Predicate<ImageIssue>() {
                @Override
                public boolean test(ImageIssue info) throws Exception {
                    Log4Test.d(info);
                    return info.imageViewHashCode == 6;
                }
            }).assertValueAt(1, new Predicate<ImageIssue>() {
                @Override
                public boolean test(ImageIssue info) throws Exception {
                    Log4Test.d(info);
                    return info.imageViewHashCode == 7;
                }
            }).assertValueAt(2, new Predicate<ImageIssue>() {
                @Override
                public boolean test(ImageIssue info) throws Exception {
                    Log4Test.d(info);
                    return info.imageViewHashCode == 8;
                }
            }).assertValueAt(3, new Predicate<ImageIssue>() {
                @Override
                public boolean test(ImageIssue info) throws Exception {
                    Log4Test.d(info);
                    return info.imageViewHashCode == 9;
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
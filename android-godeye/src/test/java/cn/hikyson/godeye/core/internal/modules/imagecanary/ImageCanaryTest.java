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
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.godeye.core.helper.Test4ImageActivity;
import cn.hikyson.godeye.core.helper.ThreadUtil;

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
    public void isInstalled() {
        try {
            Assert.assertTrue(GodEye.instance().<ImageCanary>getModule(GodEye.ModuleName.IMAGE_CANARY).isInstalled());
        } catch (UninstallException e) {
            Assert.fail();
        }
    }

    @Test
    public void work() {
        ActivityController<Test4ImageActivity> activityController = Robolectric.buildActivity(Test4ImageActivity.class).create().start().resume();
        Test4ImageActivity activity = activityController.get();
        ThreadUtil.sleep(1000);
        activityController.pause().stop().destroy();
    }
}
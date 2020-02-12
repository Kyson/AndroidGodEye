package cn.hikyson.godeye.core.internal.modules.sm;

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
import cn.hikyson.godeye.core.helper.ThreadUtil;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class SmTest {

    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
    }

    @After
    public void tearDown() throws Exception {
    }

//    @Test
//    public void isInstalled() {
//        GodEye.instance().uninstall();
//        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withSmConfig(new SmConfig()).build());
//        try {
//            Assert.assertTrue(GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).isInstalled());
//        } catch (UninstallException e) {
//            Assert.fail();
//        }
//    }

    @Test
    public void configAndInstallConfig() {
        GodEye.instance().uninstall();
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withSmConfig(new SmConfig()).build());
        try {
            Assert.assertEquals(new SmConfig().longBlockThreshold(), GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).config().longBlockThreshold());
            Assert.assertEquals(new SmConfig().longBlockThreshold(), GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).installConfig().longBlockThreshold());
            GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).setSmConfigCache(new SmConfig(true, 200, 150, 100));
            GodEye.instance().uninstall();
            GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withSmConfig(new SmConfig()).build());
            Assert.assertEquals(200, GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).config().longBlockThreshold());
            Assert.assertEquals(new SmConfig().longBlockThreshold(), GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).installConfig().longBlockThreshold());
        } catch (UninstallException e) {
            Assert.fail();
        }
    }

    @Test
    public void setSmConfigCache() {
        GodEye.instance().uninstall();
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withSmConfig(new SmConfig()).build());
        try {
            GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).setSmConfigCache(new SmConfig(true, 200, 150, 100));
            Assert.assertEquals(200, GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).getValidSmConfigCache().longBlockThreshold());
            GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).clearSmConfigCache();
            Assert.assertNull(GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).getValidSmConfigCache());
        } catch (UninstallException e) {
            Assert.fail();
        }
    }

    @Test
    public void work() {
        GodEye.instance().uninstall();
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withSmConfig(new SmConfig()).build());
        ThreadUtil.sleep(3000);
        try {
            GodEye.instance().<Sm, BlockInfo>moduleObservable(GodEye.ModuleName.SM).test().assertEmpty();
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
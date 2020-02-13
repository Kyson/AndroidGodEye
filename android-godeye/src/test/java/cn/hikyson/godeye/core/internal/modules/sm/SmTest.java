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
import cn.hikyson.godeye.core.helper.Log4Test;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.godeye.core.internal.modules.sm.core.LongBlockInfo;
import cn.hikyson.godeye.core.internal.modules.sm.core.ShortBlockInfo;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class SmTest {

    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withSmConfig(new SmConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            BlockInfo blockInfo0 = new BlockInfo(new LongBlockInfo(1, 1, 1, 1, true, null, null, null));
            BlockInfo blockInfo1 = new BlockInfo(new LongBlockInfo(2, 1, 1, 1, true, null, null, null));
            BlockInfo blockInfo2 = new BlockInfo(new ShortBlockInfo(3, 1, 1, 1, null));
            BlockInfo blockInfo3 = new BlockInfo(new ShortBlockInfo(4, 1, 1, 1, null));
            GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).produce(blockInfo0);
            GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).produce(blockInfo1);
            TestObserver<BlockInfo> testObserver = GodEye.instance().<Sm, BlockInfo>moduleObservable(GodEye.ModuleName.SM).test();
            GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).produce(blockInfo2);
            GodEye.instance().<Sm>getModule(GodEye.ModuleName.SM).produce(blockInfo3);
            testObserver.assertValueCount(4).assertValueAt(0, new Predicate<BlockInfo>() {
                @Override
                public boolean test(BlockInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.longBlockInfo.timeStart == 1;
                }
            }).assertValueAt(1, new Predicate<BlockInfo>() {
                @Override
                public boolean test(BlockInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.longBlockInfo.timeStart == 2;
                }
            }).assertValueAt(2, new Predicate<BlockInfo>() {
                @Override
                public boolean test(BlockInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.shortBlockInfo.timeStart == 3;
                }
            }).assertValueAt(3, new Predicate<BlockInfo>() {
                @Override
                public boolean test(BlockInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.shortBlockInfo.timeStart == 4;
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }

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
}
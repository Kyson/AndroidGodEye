package cn.hikyson.godeye.core.internal.modules.appsize;

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
public class AppSizeTest {
    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withAppSizeConfig(new AppSizeConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            GodEye.instance().<AppSize>getModule(GodEye.ModuleName.APP_SIZE).produce(new AppSizeInfo(9, 1, 1));
            GodEye.instance().<AppSize>getModule(GodEye.ModuleName.APP_SIZE).produce(new AppSizeInfo(10, 1, 1));
            TestObserver<AppSizeInfo> testObserver = GodEye.instance().<AppSize, AppSizeInfo>moduleObservable(GodEye.ModuleName.APP_SIZE).test();
            GodEye.instance().<AppSize>getModule(GodEye.ModuleName.APP_SIZE).produce(new AppSizeInfo(11, 1, 1));
            GodEye.instance().<AppSize>getModule(GodEye.ModuleName.APP_SIZE).produce(new AppSizeInfo(12, 1, 1));
            testObserver.assertValueCount(3).assertValueAt(0, new Predicate<AppSizeInfo>() {
                @Override
                public boolean test(AppSizeInfo appSizeInfo) throws Exception {
                    return appSizeInfo.cacheSize == 10;
                }
            }).assertValueAt(1, new Predicate<AppSizeInfo>() {
                @Override
                public boolean test(AppSizeInfo appSizeInfo) throws Exception {
                    return appSizeInfo.cacheSize == 11;
                }
            }).assertValueAt(2, new Predicate<AppSizeInfo>() {
                @Override
                public boolean test(AppSizeInfo appSizeInfo) throws Exception {
                    return appSizeInfo.cacheSize == 12;
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
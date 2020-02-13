package cn.hikyson.godeye.core.internal.modules.fps;

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
import cn.hikyson.godeye.core.helper.ChoreographerHelper;
import cn.hikyson.godeye.core.helper.Log4Test;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class FpsTest {

    @Before
    public void setUp() throws Exception {
        ChoreographerHelper.setup();
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withFpsConfig(new FpsConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
        ChoreographerHelper.teardown();
    }

    @Test
    public void work() {
        try {
            GodEye.instance().<Fps>getModule(GodEye.ModuleName.FPS).produce(new FpsInfo(6, 60));
            GodEye.instance().<Fps>getModule(GodEye.ModuleName.FPS).produce(new FpsInfo(7, 60));
            TestObserver<FpsInfo> testObserver = GodEye.instance().<Fps, FpsInfo>moduleObservable(GodEye.ModuleName.FPS).test();
            GodEye.instance().<Fps>getModule(GodEye.ModuleName.FPS).produce(new FpsInfo(8, 60));
            GodEye.instance().<Fps>getModule(GodEye.ModuleName.FPS).produce(new FpsInfo(9, 60));
            testObserver.assertValueCount(3).assertValueAt(0, new Predicate<FpsInfo>() {
                @Override
                public boolean test(FpsInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.currentFps == 7;
                }
            }).assertValueAt(1, new Predicate<FpsInfo>() {
                @Override
                public boolean test(FpsInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.currentFps == 8;
                }
            }).assertValueAt(2, new Predicate<FpsInfo>() {
                @Override
                public boolean test(FpsInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.currentFps == 9;
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
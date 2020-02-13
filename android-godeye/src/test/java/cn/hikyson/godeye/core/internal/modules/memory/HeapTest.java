package cn.hikyson.godeye.core.internal.modules.memory;

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
public class HeapTest {
    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withHeapConfig(new HeapConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            GodEye.instance().<Heap>getModule(GodEye.ModuleName.HEAP).produce(new HeapInfo(1, 1, 1));
            GodEye.instance().<Heap>getModule(GodEye.ModuleName.HEAP).produce(new HeapInfo(2, 2, 2));
            TestObserver<HeapInfo> testObserver = GodEye.instance().<Heap, HeapInfo>moduleObservable(GodEye.ModuleName.HEAP).test();
            GodEye.instance().<Heap>getModule(GodEye.ModuleName.HEAP).produce(new HeapInfo(3, 3, 3));
            GodEye.instance().<Heap>getModule(GodEye.ModuleName.HEAP).produce(new HeapInfo(4, 4, 4));
            testObserver.assertValueCount(3).assertValueAt(0, new Predicate<HeapInfo>() {
                @Override
                public boolean test(HeapInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.freeMemKb == 2;
                }
            }).assertValueAt(1, new Predicate<HeapInfo>() {
                @Override
                public boolean test(HeapInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.freeMemKb == 3;
                }
            }).assertValueAt(2, new Predicate<HeapInfo>() {
                @Override
                public boolean test(HeapInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.freeMemKb == 4;
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
package cn.hikyson.godeye.core.internal.modules.methodcanary;

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
public class MethodCanaryTest {
    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withMethodCanaryConfig(new MethodCanaryConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            MethodsRecordInfo methodsRecordInfo0 = MethodCanaryConverterTest.mockMethodsRecordInfo(MethodCanaryConverterTest.mockMethodEventMap());
            methodsRecordInfo0.start = 6;
            MethodsRecordInfo methodsRecordInfo1 = MethodCanaryConverterTest.mockMethodsRecordInfo(MethodCanaryConverterTest.mockMethodEventMap());
            methodsRecordInfo1.start = 7;
            MethodsRecordInfo methodsRecordInfo2 = MethodCanaryConverterTest.mockMethodsRecordInfo(MethodCanaryConverterTest.mockMethodEventMap());
            methodsRecordInfo2.start = 8;
            MethodsRecordInfo methodsRecordInfo3 = MethodCanaryConverterTest.mockMethodsRecordInfo(MethodCanaryConverterTest.mockMethodEventMap());
            methodsRecordInfo3.start = 9;

            GodEye.instance().<MethodCanary>getModule(GodEye.ModuleName.METHOD_CANARY).produce(methodsRecordInfo0);
            GodEye.instance().<MethodCanary>getModule(GodEye.ModuleName.METHOD_CANARY).produce(methodsRecordInfo1);
            TestObserver<MethodsRecordInfo> testObserver = GodEye.instance().<MethodCanary, MethodsRecordInfo>moduleObservable(GodEye.ModuleName.METHOD_CANARY).test();
            GodEye.instance().<MethodCanary>getModule(GodEye.ModuleName.METHOD_CANARY).produce(methodsRecordInfo2);
            GodEye.instance().<MethodCanary>getModule(GodEye.ModuleName.METHOD_CANARY).produce(methodsRecordInfo3);
            testObserver.assertValueCount(3).assertValueAt(0, new Predicate<MethodsRecordInfo>() {
                @Override
                public boolean test(MethodsRecordInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.start == 7;
                }
            }).assertValueAt(1, new Predicate<MethodsRecordInfo>() {
                @Override
                public boolean test(MethodsRecordInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.start == 8;
                }
            }).assertValueAt(2, new Predicate<MethodsRecordInfo>() {
                @Override
                public boolean test(MethodsRecordInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.start == 9;
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
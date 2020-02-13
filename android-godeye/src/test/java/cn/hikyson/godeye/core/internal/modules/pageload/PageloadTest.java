package cn.hikyson.godeye.core.internal.modules.pageload;

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
import cn.hikyson.godeye.core.helper.Test1Activity;
import cn.hikyson.godeye.core.helper.Test1Fragment;
import cn.hikyson.godeye.core.helper.Test2Activity;
import cn.hikyson.godeye.core.helper.Test2Fragment;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class PageloadTest {

    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withPageloadConfig(new PageloadConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            PageLifecycleEventInfo pageLifecycleEventInfo0 = new PageLifecycleEventInfo();
            pageLifecycleEventInfo0.pageInfo = new PageInfo<>(new Test1Activity(), null);
            PageLifecycleEventInfo pageLifecycleEventInfo1 = new PageLifecycleEventInfo();
            pageLifecycleEventInfo1.pageInfo = new PageInfo<>(new Test2Activity(), null);
            PageLifecycleEventInfo pageLifecycleEventInfo2 = new PageLifecycleEventInfo();
            pageLifecycleEventInfo2.pageInfo = new PageInfo<>(new Test1Fragment(), null);
            PageLifecycleEventInfo pageLifecycleEventInfo3 = new PageLifecycleEventInfo();
            pageLifecycleEventInfo3.pageInfo = new PageInfo<>(new Test2Fragment(), null);

            GodEye.instance().<Pageload>getModule(GodEye.ModuleName.PAGELOAD).produce(pageLifecycleEventInfo0);
            GodEye.instance().<Pageload>getModule(GodEye.ModuleName.PAGELOAD).produce(pageLifecycleEventInfo1);
            TestObserver<PageLifecycleEventInfo> testObserver = GodEye.instance().<Pageload, PageLifecycleEventInfo>moduleObservable(GodEye.ModuleName.PAGELOAD).test();
            GodEye.instance().<Pageload>getModule(GodEye.ModuleName.PAGELOAD).produce(pageLifecycleEventInfo2);
            GodEye.instance().<Pageload>getModule(GodEye.ModuleName.PAGELOAD).produce(pageLifecycleEventInfo3);
            testObserver.assertValueCount(4).assertValueAt(0, new Predicate<PageLifecycleEventInfo>() {
                @Override
                public boolean test(PageLifecycleEventInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.pageInfo.pageType == PageType.ACTIVITY
                            && Test1Activity.class.getName().equals(info.pageInfo.pageClassName);
                }
            }).assertValueAt(1, new Predicate<PageLifecycleEventInfo>() {
                @Override
                public boolean test(PageLifecycleEventInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.pageInfo.pageType == PageType.ACTIVITY
                            && Test2Activity.class.getName().equals(info.pageInfo.pageClassName);
                }
            }).assertValueAt(2, new Predicate<PageLifecycleEventInfo>() {
                @Override
                public boolean test(PageLifecycleEventInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.pageInfo.pageType == PageType.FRAGMENT
                            && Test1Fragment.class.getName().equals(info.pageInfo.pageClassName);
                }
            }).assertValueAt(3, new Predicate<PageLifecycleEventInfo>() {
                @Override
                public boolean test(PageLifecycleEventInfo info) throws Exception {
                    Log4Test.d(info);
                    return info.pageInfo.pageType == PageType.FRAGMENT
                            && Test2Fragment.class.getName().equals(info.pageInfo.pageClassName);
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
package cn.hikyson.godeye.core.internal.modules.viewcanary;

import android.app.Activity;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.godeye.core.helper.TestViewCanaryActivity;
import cn.hikyson.godeye.core.internal.modules.viewcanary.levenshtein.ViewIdWithSize;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class ViewCanaryInternalTest {

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
    public void inspectInner() {
        ActivityController<TestViewCanaryActivity> activityController = Robolectric.buildActivity(TestViewCanaryActivity.class).create().start().resume();
        ViewCanaryInternal viewCanaryInternal = new ViewCanaryInternal();
        Map<Activity, List<List<ViewIdWithSize>>> recentLayoutListRecords = new HashMap<>();
        try {
            viewCanaryInternal.inspectInner(new WeakReference<>(activityController.get()), GodEye.instance().<ViewCanary>getModule(GodEye.ModuleName.VIEW_CANARY), GodEye.instance().<ViewCanary>getModule(GodEye.ModuleName.VIEW_CANARY).config(), recentLayoutListRecords).run();
            GodEye.instance().<ViewCanary, ViewIssueInfo>moduleObservable(GodEye.ModuleName.VIEW_CANARY).test().assertNoValues();

            recentLayoutListRecords.put(activityController.get(), new ArrayList<>());
            viewCanaryInternal.inspectInner(new WeakReference<>(activityController.get()), GodEye.instance().<ViewCanary>getModule(GodEye.ModuleName.VIEW_CANARY), GodEye.instance().<ViewCanary>getModule(GodEye.ModuleName.VIEW_CANARY).config(), recentLayoutListRecords).run();
            GodEye.instance().<ViewCanary, ViewIssueInfo>moduleObservable(GodEye.ModuleName.VIEW_CANARY).test().assertValueCount(1);

            viewCanaryInternal.inspectInner(new WeakReference<>(activityController.get()), GodEye.instance().<ViewCanary>getModule(GodEye.ModuleName.VIEW_CANARY), GodEye.instance().<ViewCanary>getModule(GodEye.ModuleName.VIEW_CANARY).config(), recentLayoutListRecords).run();
            GodEye.instance().<ViewCanary, ViewIssueInfo>moduleObservable(GodEye.ModuleName.VIEW_CANARY).test().assertValueCount(1);
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
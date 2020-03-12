package cn.hikyson.godeye.core;

import android.app.Activity;
import android.os.Build;

import androidx.fragment.app.Fragment;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.helper.ChoreographerHelper;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.godeye.core.helper.Test1Activity;
import cn.hikyson.godeye.core.helper.Test1Fragment;
import cn.hikyson.godeye.core.helper.Test2Activity;
import cn.hikyson.godeye.core.helper.Test2NotV4FragmentActivity;
import cn.hikyson.godeye.core.helper.TestPageEvent;
import cn.hikyson.godeye.core.helper.ThreadHelper;
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanaryConfig;
import cn.hikyson.godeye.core.internal.modules.network.Network;
import cn.hikyson.godeye.core.internal.modules.network.NetworkConfig;
import cn.hikyson.godeye.core.internal.modules.network.NetworkContent;
import cn.hikyson.godeye.core.internal.modules.network.NetworkInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.ActivityLifecycleEvent;
import cn.hikyson.godeye.core.internal.modules.pageload.FragmentLifecycleEvent;
import cn.hikyson.godeye.core.internal.modules.pageload.PageLifecycleEventInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.Pageload;
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadConfig;
import cn.hikyson.godeye.core.internal.modules.startup.Startup;
import cn.hikyson.godeye.core.internal.modules.startup.StartupConfig;
import cn.hikyson.godeye.core.internal.modules.startup.StartupInfo;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class GodEyeHelperTest {

    @Before
    public void setUp() throws Exception {
        ThreadHelper.setupRxjava();
        ChoreographerHelper.setup();
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
    }

    @After
    public void tearDown() throws Exception {
        ChoreographerHelper.teardown();
        ThreadHelper.teardownRxjava();
    }

    @Test
    public void onPageLoadedCannotBeNull() {
        try {
            GodEye.instance().uninstall();
            GodEyeHelper.onPageLoaded(null);
            fail();
        } catch (RuntimeException ignore) {

        } catch (UninstallException e) {
            fail();
        }
    }

    @Test
    public void onPageLoadedNotInstalled() {
        try {
            GodEye.instance().uninstall();
            GodEyeHelper.onPageLoaded(new Activity());
            fail();
        } catch (UninstallException ignore) {
        }
        try {
            GodEye.instance().uninstall();
            GodEyeHelper.onPageLoaded(new Fragment());
            fail();
        } catch (UninstallException ignore) {
        }
    }

    @Test
    public void onPageLoadedWhenNotCreated() {
        GodEye.instance().uninstall();
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withPageloadConfig(new PageloadConfig()).build());
        try {
            TestObserver<PageLifecycleEventInfo> testObserver = GodEye.instance().<Pageload, PageLifecycleEventInfo>moduleObservable(GodEye.ModuleName.PAGELOAD).test();
            Activity activity = new Activity();
            GodEyeHelper.onPageLoaded(activity);
            Fragment fragment = new Fragment();
            GodEyeHelper.onPageLoaded(fragment);
            Shadows.shadowOf(ThreadUtil.obtainHandler("godeye-pageload").getLooper()).getScheduler().advanceToNextPostedRunnable();
            testObserver.await(1, TimeUnit.SECONDS);
            testObserver.assertValueAt(0, new Predicate<PageLifecycleEventInfo>() {
                @Override
                public boolean test(PageLifecycleEventInfo o) throws Exception {
                    return activity.hashCode() == o.pageInfo.pageHashCode
                            && ActivityLifecycleEvent.ON_LOAD.equals(o.currentEvent.lifecycleEvent);
                }
            }).assertValueAt(1, new Predicate<PageLifecycleEventInfo>() {
                @Override
                public boolean test(PageLifecycleEventInfo o) throws Exception {
                    return fragment.hashCode() == o.pageInfo.pageHashCode
                            && FragmentLifecycleEvent.ON_LOAD.equals(o.currentEvent.lifecycleEvent);
                }
            });
        } catch (UninstallException e) {
            fail();
        } catch (InterruptedException e) {
            fail();
        }
    }

    @Test
    public void onPageLoadedSuccessForActivity() {
        try {
            GodEye.instance().uninstall();
            GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withPageloadConfig(new PageloadConfig()).build());
            ActivityController<Test1Activity> activityController = Robolectric.buildActivity(Test1Activity.class).create().start().resume();
            Test1Activity activity = activityController.get();
            TestObserver testObserver = GodEye.instance().<Pageload, PageLifecycleEventInfo>moduleObservable(GodEye.ModuleName.PAGELOAD).test();
            GodEyeHelper.onPageLoaded(activity);
            activityController.pause().stop().destroy();
            Shadows.shadowOf(ThreadUtil.obtainHandler("godeye-pageload").getLooper()).getScheduler().advanceToNextPostedRunnable();

            List<TestPageEvent> testPageEvents = new ArrayList<>();
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_CREATE, 1));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_START, 2));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_DRAW, 3));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_RESUME, 4));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_LOAD, 5));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_PAUSE, 6));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_STOP, 7));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_DESTROY, 8));
            testObserver.assertValueCount(8);
            for (int i = 0; i < 8; i++) {
                int finalI = i;
                testObserver.assertValueAt(i, new Predicate<PageLifecycleEventInfo>() {
                    @Override
                    public boolean test(PageLifecycleEventInfo o) throws Exception {
                        return testPageEvents.get(finalI).pageHashCode == o.pageInfo.pageHashCode
                                && testPageEvents.get(finalI).allEventSize == o.allEvents.size()
                                && testPageEvents.get(finalI).lifecycleEvent.equals(o.currentEvent.lifecycleEvent);
                    }
                });
            }
        } catch (UninstallException e) {
            fail();
        }
    }

    /**
     * ACTIVITYTest2Activity, pageHashCode=638030090,ON_CREATE
     * FRAGMENTTest1Fragment, pageHashCode=436993154,ON_ATTACH
     * FRAGMENTTest1Fragment, pageHashCode=436993154,ON_CREATE
     * ACTIVITYTest2Activity, pageHashCode=638030090,ON_START
     * FRAGMENTTest1Fragment, pageHashCode=436993154,ON_VIEW_CREATE
     * FRAGMENTTest1Fragment, pageHashCode=436993154,ON_START
     * ACTIVITYTest2Activity, pageHashCode=638030090,ON_DRAW
     * FRAGMENTTest1Fragment, pageHashCode=436993154,ON_DRAW
     * ACTIVITYTest2Activity, pageHashCode=638030090,ON_RESUME
     * FRAGMENTTest1Fragment, pageHashCode=436993154,ON_RESUME
     * ACTIVITYTest2Activity, pageHashCode=638030090,ON_LOAD
     * FRAGMENTTest1Fragment, pageHashCode=436993154,ON_LOAD
     * ACTIVITYTest2Activity, pageHashCode=638030090,ON_PAUSE
     * FRAGMENTTest1Fragment, pageHashCode=436993154,ON_PAUSE
     * ACTIVITYTest2Activity, pageHashCode=638030090,ON_STOP
     * FRAGMENTTest1Fragment, pageHashCode=436993154,ON_STOP
     * ACTIVITYTest2Activity, pageHashCode=638030090,ON_DESTROY
     * FRAGMENTTest1Fragment, pageHashCode=436993154,ON_VIEW_DESTROY
     * FRAGMENTTest1Fragment, pageHashCode=436993154,ON_DESTROY
     * FRAGMENTTest1Fragment, pageHashCode=436993154,ON_DETACH
     */
    @Test
    public void onPageLoadedSuccessWithFragment() {
        try {
            GodEye.instance().uninstall();
            GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withPageloadConfig(new PageloadConfig()).build());
            ActivityController<Test2Activity> activityController = Robolectric.buildActivity(Test2Activity.class).create().start().resume();
            Test2Activity activity = activityController.get();
            GodEyeHelper.onPageLoaded(activity);
            TestObserver testObserver = GodEye.instance().<Pageload, PageLifecycleEventInfo>moduleObservable(GodEye.ModuleName.PAGELOAD).test();
            Test1Fragment fragment = activity.getTest1Fragment();
            GodEyeHelper.onPageLoaded(fragment);
            activityController.pause().stop().destroy();
            Shadows.shadowOf(ThreadUtil.obtainHandler("godeye-pageload").getLooper()).getScheduler().advanceToNextPostedRunnable();
            List<TestPageEvent> testPageEvents = new ArrayList<>();
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_CREATE, 1));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_ATTACH, 1));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_CREATE, 2));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_START, 2));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_VIEW_CREATE, 3));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_START, 4));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_DRAW, 3));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_DRAW, 5));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_RESUME, 4));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_RESUME, 6));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_LOAD, 5));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_LOAD, 7));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_PAUSE, 6));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_PAUSE, 8));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_STOP, 7));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_STOP, 9));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_DESTROY, 8));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_VIEW_DESTROY, 10));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_DESTROY, 11));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_DETACH, 12));
            testObserver.assertValueCount(20);
            for (int i = 0; i < 20; i++) {
                int finalI = i;
                testObserver.assertValueAt(i, new Predicate<PageLifecycleEventInfo>() {
                    @Override
                    public boolean test(PageLifecycleEventInfo o) throws Exception {
                        return testPageEvents.get(finalI).pageHashCode == o.pageInfo.pageHashCode
                                && testPageEvents.get(finalI).allEventSize == o.allEvents.size()
                                && testPageEvents.get(finalI).lifecycleEvent.equals(o.currentEvent.lifecycleEvent);
                    }
                });
            }
        } catch (UninstallException e) {
            fail();
        }
    }

    /**
     * Test2NotV4FragmentActivityON_CREATE
     * Test2NotV4FragmentActivityON_START
     * Test2NotV4FragmentActivityON_DRAW
     * Test2NotV4FragmentActivityON_RESUME
     * Test1NotV4FragmentON_SHOW
     * Test1NotV4FragmentON_HIDE
     * Test2NotV4FragmentActivityON_LOAD
     * Test1NotV4FragmentON_LOAD
     * Test2NotV4FragmentActivityON_PAUSE
     * Test2NotV4FragmentActivityON_STOP
     * Test2NotV4FragmentActivityON_DESTROY
     */
    @Test
    public void onFragmentPageVisibilityChangeSuccess() {
        try {
            GodEye.instance().uninstall();
            GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withPageloadConfig(new PageloadConfig()).build());
            Activity activity = null;
            Object fragment = null;
            TestObserver testObserver = null;
            ActivityController<Test2NotV4FragmentActivity> activityController = Robolectric.buildActivity(Test2NotV4FragmentActivity.class).create().start().resume();
            activity = activityController.get();
            testObserver = GodEye.instance().<Pageload, PageLifecycleEventInfo>moduleObservable(GodEye.ModuleName.PAGELOAD).test();
            fragment = ((Test2NotV4FragmentActivity) activity).getTest1NotV4Fragment();
            GodEyeHelper.onFragmentPageVisibilityChange(fragment, true);
            Thread.sleep(100);
            GodEyeHelper.onFragmentPageVisibilityChange(fragment, false);
            GodEyeHelper.onPageLoaded(activity);
            GodEyeHelper.onPageLoaded(fragment);
            activityController.pause().stop().destroy();
            Shadows.shadowOf(ThreadUtil.obtainHandler("godeye-pageload").getLooper()).getScheduler().advanceToNextPostedRunnable();
            List<TestPageEvent> testPageEvents = new ArrayList<>();
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_CREATE, 1));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_START, 2));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_DRAW, 3));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_RESUME, 4));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_SHOW, 1));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_HIDE, 2));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_LOAD, 5));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_LOAD, 3));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_PAUSE, 6));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_STOP, 7));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_DESTROY, 8));
            testObserver.assertValueCount(11);
            for (int i = 0; i < 11; i++) {
                int finalI = i;
                testObserver.assertValueAt(i, new Predicate<PageLifecycleEventInfo>() {
                    @Override
                    public boolean test(PageLifecycleEventInfo o) throws Exception {
                        return testPageEvents.get(finalI).pageHashCode == o.pageInfo.pageHashCode
                                && testPageEvents.get(finalI).allEventSize == o.allEvents.size()
                                && testPageEvents.get(finalI).lifecycleEvent.equals(o.currentEvent.lifecycleEvent);
                    }
                });
            }
        } catch (UninstallException e) {
            fail();
        } catch (InterruptedException e) {
            fail();
        }
    }

    /**
     * ACTIVITY,Test2Activity,ON_CREATE
     * FRAGMENT,Test1Fragment,ON_ATTACH
     * FRAGMENT,Test1Fragment,ON_CREATE
     * ACTIVITY,Test2Activity,ON_START
     * FRAGMENT,Test1Fragment,ON_VIEW_CREATE
     * FRAGMENT,Test1Fragment,ON_START
     * ACTIVITY,Test2Activity,ON_DRAW
     * FRAGMENT,Test1Fragment,ON_DRAW
     * ACTIVITY,Test2Activity,ON_RESUME
     * FRAGMENT,Test1Fragment,ON_RESUME
     * FRAGMENT,Test1Fragment,ON_SHOW
     * FRAGMENT,Test1Fragment,ON_HIDE
     * ACTIVITY,Test2Activity,ON_LOAD
     * FRAGMENT,Test1Fragment,ON_LOAD
     * ACTIVITY,Test2Activity,ON_PAUSE
     * FRAGMENT,Test1Fragment,ON_PAUSE
     * ACTIVITY,Test2Activity,ON_STOP
     * FRAGMENT,Test1Fragment,ON_STOP
     * ACTIVITY,Test2Activity,ON_DESTROY
     * FRAGMENT,Test1Fragment,ON_VIEW_DESTROY
     * FRAGMENT,Test1Fragment,ON_DESTROY
     * FRAGMENT,Test1Fragment,ON_DETACH
     */
    @Test
    public void onFragmentPageVisibilityChangeSuccessV4() {
        try {
            GodEye.instance().uninstall();
            GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withPageloadConfig(new PageloadConfig()).build());
            Activity activity = null;
            Object fragment = null;
            TestObserver testObserver = null;
            ActivityController<Test2Activity> activityController = Robolectric.buildActivity(Test2Activity.class).create().start().resume();
            activity = activityController.get();
            testObserver = GodEye.instance().<Pageload, PageLifecycleEventInfo>moduleObservable(GodEye.ModuleName.PAGELOAD).test();
            fragment = ((Test2Activity) activity).getTest1Fragment();
            GodEyeHelper.onFragmentPageVisibilityChange(fragment, true);
            Thread.sleep(100);
            GodEyeHelper.onFragmentPageVisibilityChange(fragment, false);
            GodEyeHelper.onPageLoaded(activity);
            GodEyeHelper.onPageLoaded(fragment);
            activityController.pause().stop().destroy();
            Shadows.shadowOf(ThreadUtil.obtainHandler("godeye-pageload").getLooper()).getScheduler().advanceToNextPostedRunnable();
            List<TestPageEvent> testPageEvents = new ArrayList<>();
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_CREATE, 1));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_ATTACH, 1));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_CREATE, 2));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_START, 2));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_VIEW_CREATE, 3));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_START, 4));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_DRAW, 3));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_DRAW, 5));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_RESUME, 4));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_RESUME, 6));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_SHOW, 7));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_HIDE, 8));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_LOAD, 5));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_LOAD, 9));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_PAUSE, 6));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_PAUSE, 10));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_STOP, 7));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_STOP, 11));
            testPageEvents.add(new TestPageEvent(activity.hashCode(), ActivityLifecycleEvent.ON_DESTROY, 8));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_VIEW_DESTROY, 12));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_DESTROY, 13));
            testPageEvents.add(new TestPageEvent(fragment.hashCode(), FragmentLifecycleEvent.ON_DETACH, 14));
            testObserver.assertValueCount(22);
            for (int i = 0; i < 22; i++) {
                int finalI = i;
                testObserver.assertValueAt(i, new Predicate<PageLifecycleEventInfo>() {
                    @Override
                    public boolean test(PageLifecycleEventInfo o) throws Exception {
                        return testPageEvents.get(finalI).pageHashCode == o.pageInfo.pageHashCode
                                && testPageEvents.get(finalI).allEventSize == o.allEvents.size()
                                && testPageEvents.get(finalI).lifecycleEvent.equals(o.currentEvent.lifecycleEvent);
                    }
                });
            }
        } catch (UninstallException e) {
            fail();
        } catch (InterruptedException e) {
            fail();
        }
    }


    @Test
    public void onFragmentPageVisibilityChangeWhenIllegal() {
        try {
            GodEye.instance().uninstall();
            GodEyeHelper.onFragmentPageVisibilityChange(null, true);
            fail();
        } catch (RuntimeException ignore) {// success
        } catch (UninstallException e) {
            fail();
        }
    }

    @Test
    public void onAppStartEndWhenNotInstalled() {
        try {
            GodEye.instance().uninstall();
            GodEyeHelper.onAppStartEnd(null);
            fail();
        } catch (UninstallException ignore) {
        }
    }

    @Test
    public void onAppStartEndWhenNull() {
        try {
            GodEye.instance().uninstall();
            GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withStartupConfig(new StartupConfig()).build());
            GodEyeHelper.onAppStartEnd(null);
        } catch (NullPointerException ignore) {
            // success
        } catch (UninstallException e) {
            fail();
        }
    }

    @Test
    public void onAppStartEndSuccess() {
        try {
            GodEye.instance().uninstall();
            GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withStartupConfig(new StartupConfig()).build());
            StartupInfo startupInfo = new StartupInfo(StartupInfo.StartUpType.COLD, 3000);
            GodEyeHelper.onAppStartEnd(startupInfo);
            TestObserver testObserver = GodEye.instance().<Startup, StartupInfo>moduleObservable(GodEye.ModuleName.STARTUP).test();
            testObserver.assertValue(new Predicate<StartupInfo>() {
                @Override
                public boolean test(StartupInfo o) throws Exception {
                    return startupInfo.startupType.equals(o.startupType) && startupInfo.startupTime == o.startupTime;
                }
            });
        } catch (Throwable e) {
            fail();
        }
    }


    @Test
    public void onNetworkEndWhenNotInstalled() {
        try {
            GodEye.instance().uninstall();
            GodEyeHelper.onNetworkEnd(null);
            fail();
        } catch (UninstallException ignore) {
        }
    }

    @Test
    public void onNetworkEndWhenNull() {
        try {
            GodEye.instance().uninstall();
            GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withNetworkConfig(new NetworkConfig()).build());
            GodEyeHelper.onNetworkEnd(null);
        } catch (NullPointerException ignore) {
            // success
        } catch (UninstallException e) {
            fail();
        }
    }

    @Test
    public void onNetworkEndSuccess() {
        try {
            GodEye.instance().uninstall();
            GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withNetworkConfig(new NetworkConfig()).build());
            NetworkInfo<NetworkContent> networkInfo = new NetworkInfo<>();
            networkInfo.isSuccessful = true;
            networkInfo.message = "message";
            GodEyeHelper.onNetworkEnd(networkInfo);
            TestObserver testObserver = GodEye.instance().<Network, NetworkInfo>moduleObservable(GodEye.ModuleName.NETWORK).test();
            testObserver.assertValue(new Predicate<NetworkInfo>() {
                @Override
                public boolean test(NetworkInfo o) throws Exception {
                    return networkInfo.isSuccessful == o.isSuccessful && networkInfo.message.equals(o.message);
                }
            });
        } catch (Throwable e) {
            fail();
        }
    }

    @Test
    public void startMethodCanaryRecordingWhenNotInstalled() {
        try {
            GodEye.instance().uninstall();
            GodEyeHelper.startMethodCanaryRecording("tag");
            fail();
        } catch (UninstallException ignore) {
        }
    }

    @Test
    public void startMethodCanaryRecordingSuccess() {
        try {
            GodEye.instance().uninstall();
            GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withMethodCanaryConfig(new MethodCanaryConfig()).build());
            GodEyeHelper.startMethodCanaryRecording("tag");
        } catch (UninstallException ignore) {
            fail();
        }
    }

    @Test
    public void stopMethodCanaryRecordingWhenNotInstalled() {
        try {
            GodEye.instance().uninstall();
            GodEyeHelper.stopMethodCanaryRecording("tag");
            fail();
        } catch (UninstallException ignore) {
        }
    }

    @Test
    public void stopMethodCanaryRecordingSuccess() {
        try {
            GodEye.instance().uninstall();
            GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withMethodCanaryConfig(new MethodCanaryConfig()).build());
            GodEyeHelper.stopMethodCanaryRecording("tag");
        } catch (UninstallException ignore) {
            fail();
        }
    }

    @Test
    public void isMethodCanaryRecordingWhenNotInstalled() {
        try {
            GodEye.instance().uninstall();
            GodEyeHelper.isMethodCanaryRecording("tag");
            fail();
        } catch (UninstallException ignore) {
        }
    }

    @Test
    public void isMethodCanaryRecordingSuccess() {
        try {
            GodEye.instance().uninstall();
            GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withMethodCanaryConfig(new MethodCanaryConfig()).build());
            GodEyeHelper.startMethodCanaryRecording("tag");
            assertTrue(GodEyeHelper.isMethodCanaryRecording("tag"));
            GodEyeHelper.stopMethodCanaryRecording("tag");
            assertFalse(GodEyeHelper.isMethodCanaryRecording("tag"));
        } catch (UninstallException ignore) {
            fail();
        }
    }
}
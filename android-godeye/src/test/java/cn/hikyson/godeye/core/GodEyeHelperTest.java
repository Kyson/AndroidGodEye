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

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.godeye.core.helper.Test1Activity;
import cn.hikyson.godeye.core.internal.modules.pageload.ActivityLifecycleEvent;
import cn.hikyson.godeye.core.internal.modules.pageload.PageLifecycleEventInfo;
import cn.hikyson.godeye.core.internal.modules.pageload.Pageload;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;

import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class GodEyeHelperTest {

    @Before
    public void setUp() throws Exception {
        TestScheduler testScheduler = new TestScheduler();
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return testScheduler;
            }
        });
        RxJavaPlugins.setComputationSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return testScheduler;
            }
        });
        RxJavaPlugins.setSingleSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return testScheduler;
            }
        });
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void onPageLoadedCannotBeNull() {
        try {
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
    public void onPageLoadedNoResultWhenNotCreated() {
        GodEye.instance().uninstall();
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withPageloadConfig(new GodEyeConfig.PageloadConfig()).build());
        try {
            TestObserver testObserver = GodEye.instance().<Pageload, PageLifecycleEventInfo>moduleObservable(GodEye.ModuleName.PAGELOAD).test();
            GodEyeHelper.onPageLoaded(new Activity());
            GodEyeHelper.onPageLoaded(new Fragment());
            Shadows.shadowOf(ThreadUtil.obtainHandler("godeye-pageload").getLooper()).getScheduler().advanceToNextPostedRunnable();
            testObserver.await(1, TimeUnit.SECONDS);
            testObserver.assertNoValues();
        } catch (UninstallException e) {
            fail();
        } catch (InterruptedException e) {
            fail();
        }
    }

    @Test
    public void onPageLoadedSuccess() {
        try {
            GodEye.instance().uninstall();
            GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withPageloadConfig(new GodEyeConfig.PageloadConfig()).build());
            ActivityController<Test1Activity> activityController = Robolectric.buildActivity(Test1Activity.class).create().start().resume();
            TestObserver testObserver = GodEye.instance().<Pageload, PageLifecycleEventInfo>moduleObservable(GodEye.ModuleName.PAGELOAD).test();
            GodEyeHelper.onPageLoaded(activityController.get());
            activityController.pause().stop().destroy();
            Shadows.shadowOf(ThreadUtil.obtainHandler("godeye-pageload").getLooper()).getScheduler().advanceToNextPostedRunnable();
            testObserver.assertValueAt(0, new Predicate<PageLifecycleEventInfo>() {
                @Override
                public boolean test(PageLifecycleEventInfo o) throws Exception {
                    return activityController.get().hashCode() == o.pageInfo.pageHashCode
                            && o.allEvents.size() == 1
                            && activityController.get().hashCode() == o.currentEvent.pageInfo.pageHashCode
                            && o.currentEvent.lifecycleEvent.equals(ActivityLifecycleEvent.ON_CREATE);
                }
            });
            testObserver.assertValueAt(1, new Predicate<PageLifecycleEventInfo>() {
                @Override
                public boolean test(PageLifecycleEventInfo o) throws Exception {
                    return activityController.get().hashCode() == o.pageInfo.pageHashCode
                            && o.allEvents.size() == 2
                            && activityController.get().hashCode() == o.currentEvent.pageInfo.pageHashCode
                            && o.currentEvent.lifecycleEvent.equals(ActivityLifecycleEvent.ON_START);
                }
            });
            testObserver.assertValueAt(2, new Predicate<PageLifecycleEventInfo>() {
                @Override
                public boolean test(PageLifecycleEventInfo o) throws Exception {
                    return activityController.get().hashCode() == o.pageInfo.pageHashCode
                            && o.allEvents.size() == 3
                            && activityController.get().hashCode() == o.currentEvent.pageInfo.pageHashCode
                            && o.currentEvent.lifecycleEvent.equals(ActivityLifecycleEvent.ON_DRAW);
                }
            });
            testObserver.assertValueAt(3, new Predicate<PageLifecycleEventInfo>() {
                @Override
                public boolean test(PageLifecycleEventInfo o) throws Exception {
                    return activityController.get().hashCode() == o.pageInfo.pageHashCode
                            && o.allEvents.size() == 4
                            && activityController.get().hashCode() == o.currentEvent.pageInfo.pageHashCode
                            && o.currentEvent.lifecycleEvent.equals(ActivityLifecycleEvent.ON_RESUME);
                }
            });

            testObserver.assertValueAt(4, new Predicate<PageLifecycleEventInfo>() {
                @Override
                public boolean test(PageLifecycleEventInfo o) throws Exception {
                    return activityController.get().hashCode() == o.pageInfo.pageHashCode
                            && o.allEvents.size() == 5
                            && activityController.get().hashCode() == o.currentEvent.pageInfo.pageHashCode
                            && o.currentEvent.lifecycleEvent.equals(ActivityLifecycleEvent.ON_LOAD);
                }
            });


            testObserver.assertValueAt(5, new Predicate<PageLifecycleEventInfo>() {
                @Override
                public boolean test(PageLifecycleEventInfo o) throws Exception {
                    return activityController.get().hashCode() == o.pageInfo.pageHashCode
                            && o.allEvents.size() == 6
                            && activityController.get().hashCode() == o.currentEvent.pageInfo.pageHashCode
                            && o.currentEvent.lifecycleEvent.equals(ActivityLifecycleEvent.ON_PAUSE);
                }
            });
            testObserver.assertValueAt(6, new Predicate<PageLifecycleEventInfo>() {
                @Override
                public boolean test(PageLifecycleEventInfo o) throws Exception {
                    return activityController.get().hashCode() == o.pageInfo.pageHashCode
                            && o.allEvents.size() == 7
                            && activityController.get().hashCode() == o.currentEvent.pageInfo.pageHashCode
                            && o.currentEvent.lifecycleEvent.equals(ActivityLifecycleEvent.ON_STOP);
                }
            });
            testObserver.assertValueAt(7, new Predicate<PageLifecycleEventInfo>() {
                @Override
                public boolean test(PageLifecycleEventInfo o) throws Exception {
                    return activityController.get().hashCode() == o.pageInfo.pageHashCode
                            && o.allEvents.size() == 8
                            && activityController.get().hashCode() == o.currentEvent.pageInfo.pageHashCode
                            && o.currentEvent.lifecycleEvent.equals(ActivityLifecycleEvent.ON_DESTROY);
                }
            });
        } catch (UninstallException e) {
            fail();
        }
    }

    @Test
    public void onFragmentPageVisibilityChange() {
        GodEye.instance().uninstall();
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withPageloadConfig(new GodEyeConfig.PageloadConfig()).build());
    }

    @Test
    public void onAppStartEnd() {
    }

    @Test
    public void onNetworkEnd() {
    }

    @Test
    public void inspectView() {
    }

    @Test
    public void startMethodCanaryRecording() {
    }

    @Test
    public void stopMethodCanaryRecording() {
    }

    @Test
    public void isMethodCanaryRecording() {
    }
}
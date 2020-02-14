package cn.hikyson.godeye.core.internal.modules.thread;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.helper.Log4Test;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.godeye.core.helper.ThreadHelper;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class ThreadDumpTest {

    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
        ThreadHelper.setupRxjava();
        ThreadUtil.setNeedDetectRunningThread(false);
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withThreadConfig(new ThreadConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        ThreadHelper.teardownRxjava();
        ThreadUtil.setNeedDetectRunningThread(true);
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            Thread thread0 = new Thread(new Runnable() {
                @Override
                public void run() {
                }
            });
            thread0.setName("thread0");
            List<ThreadInfo> threadInfos0 = new ArrayList<>();
            threadInfos0.add(new ThreadInfo(thread0));

            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                }
            });
            thread1.setName("thread1");
            List<ThreadInfo> threadInfos1 = new ArrayList<>();
            threadInfos1.add(new ThreadInfo(thread1));

            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                }
            });
            thread2.setName("thread2");
            List<ThreadInfo> threadInfos2 = new ArrayList<>();
            threadInfos2.add(new ThreadInfo(thread2));


            List<ThreadInfo> threadInfos3 = new ArrayList<>();
            threadInfos3.add(new ThreadInfo(thread0));
            threadInfos3.add(new ThreadInfo(thread1));

            GodEye.instance().<ThreadDump>getModule(GodEye.ModuleName.THREAD).produce(threadInfos0);
            GodEye.instance().<ThreadDump>getModule(GodEye.ModuleName.THREAD).produce(threadInfos1);
            TestObserver<List<ThreadInfo>> testObserver = GodEye.instance().<ThreadDump, List<ThreadInfo>>moduleObservable(GodEye.ModuleName.THREAD).test();
            GodEye.instance().<ThreadDump>getModule(GodEye.ModuleName.THREAD).produce(threadInfos2);
            GodEye.instance().<ThreadDump>getModule(GodEye.ModuleName.THREAD).produce(threadInfos3);
            testObserver.assertValueCount(3).assertValueAt(0, new Predicate<List<ThreadInfo>>() {
                @Override
                public boolean test(List<ThreadInfo> info) throws Exception {
                    Log4Test.d(info);
                    return info.size() == 1 && "thread1".equals(info.get(0).name);
                }
            }).assertValueAt(1, new Predicate<List<ThreadInfo>>() {
                @Override
                public boolean test(List<ThreadInfo> info) throws Exception {
                    Log4Test.d(info);
                    return info.size() == 1 && "thread2".equals(info.get(0).name);

                }
            }).assertValueAt(2, new Predicate<List<ThreadInfo>>() {
                @Override
                public boolean test(List<ThreadInfo> info) throws Exception {
                    Log4Test.d(info);
                    return info.size() == 2 && "thread0".equals(info.get(0).name);
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }

    @Test
    public void work2() {
        ((TestScheduler) ThreadUtil.computationScheduler()).advanceTimeBy(5, TimeUnit.SECONDS);
        try {
            TestObserver<List<ThreadInfo>> subscriber = new TestObserver<>();
            GodEye.instance().<ThreadDump, List<ThreadInfo>>moduleObservable(GodEye.ModuleName.THREAD).subscribe(subscriber);
            subscriber.assertValue(new Predicate<List<ThreadInfo>>() {
                @Override
                public boolean test(List<ThreadInfo> threadInfos) throws Exception {
                    return threadInfos != null && !threadInfos.isEmpty();
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
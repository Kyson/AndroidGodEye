package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.os.Build;
import android.util.ArraySet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import shark.ApplicationLeak;
import shark.LeakTrace;
import shark.LeakTraceObject;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class LeakDetectorTest {

    @Before
    public void setUp() throws Exception {
        GodEye.instance().install(GodEyeConfig.noneConfigBuilder().withLeakConfig(new LeakConfig()).build());
    }

    @After
    public void tearDown() throws Exception {
        GodEye.instance().uninstall();
    }

    @Test
    public void work() {
        try {
            ApplicationLeak applicationLeak0 = new ApplicationLeak(Arrays.asList(new LeakTrace(LeakTrace.GcRootType.JNI_GLOBAL, new ArrayList<>(), new LeakTraceObject(LeakTraceObject.ObjectType.ARRAY, "", new ArraySet<>(), LeakTraceObject.LeakingStatus.LEAKING, ""), 0)));
            ApplicationLeak applicationLeak1 = new ApplicationLeak(Arrays.asList(new LeakTrace(LeakTrace.GcRootType.JNI_GLOBAL, new ArrayList<>(), new LeakTraceObject(LeakTraceObject.ObjectType.ARRAY, "", new ArraySet<>(), LeakTraceObject.LeakingStatus.LEAKING, ""), 1)));
            ApplicationLeak applicationLeak2 = new ApplicationLeak(Arrays.asList(new LeakTrace(LeakTrace.GcRootType.JNI_GLOBAL, new ArrayList<>(), new LeakTraceObject(LeakTraceObject.ObjectType.ARRAY, "", new ArraySet<>(), LeakTraceObject.LeakingStatus.LEAKING, ""), 2)));
            ApplicationLeak applicationLeak3 = new ApplicationLeak(Arrays.asList(new LeakTrace(LeakTrace.GcRootType.JNI_GLOBAL, new ArrayList<>(), new LeakTraceObject(LeakTraceObject.ObjectType.ARRAY, "", new ArraySet<>(), LeakTraceObject.LeakingStatus.LEAKING, ""), 3)));

            LeakInfo leakInfo0 = new LeakInfo(0, 0, applicationLeak0);
            LeakInfo leakInfo1 = new LeakInfo(0, 0, applicationLeak1);
            LeakInfo leakInfo2 = new LeakInfo(0, 0, applicationLeak2);
            LeakInfo leakInfo3 = new LeakInfo(0, 0, applicationLeak3);


            GodEye.instance().<Leak>getModule(GodEye.ModuleName.LEAK_CANARY).produce(leakInfo0);
            GodEye.instance().<Leak>getModule(GodEye.ModuleName.LEAK_CANARY).produce(leakInfo1);
            TestObserver<LeakInfo> testObserver = GodEye.instance().<Leak, LeakInfo>moduleObservable(GodEye.ModuleName.LEAK_CANARY).test();
            GodEye.instance().<Leak>getModule(GodEye.ModuleName.LEAK_CANARY).produce(leakInfo2);
            GodEye.instance().<Leak>getModule(GodEye.ModuleName.LEAK_CANARY).produce(leakInfo3);
            testObserver.assertValueCount(4).assertValueAt(0, new Predicate<LeakInfo>() {
                @Override
                public boolean test(LeakInfo heapAnalysis) throws Exception {
                    return 0 == heapAnalysis.info.getLeakTraces().get(0).getRetainedHeapByteSize();
                }
            }).assertValueAt(1, new Predicate<LeakInfo>() {
                @Override
                public boolean test(LeakInfo heapAnalysis) throws Exception {
                    return 1 == heapAnalysis.info.getLeakTraces().get(0).getRetainedHeapByteSize();
                }
            }).assertValueAt(2, new Predicate<LeakInfo>() {
                @Override
                public boolean test(LeakInfo heapAnalysis) throws Exception {
                    return 2 == heapAnalysis.info.getLeakTraces().get(0).getRetainedHeapByteSize();
                }
            }).assertValueAt(3, new Predicate<LeakInfo>() {
                @Override
                public boolean test(LeakInfo heapAnalysis) throws Exception {
                    return 3 == heapAnalysis.info.getLeakTraces().get(0).getRetainedHeapByteSize();
                }
            });
        } catch (UninstallException e) {
            Assert.fail();
        }
    }
}
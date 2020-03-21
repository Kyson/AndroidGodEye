package cn.hikyson.godeye.core.internal.modules.leakdetector;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import leakcanary.AppWatcher;
import leakcanary.LeakCanary;
import leakcanary.OnHeapAnalyzedListener;
import shark.HeapAnalysis;
import shark.HeapAnalysisFailure;
import shark.HeapAnalysisSuccess;
import shark.LeakTrace;

public class Leak extends ProduceableSubject<LeakQueue.LeakMemoryInfo> implements Install<LeakConfig> {
    public static final String LEAK_HANDLER = "godeye-leak";
    private boolean mInstalled;
    private LeakConfig mConfig;

    class LeakInfoItem{
       public String signature;
        public String shortDescription;
        public List<LeakTrace> leakTraces;
        public int totalRetainedHeapByteSize;
    }

    @Override
    public boolean install(LeakConfig config) {
        if (mInstalled) {
            L.d("Leak already installed, ignore.");
            return true;
        }
        mConfig = config;
        ThreadUtil.createIfNotExistHandler(LEAK_HANDLER);
        LeakCanary.setConfig(new LeakCanary.Config().newBuilder().requestWriteExternalStoragePermission(false).dumpHeap(config.debug()).onHeapAnalyzedListener(new OnHeapAnalyzedListener() {
            @Override
            public void onHeapAnalyzed(@NotNull HeapAnalysis heapAnalysis) {
                if (heapAnalysis instanceof HeapAnalysisSuccess) {
                    HeapAnalysisSuccess heapAnalysisSuccess = (HeapAnalysisSuccess) heapAnalysis;
                    long createTimeMillis = heapAnalysisSuccess.getCreatedAtTimeMillis();
                    long durationMillis = heapAnalysisSuccess.getAnalysisDurationMillis();
                    Map<String, String> metaData = heapAnalysisSuccess.getMetadata();
                    Iterator<shark.Leak> iterator = heapAnalysisSuccess.getAllLeaks().iterator();
                    while (iterator.hasNext()) {
                        shark.Leak leak = iterator.next();
                        leak.getLeakTraces().get(0).getLeakingObject().
                    }


                } else if (heapAnalysis instanceof HeapAnalysisFailure) {

                } else {
                    throw new IllegalStateException("No such HeapAnalysis type of:" + heapAnalysis.getClass().getName());
                }
                List<shark.Leak> leaks =
                        ((HeapAnalysisSuccess) heapAnalysis)..getAllLeaks().iterator().next().
                        produce();
            }
        }).build());
        AppWatcher.setConfig(new AppWatcher.Config().newBuilder().enabled(true).build());
        AppWatcher.INSTANCE.manualInstall(GodEye.instance().getApplication());
        mInstalled = true;
        L.d("Leak installed.");
        return true;
    }

    @Override
    public void uninstall() {
        if (!mInstalled) {
            L.d("Leak already uninstalled, ignore.");
            return;
        }
        AppWatcher.setConfig(new AppWatcher.Config().newBuilder().enabled(false).build());
        ThreadUtil.destoryHandler(LEAK_HANDLER);
        mConfig = null;
        mInstalled = false;
        L.d("Leak uninstalled.");
    }

    @Override
    public boolean isInstalled() {
        return this.mInstalled;
    }

    @Override
    public LeakConfig config() {
        return mConfig;
    }

    @Override
    protected Subject<LeakQueue.LeakMemoryInfo> createSubject() {
        return ReplaySubject.create();
    }
}

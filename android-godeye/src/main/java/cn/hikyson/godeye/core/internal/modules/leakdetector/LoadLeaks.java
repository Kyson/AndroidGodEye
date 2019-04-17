package cn.hikyson.godeye.core.internal.modules.leakdetector;

import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.AnalyzedHeap;
import com.squareup.leakcanary.HeapDump;
import com.squareup.leakcanary.LeakDirectoryProvider;
import com.squareup.leakcanary.LeakTraceElement;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;

import static com.squareup.leakcanary.internal.LeakCanaryInternals.newSingleThreadExecutor;

/**
 * Created by kysonchao on 2017/9/30.
 */
public class LoadLeaks implements Runnable {
    public interface OnLeakCallback {
        public void onLeak(List<String> leakStack);

        public void onLeakNull(String reason);
    }

    private static final Executor backgroundExecutor = newSingleThreadExecutor("LoadLeaks");

    private final LeakDirectoryProvider leakDirectoryProvider;
    private final String mReferenceKey;
    private OnLeakCallback mOnLeakCallback;

    public LoadLeaks(LeakDirectoryProvider leakDirectoryProvider, String referenceKey, OnLeakCallback onLeakCallback) {
        this.leakDirectoryProvider = leakDirectoryProvider;
        this.mReferenceKey = referenceKey;
        this.mOnLeakCallback = onLeakCallback;
    }

    static class Leak {
        final HeapDump heapDump;
        final AnalysisResult result;
        final File resultFile;

        Leak(HeapDump heapDump, AnalysisResult result, File resultFile) {
            this.heapDump = heapDump;
            this.result = result;
            this.resultFile = resultFile;
        }
    }

    public void load() {
        backgroundExecutor.execute(this);
    }

    @Override
    public void run() {
        final List<Leak> leaks = new ArrayList<>();
        List<File> files = leakDirectoryProvider.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".result");
            }
        });
        for (File resultFile : files) {
            final AnalyzedHeap analyzedHeap = AnalyzedHeap.load(resultFile);
            if (analyzedHeap != null) {
                leaks.add(new Leak(analyzedHeap.heapDump, analyzedHeap.result, resultFile));
            }
        }
        Collections.sort(leaks, new Comparator<Leak>() {
            @Override
            public int compare(Leak lhs, Leak rhs) {
                return Long.valueOf(rhs.resultFile.lastModified())
                        .compareTo(lhs.resultFile.lastModified());
            }
        });
        if (leaks.isEmpty()) {
            mOnLeakCallback.onLeakNull("leaks file is empty");
            return;
        }
        final Leak visibleLeak = getVisibleLeak(leaks, mReferenceKey);
        if (visibleLeak == null) {
            mOnLeakCallback.onLeakNull("visibleLeak is null");
            return;
        }
        AnalysisResult result = visibleLeak.result;
        if (result.failure != null) {
            mOnLeakCallback.onLeakNull("leak analysis failed");
            return;
        }
        List<LeakTraceElement> elements = new ArrayList<>(result.leakTrace.elements);
        if (elements.isEmpty()) {
            mOnLeakCallback.onLeakNull("leak elements stack is null or empty");
            return;
        }
        ArrayList<String> elementStack = new ArrayList<String>();
        for (LeakTraceElement leakTraceElement : elements) {
            elementStack.add(String.valueOf(leakTraceElement));
        }
        mOnLeakCallback.onLeak(elementStack);
    }

    private Leak getVisibleLeak(List<Leak> leaks, String leakRefKey) {
        if (leaks == null) {
            return null;
        }
        for (Leak leak : leaks) {
            if (leak.heapDump.referenceKey.equals(leakRefKey)) {
                return leak;
            }
        }
        return null;
    }
}

package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.squareup.leakcanary.AbstractAnalysisResultService;
import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.AnalyzerProgressListener;
import com.squareup.leakcanary.HeapAnalyzer;
import com.squareup.leakcanary.HeapDump;
import com.squareup.leakcanary.internal.ForegroundService;

import static com.squareup.leakcanary.internal.LeakCanaryInternals.setEnabledBlocking;


public class GodEyeHeapAnalyzerService extends ForegroundService implements AnalyzerProgressListener {

    private static final String LISTENER_CLASS_EXTRA = "listener_class_extra";
    private static final String HEAPDUMP_EXTRA = "heapdump_extra";

    private String referenceKey;

    public GodEyeHeapAnalyzerService() {
        super(GodEyeHeapAnalyzerService.class.getSimpleName(), 0);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void showForegroundNotification(int max, int progress, boolean indeterminate, String contentText) {
    }

    public static void runAnalysis(Context context, HeapDump heapDump,
                                   Class<? extends AbstractAnalysisResultService> listenerServiceClass) {
        setEnabledBlocking(context, GodEyeHeapAnalyzerService.class, true);
        setEnabledBlocking(context, listenerServiceClass, true);
        Intent intent = new Intent(context, GodEyeHeapAnalyzerService.class);
        intent.putExtra(LISTENER_CLASS_EXTRA, listenerServiceClass.getName());
        intent.putExtra(HEAPDUMP_EXTRA, heapDump);
        ContextCompat.startForegroundService(context, intent);
    }

    @Override
    protected void onHandleIntentInForeground(@Nullable Intent intent) {
        if (intent == null) {
            GodEyeCanaryLog.d("HeapAnalyzerService received a null intent, ignoring.");
            return;
        }
        GodEyeCanaryLog.d("开始分析dump");
        String listenerClassName = intent.getStringExtra(LISTENER_CLASS_EXTRA);
        HeapDump heapDump = (HeapDump) intent.getSerializableExtra(HEAPDUMP_EXTRA);
        referenceKey = heapDump.referenceKey;
        GodEyeCanaryLog.d("listenerClassName:" + listenerClassName);
        GodEyeCanaryLog.d("referenceKey:" + heapDump.referenceKey);
        GodEyeCanaryLog.d("heapDumpFile:" + heapDump.heapDumpFile.getAbsolutePath());
        HeapAnalyzer heapAnalyzer =
                new HeapAnalyzer(heapDump.excludedRefs, this, heapDump.reachabilityInspectorClasses);
        GodEyeCanaryLog.d("checkForLeak...");
        AnalysisResult result = heapAnalyzer.checkForLeak(heapDump.heapDumpFile, heapDump.referenceKey,
                heapDump.computeRetainedHeapSize);
        GodEyeCanaryLog.d("sendResultToListener...");
        AbstractAnalysisResultService.sendResultToListener(this, listenerClassName, heapDump, result);
        GodEyeCanaryLog.d("分析dump完成");
    }

    @Override
    public void onProgressUpdate(@NonNull Step step) {
        OutputLeakService.sendOutputBroadcastProgress(this, referenceKey, step.name());
        GodEyeCanaryLog.d(step.toString());
    }
}

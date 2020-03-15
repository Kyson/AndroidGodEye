package cn.hikyson.godeye.core.internal.modules.leakdetector.debug;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.AnalyzerProgressListener;
import com.squareup.leakcanary.HeapAnalyzer;
import com.squareup.leakcanary.HeapDump;
import com.squareup.leakcanary.LeakTraceElement;

import java.util.ArrayList;

import cn.hikyson.godeye.core.helper.Notifier;
import cn.hikyson.godeye.core.utils.L;

import static android.text.format.Formatter.formatShortFileSize;
import static com.squareup.leakcanary.LeakCanary.leakInfo;
import static com.squareup.leakcanary.internal.LeakCanaryInternals.classSimpleName;
import static com.squareup.leakcanary.internal.LeakCanaryInternals.setEnabledBlocking;


public class DebugHeapAnalyzerService extends IntentService implements AnalyzerProgressListener {

    private static final String HEAPDUMP_EXTRA = "heapdump_extra";

    private static final String SHOW_NOTIFICATION = "show_notification";

    public static final String OUTPUT_BOARDCAST_ACTION_START = "com.ctrip.ibu.leakcanary.output.start";
    public static final String OUTPUT_BOARDCAST_ACTION_DONE = "com.ctrip.ibu.leakcanary.output.done";
    public static final String OUTPUT_BOARDCAST_ACTION_FAILURE = "com.ctrip.ibu.leakcanary.output.failure";
    public static final String OUTPUT_BOARDCAST_ACTION_PROGRESS = "com.ctrip.ibu.leakcanary.output.progress";

    private String referenceKey;
    private String leakRefInfo;

    public DebugHeapAnalyzerService() {
        super("LeakMemory Analyzer Service");
    }

    public static void runAnalysis(Context context, HeapDump heapDump, boolean showNotification) {
        setEnabledBlocking(context, DebugHeapAnalyzerService.class, true);
        Intent intent = new Intent(context, DebugHeapAnalyzerService.class);
        intent.putExtra(HEAPDUMP_EXTRA, heapDump);
        intent.putExtra(SHOW_NOTIFICATION, showNotification);
        ContextCompat.startForegroundService(context, intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            L.d("LeakDetector HeapAnalyzerService received a null intent, ignoring.");
            return;
        }
        boolean showNotification = intent.getBooleanExtra(SHOW_NOTIFICATION, false);
        int id = 0;
        if (showNotification) {
            id = showNotification();
        }
        HeapDump heapDump = (HeapDump) intent.getSerializableExtra(HEAPDUMP_EXTRA);
        referenceKey = heapDump.referenceKey;
        leakRefInfo = heapDump.referenceName;
        L.d("LeakDetector Dump analyzing start, referenceKey:" + heapDump.referenceKey + ", leakRefInfo:" + heapDump.referenceName + ", heapDumpFile:" + heapDump.heapDumpFile.getAbsolutePath());
        HeapAnalyzer heapAnalyzer =
                new HeapAnalyzer(heapDump.excludedRefs, this, heapDump.reachabilityInspectorClasses);
        //分析
        AnalysisResult result = heapAnalyzer.checkForLeak(heapDump.heapDumpFile, heapDump.referenceKey,
                heapDump.computeRetainedHeapSize);
        onHeapAnalyzed(this, heapDump, result);
        //删除dump文件
        heapDump.heapDumpFile.delete();
        L.d("LeakDetector dump file deleted.");
        if (id > 0) {
            Notifier.cancelNotice(this, id);
        }
    }

    private int showNotification() {
        Notification notification = Notifier.create(this, new Notifier.Config("MemoryLeakAnalyzerService", "Analyzing..."));
        int id = Notifier.createNoticeId();
        startForeground(id, notification);
        return id;
    }

    @Override
    public void onProgressUpdate(@NonNull Step step) {
        sendOutputBroadcastProgress(this, referenceKey, leakRefInfo, step);
    }

    private void onHeapAnalyzed(Context context, @NonNull HeapDump heapDump, AnalysisResult result) {
        String leakInfo = leakInfo(this, heapDump, result, true);
        L.d("LeakDetector Dump analyzing done, leakInfo:%s", leakInfo);
        if (!result.leakFound) {//没有泄漏
            L.d("LeakDetector No leak found.");
            return;
        }
        if (result.failure != null || result.leakTrace == null) {
            sendOutputBroadcastFailure(context, heapDump.referenceKey, heapDump.referenceName, result, getSummary(result));
        } else {
            ArrayList<LeakTraceElement> elements = new ArrayList<>(result.leakTrace.elements);
            ArrayList<String> elementStack = new ArrayList<>();
            for (LeakTraceElement leakTraceElement : elements) {
                elementStack.add(String.valueOf(leakTraceElement));
            }
            sendOutputBroadcastDone(context, heapDump.referenceKey, heapDump.referenceName, result, getSummary(result), elementStack);
        }
    }

    private String getSummary(AnalysisResult result) {
        String summary;
        if (result.failure != null) {
            summary = "Leak analysis failed";
        } else {
            String className = classSimpleName(result.className);
            if (result.leakFound) {
                if (result.retainedHeapSize == AnalysisResult.RETAINED_HEAP_SKIPPED) {
                    if (result.excludedLeak) {
                        summary = String.format("[Excluded] %1$s leaked", className);
                    } else {
                        summary = String.format("%1$s leaked", className);
                    }
                } else {
                    String size = formatShortFileSize(this, result.retainedHeapSize);
                    if (result.excludedLeak) {
                        summary =
                                String.format("[Excluded] %1$s leaked %2$s", className, size);
                    } else {
                        summary =
                                String.format("[Excluded] %1$s leaked %2$s", className, size);
                    }
                }
            } else {
                summary = String.format("%1$s was never GCed but no leak found", className);
            }
        }
        return summary;
    }

    private void sendOutputBroadcastProgress(Context context, String referenceKey, String leakRefInfo, AnalyzerProgressListener.Step step) {
        if (step == AnalyzerProgressListener.Step.READING_HEAP_DUMP_FILE) {
            Intent intent = new Intent(context, LeakOutputReceiver.class);
            intent.putExtra("referenceKey", referenceKey);
            intent.setAction(OUTPUT_BOARDCAST_ACTION_START);
            context.sendBroadcast(intent);
        }
        Intent intent = new Intent(context, LeakOutputReceiver.class);
        intent.putExtra("referenceKey", referenceKey);
        intent.putExtra("leakRefInfo", leakRefInfo);
        intent.putExtra("progress", step.name());
        intent.setAction(OUTPUT_BOARDCAST_ACTION_PROGRESS);
        context.sendBroadcast(intent);
    }

    private void sendOutputBroadcastDone(Context context, String referenceKey, String leakRefInfo, AnalysisResult result, String summary,
                                         ArrayList<String> elementStack) {
        Intent intent = new Intent(context, LeakOutputReceiver.class);
        intent.setAction(OUTPUT_BOARDCAST_ACTION_DONE);
        intent.putExtra("referenceKey", referenceKey);
        intent.putExtra("leakRefInfo", leakRefInfo);
        intent.putExtra("result", new AnalysisResultWrapper(result));
        intent.putExtra("summary", summary);
        intent.putStringArrayListExtra("elementStack", elementStack);
        context.sendBroadcast(intent);
    }

    private void sendOutputBroadcastFailure(Context context, String referenceKey, String leakRefInfo, AnalysisResult result, String summary) {
        Intent intent = new Intent(context, LeakOutputReceiver.class);
        intent.setAction(OUTPUT_BOARDCAST_ACTION_FAILURE);
        intent.putExtra("referenceKey", referenceKey);
        intent.putExtra("leakRefInfo", leakRefInfo);
        intent.putExtra("result", new AnalysisResultWrapper(result));
        intent.putExtra("summary", summary);
        context.sendBroadcast(intent);
    }
}

package cn.hikyson.godeye.core.internal.modules.leakdetector.debug;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.AnalyzerProgressListener;
import com.squareup.leakcanary.HeapAnalyzer;
import com.squareup.leakcanary.HeapDump;
import com.squareup.leakcanary.LeakTraceElement;
import com.squareup.leakcanary.internal.ForegroundService;

import java.util.ArrayList;

import cn.hikyson.godeye.core.internal.modules.leakdetector.GodEyeCanaryLog;

import static android.text.format.Formatter.formatShortFileSize;
import static com.squareup.leakcanary.LeakCanary.leakInfo;
import static com.squareup.leakcanary.internal.LeakCanaryInternals.classSimpleName;
import static com.squareup.leakcanary.internal.LeakCanaryInternals.setEnabledBlocking;


public class GodEyeHeapAnalyzerService extends ForegroundService implements AnalyzerProgressListener {

    private static final String HEAPDUMP_EXTRA = "heapdump_extra";

    public static final String OUTPUT_BOARDCAST_ACTION_START = "com.ctrip.ibu.leakcanary.output.start";
    public static final String OUTPUT_BOARDCAST_ACTION_DONE = "com.ctrip.ibu.leakcanary.output.done";
    public static final String OUTPUT_BOARDCAST_ACTION_FAILURE = "com.ctrip.ibu.leakcanary.output.failure";
    public static final String OUTPUT_BOARDCAST_ACTION_PROGRESS = "com.ctrip.ibu.leakcanary.output.progress";

    private String referenceKey;
    private String referenceName;

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

    public static void runAnalysis(Context context, HeapDump heapDump) {
        setEnabledBlocking(context, GodEyeHeapAnalyzerService.class, true);
        Intent intent = new Intent(context, GodEyeHeapAnalyzerService.class);
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
        HeapDump heapDump = (HeapDump) intent.getSerializableExtra(HEAPDUMP_EXTRA);
        referenceKey = heapDump.referenceKey;
        referenceName = heapDump.referenceName;
        GodEyeCanaryLog.d("referenceKey:" + heapDump.referenceKey);
        GodEyeCanaryLog.d("heapDumpFile:" + heapDump.heapDumpFile.getAbsolutePath());
        HeapAnalyzer heapAnalyzer =
                new HeapAnalyzer(heapDump.excludedRefs, this, heapDump.reachabilityInspectorClasses);
        GodEyeCanaryLog.d("checkForLeak...");
        //分析
        AnalysisResult result = heapAnalyzer.checkForLeak(heapDump.heapDumpFile, heapDump.referenceKey,
                heapDump.computeRetainedHeapSize);
        GodEyeCanaryLog.d("分析dump完成");
        onHeapAnalyzed(this, heapDump, result);
        //删除dump文件
        heapDump.heapDumpFile.delete();
        GodEyeCanaryLog.d("dump文件删除");
    }

    @Override
    public void onProgressUpdate(@NonNull Step step) {
        sendOutputBroadcastProgress(this, referenceKey, referenceName, step);
        GodEyeCanaryLog.d(step.toString());
    }


    private void onHeapAnalyzed(Context context, @NonNull HeapDump heapDump, AnalysisResult result) {
        String leakInfo = leakInfo(this, heapDump, result, true);
        GodEyeCanaryLog.d("%s", leakInfo);
        if (!result.leakFound) {//没有泄漏
            GodEyeCanaryLog.d("没有泄漏");
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
        GodEyeCanaryLog.d("开始生成摘要");
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
        GodEyeCanaryLog.d("摘要信息获取完成: %s", summary);
        GodEyeCanaryLog.d("输出信息，done!");
        return summary;
    }

    private void sendOutputBroadcastProgress(Context context, String referenceKey, String referenceName, AnalyzerProgressListener.Step step) {
        if (step == AnalyzerProgressListener.Step.READING_HEAP_DUMP_FILE) {
            Intent intent = new Intent(context, LeakOutputReceiver.class);
            intent.putExtra("referenceKey", referenceKey);
            intent.setAction(OUTPUT_BOARDCAST_ACTION_START);
            context.sendBroadcast(intent);
        }
        Intent intent = new Intent(context, LeakOutputReceiver.class);
        intent.putExtra("referenceKey", referenceKey);
        intent.putExtra("referenceName", referenceName);
        intent.putExtra("progress", step.name());
        intent.setAction(OUTPUT_BOARDCAST_ACTION_PROGRESS);
        context.sendBroadcast(intent);
    }

    private void sendOutputBroadcastDone(Context context, String referenceKey, String referenceName, AnalysisResult result, String summary,
                                         ArrayList<String> elementStack) {
        Intent intent = new Intent(context, LeakOutputReceiver.class);
        intent.setAction(OUTPUT_BOARDCAST_ACTION_DONE);
        intent.putExtra("referenceKey", referenceKey);
        intent.putExtra("referenceName", referenceName);
        intent.putExtra("result", result);
        intent.putExtra("summary", summary);
        intent.putStringArrayListExtra("elementStack", elementStack);
        context.sendBroadcast(intent);
    }

    private void sendOutputBroadcastFailure(Context context, String referenceKey, String referenceName, AnalysisResult result, String summary) {
        Intent intent = new Intent(context, LeakOutputReceiver.class);
        intent.setAction(OUTPUT_BOARDCAST_ACTION_DONE);
        intent.putExtra("referenceKey", referenceKey);
        intent.putExtra("referenceName", referenceName);
        intent.putExtra("result", result);
        intent.putExtra("summary", summary);
        context.sendBroadcast(intent);
    }
}

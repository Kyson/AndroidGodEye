package cn.hikyson.godeye.core.internal.modules.leakdetector.debug;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.AnalyzerProgressListener;
import com.squareup.leakcanary.HeapAnalyzer;
import com.squareup.leakcanary.HeapDump;
import com.squareup.leakcanary.LeakTraceElement;

import java.util.ArrayList;

import cn.hikyson.godeye.core.R;
import cn.hikyson.godeye.core.helper.Notifier;
import cn.hikyson.godeye.core.internal.modules.leakdetector.GodEyeCanaryLog;

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
    private String referenceName;

    public DebugHeapAnalyzerService() {
        super("内存泄漏分析服务");
    }

    public static void runAnalysis(Context context, HeapDump heapDump, boolean showNotification) {
        setEnabledBlocking(context, DebugHeapAnalyzerService.class, true);
        Intent intent = new Intent(context, DebugHeapAnalyzerService.class);
        intent.putExtra(HEAPDUMP_EXTRA, heapDump);
        intent.putExtra(SHOW_NOTIFICATION, showNotification);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            GodEyeCanaryLog.d("HeapAnalyzerService received a null intent, ignoring.");
            return;
        }
        boolean showNotification = intent.getBooleanExtra(SHOW_NOTIFICATION, false);
        int id = Notifier.getId();
        if (showNotification) {
            showNotification(id);
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
        if (showNotification) {
            cancelNotification(id);
        }
    }

    private void showNotification(int id) {
        Notification.Builder builder;
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("AndroidGodEye", "AndroidGodEye", NotificationManager.IMPORTANCE_MIN);
            channel.setDescription("AndroidGodEye");
            notificationManager.createNotificationChannel(channel);
            builder = new Notification.Builder(this, "AndroidGodEye");
        } else {
            builder = new Notification.Builder(this);
        }
        builder.setSmallIcon(R.drawable.ic_remove_red_eye)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("内存泄漏分析服务").setContentText("AndroidGodEye").build();
        startForeground(id, builder.build());
    }

    private void cancelNotification(int id) {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
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
        intent.putExtra("result", new AnalysisResultWrapper(result));
        intent.putExtra("summary", summary);
        intent.putStringArrayListExtra("elementStack", elementStack);
        context.sendBroadcast(intent);
    }

    private void sendOutputBroadcastFailure(Context context, String referenceKey, String referenceName, AnalysisResult result, String summary) {
        Intent intent = new Intent(context, LeakOutputReceiver.class);
        intent.setAction(OUTPUT_BOARDCAST_ACTION_FAILURE);
        intent.putExtra("referenceKey", referenceKey);
        intent.putExtra("referenceName", referenceName);
        intent.putExtra("result", new AnalysisResultWrapper(result));
        intent.putExtra("summary", summary);
        context.sendBroadcast(intent);
    }
}

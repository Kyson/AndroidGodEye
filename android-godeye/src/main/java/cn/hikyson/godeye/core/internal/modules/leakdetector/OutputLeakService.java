/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.AbstractAnalysisResultService;
import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.AnalyzedHeap;
import com.squareup.leakcanary.CanaryLog;
import com.squareup.leakcanary.HeapDump;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.text.format.Formatter.formatShortFileSize;
import static com.squareup.leakcanary.LeakCanary.leakInfo;
import static com.squareup.leakcanary.internal.LeakCanaryInternals.classSimpleName;

/**
 * 内存泄漏输出服务
 */
public class OutputLeakService extends AbstractAnalysisResultService {

    public static final String OUTPUT_BOARDCAST_ACTION_START = "com.ctrip.ibu.leakcanary.output.start";
    public static final String OUTPUT_BOARDCAST_ACTION_RETRY = "com.ctrip.ibu.leakcanary.output.retry";
    public static final String OUTPUT_BOARDCAST_ACTION_DONE = "com.ctrip.ibu.leakcanary.output.done";
    public static final String OUTPUT_BOARDCAST_ACTION_PROGRESS = "com.ctrip.ibu.leakcanary.output.progress";

    public OutputLeakService() {
        super();
    }

    @Override
    protected void onHeapAnalyzed(@NonNull AnalyzedHeap analyzedHeap) {
        super.onHeapAnalyzed(analyzedHeap);
        HeapDump heapDump = analyzedHeap.heapDump;
        AnalysisResult result = analyzedHeap.result;

        String leakInfo = leakInfo(this, heapDump, result, true);
        GodEyeCanaryLog.d("%s", leakInfo);
        boolean shouldSaveResult = result.leakFound || result.failure != null;
        if (!shouldSaveResult) {//没有泄漏
            GodEyeCanaryLog.d("没有泄漏");
            return;
        }

        heapDump = renameHeapdump(heapDump);
        boolean resultSaved = saveResult(heapDump, result);
        String summary;
        if (resultSaved) {
            GodEyeCanaryLog.d("开始生成摘要");
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
            GodEyeCanaryLog.d("摘要信息获取完成");
            GodEyeCanaryLog.d("输出信息，done!");
        } else {
            summary = "LeakCanary was unable to save the analysis result.";
        }

        sendOutputBroadcastDone(this, heapDump.referenceKey, heapDump, result, summary, leakInfo);

    }

    private boolean saveResult(HeapDump heapDump, AnalysisResult result) {
        File resultFile = AnalyzedHeap.save(heapDump, result);
        return resultFile != null;
    }

    private HeapDump renameHeapdump(HeapDump heapDump) {
        String fileName =
                new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS'.hprof'", Locale.US).format(new Date());

        File newFile = new File(heapDump.heapDumpFile.getParent(), fileName);
        boolean renamed = heapDump.heapDumpFile.renameTo(newFile);
        if (!renamed) {
            CanaryLog.d("Could not rename heap dump file %s to %s", heapDump.heapDumpFile.getPath(),
                    newFile.getPath());
        }
        return heapDump.buildUpon().heapDumpFile(newFile).build();
    }

    public static void sendOutputBroadcastStart(Context context, String referenceKey) {
        Intent intent = new Intent(context, LeakOutputReceiver.class);
        intent.putExtra("referenceKey", referenceKey);
        intent.setAction(OUTPUT_BOARDCAST_ACTION_START);
        context.sendBroadcast(intent);
    }

    public static void sendOutputBroadcastProgress(Context context, String referenceKey, String progress) {
        Intent intent = new Intent(context, LeakOutputReceiver.class);
        intent.putExtra("referenceKey", referenceKey);
        intent.putExtra("progress", progress);
        intent.setAction(OUTPUT_BOARDCAST_ACTION_PROGRESS);
        context.sendBroadcast(intent);
    }

    public static void sendOutputBroadcastRetry(Context context) {
        Intent intent = new Intent(context, LeakOutputReceiver.class);
        intent.setAction(OUTPUT_BOARDCAST_ACTION_RETRY);
        context.sendBroadcast(intent);
    }

    public static void sendOutputBroadcastDone(Context context, String referenceKey, HeapDump heapDump, AnalysisResult result, String summary, String leakInfo) {
        Intent intent = new Intent(context, LeakOutputReceiver.class);
        intent.setAction(OUTPUT_BOARDCAST_ACTION_DONE);
        intent.putExtra("referenceKey", referenceKey);
        intent.putExtra("heapDump", heapDump);
        intent.putExtra("result", result);
        intent.putExtra("summary", summary);
        intent.putExtra("leakInfo", leakInfo);
        context.sendBroadcast(intent);
    }
}

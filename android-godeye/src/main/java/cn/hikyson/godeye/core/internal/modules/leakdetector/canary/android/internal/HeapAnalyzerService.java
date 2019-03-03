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
package cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.internal;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.squareup.leakcanary.HeapDump;

import cn.hikyson.godeye.core.R;
import cn.hikyson.godeye.core.helper.Notifier;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.analyzer.leakcanary.AnalysisResult;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.analyzer.leakcanary.HeapAnalyzer;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.AbstractAnalysisResultService;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.CanaryLog;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.ForegroundService;

/**
 * This service runs in a separate process to avoid slowing down the app process or making it run
 * out of memory.
 */
public final class HeapAnalyzerService extends ForegroundService {

    private static final String LISTENER_CLASS_EXTRA = "listener_class_extra";
    private static final String HEAPDUMP_EXTRA = "heapdump_extra";

    public static void runAnalysis(Context context, HeapDump heapDump,
                                   Class<? extends AbstractAnalysisResultService> listenerServiceClass) {
        Intent intent = new Intent(context, HeapAnalyzerService.class);
        intent.putExtra(LISTENER_CLASS_EXTRA, listenerServiceClass.getName());
        intent.putExtra(HEAPDUMP_EXTRA, heapDump);
        ContextCompat.startForegroundService(context, intent);
    }

    public HeapAnalyzerService() {
        super("内存泄漏分析服务");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            CanaryLog.d("HeapAnalyzerService received a null intent, ignoring.");
            return;
        }
        CanaryLog.d("开始分析dump");
        String listenerClassName = intent.getStringExtra(LISTENER_CLASS_EXTRA);
        HeapDump heapDump = (HeapDump) intent.getSerializableExtra(HEAPDUMP_EXTRA);
        CanaryLog.d("listenerClassName:" + listenerClassName);
        CanaryLog.d("referenceKey:" + heapDump.referenceKey);
        CanaryLog.d("heapDumpFile:" + heapDump.heapDumpFile.getAbsolutePath());
        HeapAnalyzer heapAnalyzer = new HeapAnalyzer(this, heapDump.excludedRefs);
        CanaryLog.d("checkForLeak...");
        AnalysisResult result = heapAnalyzer.checkForLeak(heapDump.heapDumpFile, heapDump.referenceKey);
        CanaryLog.d("分析dump完成");
        AbstractAnalysisResultService.sendResultToListener(this, listenerClassName, heapDump, result);
    }
}

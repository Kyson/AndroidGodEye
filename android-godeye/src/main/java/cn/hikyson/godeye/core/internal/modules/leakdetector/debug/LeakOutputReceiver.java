package cn.hikyson.godeye.core.internal.modules.leakdetector.debug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;

import com.squareup.leakcanary.AnalysisResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executor;

import cn.hikyson.godeye.core.internal.modules.leakdetector.GodEyeCanaryLog;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakDetector;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakQueue;
import cn.hikyson.godeye.core.utils.ThreadUtil;

public class LeakOutputReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, final Intent intent) {
        Executor executor = LeakDetector.instance().getsSingleForLeakExecutor();
        if (executor != null) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    if (DebugHeapAnalyzerService.OUTPUT_BOARDCAST_ACTION_START.equals(intent.getAction())) {
                        onLeakDumpStart(intent);
                    } else if (DebugHeapAnalyzerService.OUTPUT_BOARDCAST_ACTION_PROGRESS.equals(intent.getAction())) {
                        onLeakDumpProgress(intent);
                    } else if (DebugHeapAnalyzerService.OUTPUT_BOARDCAST_ACTION_DONE.equals(intent.getAction())) {
                        onLeakDumpDone(intent);
                    } else if (DebugHeapAnalyzerService.OUTPUT_BOARDCAST_ACTION_FAILURE.equals(intent.getAction())) {
                        onLeakDumpFailure(intent);
                    }
                }
            });
        }
    }

    private void onLeakDumpStart(Intent intent) {
        String referenceKey = intent.getStringExtra("referenceKey");
        String referenceName = intent.getStringExtra("referenceName");
        GodEyeCanaryLog.d("onLeakDumpStart:" + referenceKey);
        Map<String, Object> map = new ArrayMap<>();
        map.put(LeakQueue.LeakMemoryInfo.Fields.LEAK_TIME, LeakQueue.LeakMemoryInfo.DF.format(new Date(System.currentTimeMillis())));
        map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS_SUMMARY, "Leak detected");
        map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS, LeakQueue.LeakMemoryInfo.Status.STATUS_START);
        LeakQueue.instance().createOrUpdateIfExsist(referenceKey, map);
        LeakDetector.instance().produce(LeakQueue.instance().generateLeakMemoryInfo(referenceKey, referenceName));
    }

    private void onLeakDumpProgress(Intent intent) {
        String referenceKey = intent.getStringExtra("referenceKey");
        String referenceName = intent.getStringExtra("referenceName");
        String progress = intent.getStringExtra("progress");
        GodEyeCanaryLog.d("onLeakDumpProgress:" + referenceKey + " , progress:" + progress);
        Map<String, Object> map = new ArrayMap<>();
        map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS_SUMMARY, progress);
        map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS, LeakQueue.LeakMemoryInfo.Status.STATUS_PROGRESS);
        LeakQueue.instance().createOrUpdateIfExsist(referenceKey, map);
        LeakDetector.instance().produce(LeakQueue.instance().generateLeakMemoryInfo(referenceKey, referenceName));
    }


    private void onLeakDumpDone(Intent intent) {
        String referenceKey = intent.getStringExtra("referenceKey");
        String referenceName = intent.getStringExtra("referenceName");
        AnalysisResultWrapper analysisResult = (AnalysisResultWrapper) intent.getSerializableExtra("result");
        String summary = intent.getStringExtra("summary");
        ArrayList<String> elementStack = intent.getStringArrayListExtra("elementStack");
        GodEyeCanaryLog.d("onLeakDumpDone:" + referenceKey + " , leak:" + analysisResult.className + " , summary:" + summary);
        Map<String, Object> map = new ArrayMap<>();
        map.put(LeakQueue.LeakMemoryInfo.Fields.LEAK_OBJ_NAME, analysisResult.className + (analysisResult.excludedLeak ? "[Excluded]" : ""));
        map.put(LeakQueue.LeakMemoryInfo.Fields.PATH_TO_ROOT, elementStack);
        //因为计算对象引用的所有对象大小很耗时导致分析失败，所以分析跳过了这步，这里永远是0
        map.put(LeakQueue.LeakMemoryInfo.Fields.LEAK_MEMORY_BYTES, analysisResult.retainedHeapSize);
        map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS, LeakQueue.LeakMemoryInfo.Status.STATUS_DONE);
        map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS_SUMMARY, "done");
        LeakQueue.instance().createOrUpdateIfExsist(referenceKey, map);
        LeakDetector.instance().produce(LeakQueue.instance().generateLeakMemoryInfo(referenceKey, referenceName));
    }

    private void onLeakDumpFailure(Intent intent) {
        String referenceKey = intent.getStringExtra("referenceKey");
        String referenceName = intent.getStringExtra("referenceName");
        AnalysisResultWrapper analysisResult = (AnalysisResultWrapper) intent.getSerializableExtra("result");
        String summary = intent.getStringExtra("summary");
        GodEyeCanaryLog.d("onLeakDumpFailure:" + referenceKey + " , leak:" + analysisResult.className + " , summary:" + summary);
        Map<String, Object> map = new ArrayMap<>();
        map.put(LeakQueue.LeakMemoryInfo.Fields.LEAK_OBJ_NAME, analysisResult.className + (analysisResult.excludedLeak ? "[Excluded]" : ""));
        //因为计算对象引用的所有对象大小很耗时导致分析失败，所以分析跳过了这步，这里永远是0
        map.put(LeakQueue.LeakMemoryInfo.Fields.LEAK_MEMORY_BYTES, analysisResult.retainedHeapSize);
        map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS, LeakQueue.LeakMemoryInfo.Status.STATUS_DONE);
        map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS_SUMMARY, "Leak null.");
        LeakQueue.instance().createOrUpdateIfExsist(referenceKey, map);
        LeakDetector.instance().produce(LeakQueue.instance().generateLeakMemoryInfo(referenceKey, referenceName));
    }
}

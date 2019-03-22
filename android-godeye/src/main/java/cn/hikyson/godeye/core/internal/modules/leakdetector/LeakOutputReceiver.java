package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;

import com.squareup.leakcanary.HeapDump;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.analyzer.leakcanary.AnalysisResult;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.analyzer.leakcanary.HeapAnalyzer;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.internal.LoadLeaks;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.output.OutputLeakService;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class LeakOutputReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, final Intent intent) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (OutputLeakService.OUTPUT_BOARDCAST_ACTION_START.equals(intent.getAction())) {
                    onLeakDumpStart(intent);
                } else if (HeapAnalyzer.OUTPUT_BOARDCAST_ACTION_PROGRESS.equals(intent.getAction())) {
                    onLeakDumpProgress(intent);
                } else if (OutputLeakService.OUTPUT_BOARDCAST_ACTION_RETRY.equals(intent.getAction())) {
                    onLeakDumpRetry(intent);
                } else if (OutputLeakService.OUTPUT_BOARDCAST_ACTION_DONE.equals(intent.getAction())) {
                    onLeakDumpDone(intent);
                }
                return true;
            }
        }).subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation()).subscribe();
    }

    private void onLeakDumpStart(Intent intent) {
        final String refrenceKey = intent.getStringExtra("refrenceKey");
        L.d("onLeakDumpStart:" + refrenceKey);
        Map<String, Object> map = new ArrayMap<>();
        map.put(LeakQueue.LeakMemoryInfo.Fields.LEAK_TIME, LeakQueue.LeakMemoryInfo.DF.format(new Date(System.currentTimeMillis())));
        map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS_SUMMARY, "Leak detected");
        map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS, LeakQueue.LeakMemoryInfo.Status.STATUS_START);
        LeakQueue.instance().createOrUpdateIfExsist(refrenceKey, map);
        LeakDetector.instance().produce(LeakQueue.instance().generateLeakMemoryInfo(refrenceKey));
    }

    private void onLeakDumpProgress(Intent intent) {
        final String refrenceKey = intent.getStringExtra("refrenceKey");
        final String progress = intent.getStringExtra("progress");
        L.d("onLeakDumpProgress:" + refrenceKey + " , progress:" + progress);
        Map<String, Object> map = new ArrayMap<>();
        map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS_SUMMARY, progress);
        map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS, LeakQueue.LeakMemoryInfo.Status.STATUS_PROGRESS);
        LeakQueue.instance().createOrUpdateIfExsist(refrenceKey, map);
        LeakDetector.instance().produce(LeakQueue.instance().generateLeakMemoryInfo(refrenceKey));
    }

    private void onLeakDumpRetry(Intent intent) {
        final String refrenceKey = intent.getStringExtra("refrenceKey");
        L.d("onLeakDumpRetry:" + refrenceKey);
        Map<String, Object> map = new ArrayMap<>();
        map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS_SUMMARY, "Retry waiting");
        map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS, LeakQueue.LeakMemoryInfo.Status.STATUS_RETRY);
        LeakQueue.instance().createOrUpdateIfExsist(refrenceKey, map);
        LeakDetector.instance().produce(LeakQueue.instance().generateLeakMemoryInfo(refrenceKey));
    }

    private void onLeakDumpDone(Intent intent) {
        final String refrenceKey = intent.getStringExtra("refrenceKey");
        HeapDump heapDump = (HeapDump) intent.getSerializableExtra("heapDump");
        final AnalysisResult analysisResult = (AnalysisResult) intent.getSerializableExtra("result");
        String summary = intent.getStringExtra("summary");
        String leakInfo = intent.getStringExtra("leakInfo");
        L.d("onLeakDumpDone:" + summary);
        new LoadLeaks(LeakDetector.getLeakDirectoryProvider(), heapDump.referenceKey, new LoadLeaks.OnLeakCallback() {
            @Override
            public void onLeak(List<String> list) {
                ThreadUtil.ensureWorkThread("LoadLeaks onLeak");
                L.d("onLeakDumpDone:" + refrenceKey + " , leak:" + analysisResult.className);
                Map<String, Object> map = new ArrayMap<>();
                map.put(LeakQueue.LeakMemoryInfo.Fields.LEAK_OBJ_NAME, analysisResult.className + (analysisResult.excludedLeak ? "[Excluded]" : ""));
                map.put(LeakQueue.LeakMemoryInfo.Fields.PATH_TO_ROOT, list);
                //因为计算对象引用的所有对象大小很耗时导致分析失败，所以分析跳过了这步，这里永远是0
                map.put(LeakQueue.LeakMemoryInfo.Fields.LEAK_MEMORY_BYTES, analysisResult.retainedHeapSize);
                map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS, LeakQueue.LeakMemoryInfo.Status.STATUS_DONE);
                map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS_SUMMARY, "done");
                LeakQueue.instance().createOrUpdateIfExsist(refrenceKey, map);
                LeakDetector.instance().produce(LeakQueue.instance().generateLeakMemoryInfo(refrenceKey));
            }

            @Override
            public void onLeakNull(String s) {
                ThreadUtil.ensureWorkThread("LoadLeaks onLeakNull");
                L.d("onLeakDumpDone:" + s);
                Map<String, Object> map = new ArrayMap<>();
                map.put(LeakQueue.LeakMemoryInfo.Fields.LEAK_OBJ_NAME, analysisResult.className + (analysisResult.excludedLeak ? "[Excluded]" : ""));
                //因为计算对象引用的所有对象大小很耗时导致分析失败，所以分析跳过了这步，这里永远是0
                map.put(LeakQueue.LeakMemoryInfo.Fields.LEAK_MEMORY_BYTES, analysisResult.retainedHeapSize);
                map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS, LeakQueue.LeakMemoryInfo.Status.STATUS_DONE);
                map.put(LeakQueue.LeakMemoryInfo.Fields.STATUS_SUMMARY, "leak null.");
                LeakQueue.instance().createOrUpdateIfExsist(refrenceKey, map);
                LeakDetector.instance().produce(LeakQueue.instance().generateLeakMemoryInfo(refrenceKey));
            }
        }).load();
    }
}

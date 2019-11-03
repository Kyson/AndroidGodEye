package cn.hikyson.godeye.core.internal.modules.crash;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import xcrash.ICrashCallback;
import xcrash.TombstoneManager;
import xcrash.TombstoneParser;
import xcrash.XCrash;

import static cn.hikyson.godeye.core.internal.modules.crash.CrashConstant.CRASH_MESSAGE;

/**
 * crash collector
 * not immediate:send crash info when crash if emergency(file store error) or crash module installed if not
 * immediate:send crash info when crash
 * <p>
 * Created by kysonchao on 2017/12/18.
 */
public class Crash extends ProduceableSubject<List<Map<String, String>>> implements Install<CrashContext> {
    private boolean mInstalled;
    private CrashContext mConfig;

    @Override
    public synchronized void install(final CrashContext crashContext) {
        if (mInstalled) {
            L.d("Crash already installed, ignore.");
            return;
        }
        mConfig = crashContext;
        Context context = crashContext.context();
        ICrashCallback callback = (logPath, emergency) -> {
            try {
                sendThenDeleteCrashLog(logPath, emergency, crashContext);
            } catch (IOException e) {
                L.e(e);
            }
        };
        XCrash.init(context, new XCrash.InitParameters()
                .setAppVersion(getAppVersion(context))
                .setJavaRethrow(true)
                .setJavaLogCountMax(10)
                .setJavaDumpAllThreadsWhiteList(new String[]{"^main$", "^Binder:.*", ".*Finalizer.*"})
                .setJavaDumpAllThreadsCountMax(10)
                .setJavaCallback(callback)
                .setNativeRethrow(true)
                .setNativeLogCountMax(10)
                .setNativeDumpAllThreadsWhiteList(new String[]{"^xcrash\\.sample$", "^Signal Catcher$", "^Jit thread pool$", ".*(R|r)ender.*", ".*Chrome.*"})
                .setNativeDumpAllThreadsCountMax(10)
                .setNativeCallback(callback)
                .setAnrRethrow(true)
                .setAnrLogCountMax(10)
                .setAnrCallback(callback)
                .setPlaceholderCountMax(3)
                .setPlaceholderSizeKb(512)
                .setLogFileMaintainDelayMs(1000));
        Schedulers.computation().scheduleDirect(this::sendThenDeleteCrashLogs);
        mInstalled = true;
        L.d("Crash installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("Crash already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        mInstalled = false;
        L.d("Crash uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mInstalled;
    }

    @Override
    public CrashContext config() {
        return mConfig;
    }

    private static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            L.e(e);
        }
        return "";
    }

    private void sendThenDeleteCrashLog(String logPath, String emergency, CrashContext crashContext) throws IOException {
        if (emergency != null || crashContext.immediate()) {// if emergency or immediate,output right now
            L.d("Crash produce message when emergency, crash count:%s, emergency:%s, logPath:%s", 1, emergency, logPath);
            produce(Collections.singletonList(wrapCrashMessage(TombstoneParser.parse(logPath, emergency))));
            TombstoneManager.deleteTombstone(logPath);
        }
    }

    private void sendThenDeleteCrashLogs() {
        File[] files = TombstoneManager.getAllTombstones();
        List<Map<String, String>> crashes = new ArrayList<>();
        for (File f : files) {
            try {
                crashes.add(wrapCrashMessage(TombstoneParser.parse(f.getAbsolutePath(), null)));
            } catch (IOException e) {
                L.e(e);
            }
        }
        L.d("Crash produce message when install, crash count:%s", crashes.size());
        produce(crashes);
        TombstoneManager.clearAllTombstones();
    }

    private Map<String, String> wrapCrashMessage(Map<String, String> crashMap) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(crashMap.get(TombstoneParser.keyCode))) {
            sb.append(crashMap.get(TombstoneParser.keyCode)).append(" | ");
        }
        String javaStackTrace = crashMap.get(TombstoneParser.keyJavaStacktrace);
        if (javaStackTrace != null) {
            String[] javaStackTraceLines = javaStackTrace.split("\n");
            if (javaStackTraceLines.length > 0) {
                sb.append(javaStackTraceLines[0]);
            }
        }
        crashMap.put(CRASH_MESSAGE, String.valueOf(sb));
        return crashMap;
    }

    @Override
    protected Subject<List<Map<String, String>>> createSubject() {
        return BehaviorSubject.create();
    }
}

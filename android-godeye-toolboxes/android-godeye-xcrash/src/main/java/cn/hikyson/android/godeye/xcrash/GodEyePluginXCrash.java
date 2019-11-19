package cn.hikyson.android.godeye.xcrash;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.annotation.Keep;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.core.internal.modules.crash.CrashContext;
import cn.hikyson.godeye.core.internal.modules.crash.CrashInfo;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xcrash.ICrashCallback;
import xcrash.TombstoneManager;
import xcrash.TombstoneParser;
import xcrash.XCrash;

@Keep
public class GodEyePluginXCrash {

    /**
     * entrace
     *
     * @param crashContext
     * @param consumer
     */
    public static void init(CrashContext crashContext, Consumer<List<CrashInfo>> consumer) {
        ICrashCallback callback = (logPath, emergency) -> {
            try {
                sendThenDeleteCrashLog(logPath, emergency, crashContext, consumer);
            } catch (IOException e) {
                L.e(e);
            }
        };
        XCrash.init(crashContext.context(), new XCrash.InitParameters()
                .setAppVersion(getAppVersion(crashContext.context()))
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
        Schedulers.computation().scheduleDirect(() -> {
            try {
                sendThenDeleteCrashLogs(consumer);
            } catch (Exception e) {
                L.e(e);
            }
        });
    }

    private static void sendThenDeleteCrashLog(String logPath, String emergency, CrashContext crashContext, Consumer<List<CrashInfo>> consumer) throws Exception {
        if (emergency != null || crashContext.immediate()) {// if emergency or immediate,output right now
            L.d("Crash produce message when emergency or immediate, crash count:%s, emergency:%s, logPath:%s", 1, emergency, logPath);
            consumer.accept(Collections.singletonList(wrapCrashMessage(TombstoneParser.parse(logPath, emergency))));
            TombstoneManager.deleteTombstone(logPath);
        }
    }

    private static void sendThenDeleteCrashLogs(Consumer<List<CrashInfo>> consumer) throws Exception {
        File[] files = TombstoneManager.getAllTombstones();
        List<CrashInfo> crashes = new ArrayList<>();
        for (File f : files) {
            try {
                crashes.add(wrapCrashMessage(TombstoneParser.parse(f.getAbsolutePath(), null)));
            } catch (IOException e) {
                L.e(e);
            }
        }
        if (!crashes.isEmpty()) {
            L.d("Crash produce message when install, crash count:%s", crashes.size());
            consumer.accept(crashes);
            TombstoneManager.clearAllTombstones();
        }
    }

    private static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            L.e(e);
        }
        return "";
    }

    private static CrashInfo wrapCrashMessage(Map<String, String> crashMap) {
        CrashInfo crashInfo = new CrashInfo();
        crashInfo.startTime = crashMap.remove(TombstoneParser.keyStartTime);
        crashInfo.crashTime = crashMap.remove(TombstoneParser.keyCrashTime);
        crashInfo.crashType = crashMap.remove(TombstoneParser.keyCrashType);
        crashInfo.processId = crashMap.remove(TombstoneParser.keyProcessId);
        crashInfo.processName = crashMap.remove(TombstoneParser.keyProcessName);
        crashInfo.threadId = crashMap.remove(TombstoneParser.keyThreadId);
        crashInfo.threadName = crashMap.remove(TombstoneParser.keyThreadName);
        crashInfo.nativeCrashCode = crashMap.remove(TombstoneParser.keyCode);
        crashInfo.nativeCrashSignal = crashMap.remove(TombstoneParser.keySignal);
        crashInfo.nativeCrashBacktrace = crashMap.remove(TombstoneParser.keyBacktrace);
        crashInfo.nativeCrashStack = crashMap.remove(TombstoneParser.keyStack);
        crashInfo.javaCrashStacktrace = crashMap.remove(TombstoneParser.keyJavaStacktrace);
        crashInfo.extras = crashMap;
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(crashInfo.nativeCrashCode)) {
            sb.append(crashInfo.nativeCrashCode).append(" | ");
        }
        if (!TextUtils.isEmpty(crashInfo.nativeCrashSignal)) {
            sb.append(crashInfo.nativeCrashSignal).append(" | ");
        }
        String javaStackTrace = crashInfo.javaCrashStacktrace;
        if (javaStackTrace != null) {
            String[] javaStackTraceLines = javaStackTrace.split("\n");
            if (javaStackTraceLines.length > 0) {
                sb.append(javaStackTraceLines[0]);
            }
        }
        crashInfo.crashMessage = String.valueOf(sb);
        return crashInfo;
    }
}

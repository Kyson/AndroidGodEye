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
package cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.squareup.leakcanary.HeapDump;
import com.squareup.leakcanary.RefWatcher;

import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.analyzer.leakcanary.AnalysisResult;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.internal.HeapAnalyzerService;
import cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.output.OutputLeakService;

import static android.text.format.Formatter.formatShortFileSize;
import static cn.hikyson.godeye.core.internal.modules.leakdetector.canary.android.internal.LeakCanaryInternals.isInServiceProcess;

public final class LeakCanary {
    @SuppressLint("StaticFieldLeak")
    private static ActivityRefWatcher sActivityRefWatcher;

    public static void install(Application application) {
        uninstall();
        sActivityRefWatcher = LeakCanary.refWatcher(application).listenerServiceClass(OutputLeakService.class)
                .excludedRefs(AndroidExcludedRefs.createAppDefaults().build())
                .buildAndInstall();
    }

    public static void uninstall() {
        if (sActivityRefWatcher != null) {
            sActivityRefWatcher.stopWatchingActivities();
            sActivityRefWatcher = null;
        }
    }

    /**
     * Builder to create a customized {@link RefWatcher} with appropriate Android defaults.
     */
    public static AndroidRefWatcherBuilder refWatcher(Context context) {
        return new AndroidRefWatcherBuilder(context);
    }

    /**
     * Returns a string representation of the result of a heap analysis.
     */
    public static String leakInfo(Context context, HeapDump heapDump, AnalysisResult result,
                                  boolean detailed) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        String versionName = packageInfo.versionName;
        int versionCode = packageInfo.versionCode;
        String info = "In " + packageName + ":" + versionName + ":" + versionCode + ".\n";
        String detailedString = "";
        if (result.leakFound) {
            if (result.excludedLeak) {
                info += "* EXCLUDED LEAK.\n";
            }
            info += "* " + result.className;
            if (!heapDump.referenceName.equals("")) {
                info += " (" + heapDump.referenceName + ")";
            }
            info += " has leaked:\n" + result.leakTrace.toString() + "\n";
            info += "* Retaining: " + formatShortFileSize(context, result.retainedHeapSize) + ".\n";
            if (detailed) {
                detailedString = "\n* Details:\n" + result.leakTrace.toDetailedString();
            }
        } else if (result.failure != null) {
            // We duplicate the library version & Sha information because bug reports often only contain
            // the stacktrace.
            info += "* FAILURE in :" + Log.getStackTraceString(
                    result.failure) + "\n";
        } else {
            info += "* NO LEAK FOUND.\n\n";
        }
        if (detailed) {
            detailedString += "* Excluded Refs:\n" + heapDump.excludedRefs;
        }

        info += "* Reference Key: "
                + heapDump.referenceKey
                + "\n"
                + "* Device: "
                + Build.MANUFACTURER
                + " "
                + Build.BRAND
                + " "
                + Build.MODEL
                + " "
                + Build.PRODUCT
                + "\n"
                + "* Android Version: "
                + Build.VERSION.RELEASE
                + " API: "
                + Build.VERSION.SDK_INT
                + "\n"
                + "* Durations: watch="
                + heapDump.watchDurationMs
                + "ms, gc="
                + heapDump.gcDurationMs
                + "ms, heap dump="
                + heapDump.heapDumpDurationMs
                + "ms, analysis="
                + result.analysisDurationMs
                + "ms"
                + "\n"
                + detailedString;

        return info;
    }

    /**
     * Whether the current process is the process running the {@link HeapAnalyzerService}, which is
     * a different process than the normal app process.
     */
    public static boolean isInAnalyzerProcess(Context context) {
        return isInServiceProcess(context, HeapAnalyzerService.class);
    }

    private LeakCanary() {
        throw new AssertionError();
    }
}

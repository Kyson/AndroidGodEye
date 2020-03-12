/*
 * Copyright (C) 2016 MarkZhai (http://zhaiyifan.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.hikyson.godeye.core.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.util.List;

public class ProcessUtils {

    private static volatile String sProcessName;
    private final static Object sNameLock = new Object();

    public static String myProcessName(Context context) {
        if (sProcessName != null) {
            return sProcessName;
        }
        synchronized (sNameLock) {
            if (sProcessName != null) {
                return sProcessName;
            }
            sProcessName = obtainProcessName(context);
            return sProcessName;
        }
    }

    private static String obtainProcessName(Context context) {
        final int pid = getCurrentPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> listTaskInfo = null;
        if (am != null) {
            listTaskInfo = am.getRunningAppProcesses();
        }
        if (listTaskInfo != null && !listTaskInfo.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo info : listTaskInfo) {
                if (info != null && info.pid == pid) {
                    return info.processName;
                }
            }
        }
        return null;
    }

    /**
     * 获取当前应用进程的pid
     *
     * @return
     */
    public static int getCurrentPid() {
        return android.os.Process.myPid();
    }

    public static int getCurrentUid() {
        return android.os.Process.myUid();
    }

    /**
     * 是否主进程
     * 提供给外部使用
     * @param application
     * @return
     */
    public static boolean isMainProcess(Application application) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) application.getSystemService
                (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return application.getPackageName().equals(processName);
    }
}

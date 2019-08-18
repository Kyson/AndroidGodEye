package cn.hikyson.godeye.core.internal.modules.memory;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import cn.hikyson.godeye.core.utils.ProcessUtils;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class MemoryUtil {
    private static AtomicLong sTotalMem = new AtomicLong(0L);
    private static ActivityManager sActivityManager;

    /**
     * 获取应用dalvik内存信息
     * 耗时忽略不计
     *
     * @return dalvik堆内存KB
     */
    public static HeapInfo getAppHeapInfo() {
        Runtime runtime = Runtime.getRuntime();
        HeapInfo heapInfo = new HeapInfo();
        heapInfo.freeMemKb = runtime.freeMemory() / 1024;
        heapInfo.maxMemKb = Runtime.getRuntime().maxMemory() / 1024;
        heapInfo.allocatedKb = (Runtime.getRuntime().totalMemory() - runtime.freeMemory()) / 1024;
        return heapInfo;
    }


    /**
     * 获取应用实际占用RAM
     *
     * @param context
     * @return 应用pss信息KB
     */
    public static PssInfo getAppPssInfo(Context context) {
        final int pid = ProcessUtils.getCurrentPid();
        if (sActivityManager == null) {
            sActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        Debug.MemoryInfo memoryInfo = sActivityManager.getProcessMemoryInfo(new int[]{pid})[0];
        PssInfo pssInfo = new PssInfo();
        pssInfo.totalPssKb = memoryInfo.getTotalPss();
        pssInfo.dalvikPssKb = memoryInfo.dalvikPss;
        pssInfo.nativePssKb = memoryInfo.nativePss;
        pssInfo.otherPssKb = memoryInfo.otherPss;
        return pssInfo;
    }

    public static RamInfo getRamInfo(Context context) {
        if (sActivityManager == null) {
            sActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        final ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        sActivityManager.getMemoryInfo(mi);
        final RamInfo ramMemoryInfo = new RamInfo();
        ramMemoryInfo.availMemKb = mi.availMem / 1024;
        ramMemoryInfo.isLowMemory = mi.lowMemory;
        ramMemoryInfo.lowMemThresholdKb = mi.threshold / 1024;
        ramMemoryInfo.totalMemKb = getRamTotalMem(sActivityManager);
        return ramMemoryInfo;
    }

    /**
     * 同步获取系统的总ram大小
     *
     * @param activityManager
     * @return
     */
    private static long getRamTotalMem(ActivityManager activityManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(mi);
            return mi.totalMem / 1024;
        } else if (sTotalMem.get() > 0L) {//如果已经从文件获取过值，则不需要再次获取
            return sTotalMem.get();
        } else {
            final long tm = getRamTotalMemByFile();
            sTotalMem.set(tm);
            return tm;
        }
    }


    /**
     * 获取手机的RAM容量，其实和activityManager.getMemoryInfo(mi).totalMem效果一样，也就是说，在API16以上使用系统API获取，低版本采用这个文件读取方式
     *
     * @return 容量KB
     */
    private static long getRamTotalMemByFile() {
        final String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine
                    .indexOf("MemTotal:"));
            br.close();
            return (long) Integer.parseInt(subMemoryLine.replaceAll(
                    "\\D+", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}

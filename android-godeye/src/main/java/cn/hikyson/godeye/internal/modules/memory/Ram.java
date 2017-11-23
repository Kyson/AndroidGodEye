package cn.hikyson.godeye.internal.modules.memory;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.Observable;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class Ram implements Snapshotable<RamInfo> {
    private Context mContext;

    public Ram(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public Observable<RamInfo> snapshot() {
        return Observable.fromCallable(new Callable<RamInfo>() {
            @Override
            public RamInfo call() throws Exception {
                ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
                final ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                am.getMemoryInfo(mi);
                final RamInfo ramMemoryInfo = new RamInfo();
                ramMemoryInfo.availMemKb = mi.availMem / 1024;
                ramMemoryInfo.isLowMemory = mi.lowMemory;
                ramMemoryInfo.lowMemThresholdKb = mi.threshold / 1024;
                ramMemoryInfo.totalMemKb = getRamTotalMem(am);
                return ramMemoryInfo;
            }
        });
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

    private static AtomicLong sTotalMem = new AtomicLong(0L);

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
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll(
                    "\\D+", ""));
            return totalMemorySize;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}

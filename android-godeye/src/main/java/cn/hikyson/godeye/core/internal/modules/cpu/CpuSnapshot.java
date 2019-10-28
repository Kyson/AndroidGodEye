package cn.hikyson.godeye.core.internal.modules.cpu;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import cn.hikyson.godeye.core.utils.IoUtil;

/**
 * 1. adb shell dumpsys cpuinfo |grep packagename
 * 2. adb shell top -m 10 -s cpu
 * <p>
 * Created by kysonchao on 2017/5/22.
 */
public class CpuSnapshot implements Serializable {
    public long user = 0;
    public long system = 0;
    public long idle = 0;
    public long ioWait = 0;
    public long total = 0;
    public long app = 0;

    public CpuSnapshot(long user, long system, long idle, long ioWait, long total, long app) {
        this.user = user;
        this.system = system;
        this.idle = idle;
        this.ioWait = ioWait;
        this.total = total;
        this.app = app;
    }

    public CpuSnapshot() {
    }

    private static final int BUFFER_SIZE = 1024;

    /**
     * cpu快照
     *
     * @return invalid如果发生了异常
     */
    @WorkerThread
    public synchronized static @NonNull
    CpuSnapshot snapshot() {
        try {
            return parse(getCpuRate(), getCpuRateOfApp());
        } catch (Throwable throwable) {
            return new CpuSnapshot();
        }
    }

    private static String getCpuRate() {
        BufferedReader cpuReader = null;
        try {
            cpuReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), BUFFER_SIZE);
            String cpuRate = cpuReader.readLine();
            if (cpuRate == null) {
                return "";
            }
            return cpuRate;
        } catch (Throwable e) {
            return "";
        } finally {
            IoUtil.closeSilently(cpuReader);
        }
    }

    private static String getCpuRateOfApp() {
        BufferedReader pidReader = null;
        try {
            int pid = android.os.Process.myPid();
            pidReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + pid + "/stat")), BUFFER_SIZE);
            String pidCpuRate = pidReader.readLine();
            if (pidCpuRate == null) {
                return "";
            }
            return pidCpuRate;
        } catch (Throwable throwable) {
            return "";
        } finally {
            IoUtil.closeSilently(pidReader);
        }
    }

    private static CpuSnapshot parse(String cpuRate, String pidCpuRate) throws Throwable {
        String[] cpuInfoArray = cpuRate.split(" ");
        if (cpuInfoArray.length < 9) {
            throw new IllegalStateException("cpu info array size must great than 9");
        }
        long user = Long.parseLong(cpuInfoArray[2]);
        long nice = Long.parseLong(cpuInfoArray[3]);
        long system = Long.parseLong(cpuInfoArray[4]);
        long idle = Long.parseLong(cpuInfoArray[5]);
        long ioWait = Long.parseLong(cpuInfoArray[6]);
        long total = user + nice + system + idle + ioWait
                + Long.parseLong(cpuInfoArray[7])
                + Long.parseLong(cpuInfoArray[8]);
        String[] pidCpuInfoList = pidCpuRate.split(" ");
        if (pidCpuInfoList.length < 17) {
            throw new IllegalStateException("pid cpu info array size must great than 17");
        }
        long appCpuTime = Long.parseLong(pidCpuInfoList[13])
                + Long.parseLong(pidCpuInfoList[14])
                + Long.parseLong(pidCpuInfoList[15])
                + Long.parseLong(pidCpuInfoList[16]);
        return new CpuSnapshot(user, system, idle, ioWait, total, appCpuTime);
    }
}

package cn.hikyson.godeye.core.internal.modules.cpu;

import android.text.TextUtils;

import androidx.annotation.VisibleForTesting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.hikyson.godeye.core.utils.IoUtil;
import cn.hikyson.godeye.core.utils.L;

public class CpuUsage {

    private static boolean isReadFromFile;

    static {
        isReadFromFile = cpuFileUsability();
    }

    private static CpuSnapshot sLastCpuSnapshot;

    public static CpuInfo getCpuInfo() {
        if (isReadFromFile) {
            if (sLastCpuSnapshot == null) {
                sLastCpuSnapshot = parse(getCpuRateOfDevice(), getCpuRateOfApp());
                return CpuInfo.INVALID;
            } else {
                CpuSnapshot current = parse(getCpuRateOfDevice(), getCpuRateOfApp());
                float totalTime = (current.total - sLastCpuSnapshot.total) * 1.0f;
                if (totalTime <= 0) {
                    L.e("totalTime must greater than 0");
                    return CpuInfo.INVALID;
                }
                long idleTime = current.idle - sLastCpuSnapshot.idle;
                double totalRatio = (totalTime - idleTime) / totalTime;
                double appRatio = (current.app - sLastCpuSnapshot.app) / totalTime;
                double userRatio = (current.user - sLastCpuSnapshot.user) / totalTime;
                double systemRatio = (current.system - sLastCpuSnapshot.system) / totalTime;
                double ioWaitRatio = (current.ioWait - sLastCpuSnapshot.ioWait) / totalTime;
                return new CpuInfo(filterCpuRatio(totalRatio), filterCpuRatio(appRatio), filterCpuRatio(userRatio), filterCpuRatio(systemRatio), filterCpuRatio(ioWaitRatio));
            }
        } else {
            return getCpuInfoFromShell();
        }
    }

    private static double filterCpuRatio(double ratio) {
        if (ratio < 0 || ratio > 1) {
            return 0;
        }
        return ratio;
    }

    private static boolean cpuFileUsability() {
        File stat = new File("/proc/stat");
        if (!stat.exists() || !stat.canRead()) {
            return false;
        }
        int pid = android.os.Process.myPid();
        File statPid = new File("/proc/" + pid + "/stat");
        if (!statPid.exists() || !statPid.canRead()) {
            return false;
        }
        return true;
    }

    private static final int BUFFER_SIZE = 1024;

    private static String getCpuRateOfDevice() {
        BufferedReader cpuReader = null;
        try {
            cpuReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), BUFFER_SIZE);
            String cpuRate = cpuReader.readLine();
            if (cpuRate == null) {
                return "";
            }
            return cpuRate.trim();
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

    @VisibleForTesting
    static CpuSnapshot parse(String cpuRate, String pidCpuRate) {
        String[] cpuInfoArray = cpuRate.split("\\s+");
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

    /**
     * Tasks: 685 total,   1 running, 453 sleeping,   0 stopped,   0 zombie
     * Mem:   7656764k total,  7049688k used,   607076k free,   155716k buffers
     * Swap:  2621436k total,   515840k used,  2105596k free,  3681948k cached
     * 800%cpu   7%user   3%nice  28%sys 755%idle   0%iow   3%irq   3%sirq   0%host
     * PID USER         PR  NI VIRT  RES  SHR S[%CPU] %MEM     TIME+ ARGS
     * 21637 shell        20   0  13M 4.6M 3.7M R 10.3   0.0   0:00.07 top -n 1
     * 20750 root         20   0    0    0    0 I  6.8   0.0   0:02.43 [kworker/u16:5]
     * 816 system       -2  -8 2.1G  27M  22M S  6.8   0.3  38:04.22 surfaceflinger
     * 14471 shell        20   0  60M 5.6M 4.9M S  3.4   0.0   0:13.73 transport -conf+
     * 12900 u0_a41       20   0 4.1G 131M 131M S  3.4   1.7  26:43.83 com.google.andr+
     *
     * @return
     */
    private static CpuInfo getCpuInfoFromShell() {
        java.lang.Process process = null;
        CpuInfo cpuInfo = new CpuInfo();
        try {
            process = Runtime.getRuntime().exec("top -n 1");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int cpuIndex = -1;
            Map<String, Float> cpuDevice = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (TextUtils.isEmpty(line)) {
                    continue;
                }
                Map<String, Float> tempCpuDevice = parseCpuRateOfDeviceAndTotalByShell(line);
                if (tempCpuDevice != null) {
                    // 800%cpu   7%user   3%nice  28%sys 755%idle   0%iow   3%irq   3%sirq   0%host
                    cpuDevice = tempCpuDevice;
                    Float total = cpuDevice.get("cpu");
                    Float user = cpuDevice.get("user");
                    Float sys = cpuDevice.get("sys");
                    Float idle = cpuDevice.get("idle");
                    Float iow = cpuDevice.get("iow");
                    if (total != null && total > 0 && iow != null && iow >= 0) {
                        cpuInfo.ioWaitRatio = iow / total;
                    }
                    if (total != null && total > 0 && sys != null && sys >= 0) {
                        cpuInfo.sysCpuRatio = sys / total;
                    }
                    if (total != null && total > 0 && idle != null && idle >= 0) {
                        cpuInfo.totalUseRatio = (total - idle) / total;
                    }
                    if (total != null && total > 0 && user != null && user >= 0) {
                        cpuInfo.userCpuRatio = user / total;
                    }
                    continue;
                }
                int tempIndex = parseCPUIndex(line);
                if (tempIndex != -1) {
                    // PID USER         PR  NI VIRT  RES  SHR S[%CPU] %MEM     TIME+ ARGS
                    cpuIndex = tempIndex;
                    continue;
                }
                float tempAppRatio = parseCpuRateOfAppByShell(line, cpuIndex, cpuDevice == null ? null : cpuDevice.get("cpu"));
                if (tempAppRatio != -1) {
                    // 816 system       -2  -8 2.1G  27M  22M S  6.8   0.3  38:04.22 surfaceflinger
                    cpuInfo.appCpuRatio = tempAppRatio;
                    return cpuInfo;
                }
            }
        } catch (IOException e) {
            L.e(e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return cpuInfo;
    }

    @VisibleForTesting
    static int parseCPUIndex(String line) {
        if (line.contains("CPU")) {
            String[] titles = line.split("\\s+");
            for (int i = 0; i < titles.length; i++) {
                if (titles[i].contains("CPU")) {
                    return i;
                }
            }
        }
        return -1;
    }

    @VisibleForTesting
    static Map<String, Float> parseCpuRateOfDeviceAndTotalByShell(String line) {
        if (line.matches("^\\d+%\\w+.+\\d+%\\w+")) {
            String lineLowerCase = line.toLowerCase(Locale.US);
            String[] cpuList = lineLowerCase.split("\\s+");
            Map<String, Float> cpuMap = new HashMap<>();
            for (String s : cpuList) {
                String[] cpuItem = s.split("%");
                if (cpuItem.length != 2) {
                    throw new IllegalStateException("parseCpuRateOfDeviceAndTotalByShell but cpuItem.length != 2");
                }
                try {
                    cpuMap.put(cpuItem[1], Float.parseFloat(cpuItem[0]));
                } catch (Throwable e) {
                    throw new IllegalStateException("parseCpuRateOfDeviceAndTotalByShell but " + e);
                }
            }
            return cpuMap;
        }
        return null;
    }

    @VisibleForTesting
    static float parseCpuRateOfAppByShell(String line, int cpuIndex, Float cpuTotal) {
        if (line.startsWith(String.valueOf(android.os.Process.myPid()))) {
            if (cpuIndex == -1) {
                throw new IllegalStateException("parseCpuRateOfAppByShell but cpuIndex == -1:" + line);
            }
            String[] param = line.split("\\s+");
            if (param.length <= cpuIndex) {
                throw new IllegalStateException("parseCpuRateOfAppByShell but param.length <= cpuIndex:" + line);
            }
            String cpu = param[cpuIndex];
            if (cpu.endsWith("%")) {
                cpu = cpu.substring(0, cpu.lastIndexOf("%"));
            }
            if (cpuTotal == null || cpuTotal <= 0) {
                throw new IllegalStateException("parseCpuRateOfAppByShell but cpuTotal == null || cpuTotal <= 0:" + line);
            }
            try {
                return Float.parseFloat(cpu) / cpuTotal;
            } catch (Throwable e) {
                throw new IllegalStateException("parseCpuRateOfAppByShell but " + e + ":" + line);
            }
        }
        return -1;
    }

}

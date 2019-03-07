package cn.hikyson.godeye.core.internal.modules.cpu;

import java.io.File;

public class CpuUsable {

    public static boolean usability() {
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
}

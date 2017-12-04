package cn.hikyson.godeye.core.internal.modules.cpu;

import java.io.Serializable;
import java.util.Locale;

/**
 * Description:
 * 一个sample时间段内
 * 总的cpu使用率
 * app的cpu使用率
 * 用户进程cpu使用率
 * 系统进程cpu使用率
 * io等待时间占比
 * Author: hui.zhao
 * Date: 2017/2/8
 * Copyright: Ctrip
 */
public class CpuInfo implements Serializable {
    public static final CpuInfo INVALID = new CpuInfo();

    // 总的cpu使用率(user + system+io+其他)
    public double totalUseRatio;
    // app的cpu使用率
    public double appCpuRatio;
    // 用户进程cpu使用率
    public double userCpuRatio;
    // 系统进程cpu使用率
    public double sysCpuRatio;
    // io等待时间占比
    public double ioWaitRatio;

    public CpuInfo(double totalUseRatio, double appCpuRatio, double userCpuRatio, double sysCpuRatio, double
            ioWaitRatio) {
        this.totalUseRatio = totalUseRatio;
        this.appCpuRatio = appCpuRatio;
        this.userCpuRatio = userCpuRatio;
        this.sysCpuRatio = sysCpuRatio;
        this.ioWaitRatio = ioWaitRatio;
    }

    public CpuInfo() {
    }

    @Override
    public String toString() {
        return "app:" +
                String.format(Locale.US, "%.1f", appCpuRatio * 100f) +
                "% , total:" +
                String.format(Locale.US, "%.1f", totalUseRatio * 100f) +
                "% , user:" +
                String.format(Locale.US, "%.1f", userCpuRatio * 100f) +
                "% , system:" +
                String.format(Locale.US, "%.1f", sysCpuRatio * 100f) +
                "% , iowait:" +
                String.format(Locale.US, "%.1f", ioWaitRatio * 100f) + "%";
    }
}

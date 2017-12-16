package cn.hikyson.godeye.core;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import cn.hikyson.godeye.core.internal.modules.battery.BatteryInfo;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuInfo;

/**
 * Created by kysonchao on 2017/12/16.
 */
@RunWith(AndroidJUnit4.class)
public class GodEyeTest {

    @Test
    public void cpu() throws ExecutionException, InterruptedException {
        GodEye.instance().cpu().install();
        ConsumerFuture<CpuInfo> consumerFuture = new ConsumerFuture<>();
        GodEye.instance().cpu().subject().subscribe(consumerFuture);
        CpuInfo cpuInfo = consumerFuture.get();
        Assert.assertTrue(cpuInfo != null);
        Assert.assertTrue(cpuInfo.totalUseRatio >= cpuInfo.appCpuRatio
                && cpuInfo.totalUseRatio >= cpuInfo.ioWaitRatio
                && cpuInfo.totalUseRatio >= cpuInfo.sysCpuRatio
                && cpuInfo.totalUseRatio >= cpuInfo.userCpuRatio
                && cpuInfo.appCpuRatio >= 0
                && cpuInfo.ioWaitRatio >= 0
                && cpuInfo.sysCpuRatio >= 0
                && cpuInfo.userCpuRatio >= 0
        );
        GodEye.instance().cpu().uninstall();
    }

    @Test
    public void battery() throws ExecutionException, InterruptedException {
        Context context = InstrumentationRegistry.getTargetContext();
        GodEye.instance().battery().install(context);
        ConsumerFuture<BatteryInfo> consumerFuture = new ConsumerFuture<>();
        GodEye.instance().battery().subject().subscribe(consumerFuture);
        BatteryInfo batteryInfo = consumerFuture.get();
        Assert.assertTrue(batteryInfo != null);
        Assert.assertTrue(batteryInfo.level >= 0
                && batteryInfo.level <= batteryInfo.scale);
        GodEye.instance().battery().uninstall();
    }

}

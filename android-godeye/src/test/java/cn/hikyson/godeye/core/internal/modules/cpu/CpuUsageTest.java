package cn.hikyson.godeye.core.internal.modules.cpu;

import android.os.Build;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Map;

import cn.hikyson.godeye.core.helper.RoboTestApplication;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class CpuUsageTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parse() {
        String deviceCpu0 = "800%cpu   0%user   0%nice   0%sys 800%idle   0%iow   0%irq   0%sirq   0%host";
        String appCpu0 = "[7m  PID USER         PR  NI VIRT  RES  SHR S[%CPU] %MEM     TIME+ ARGS            \u001B[0m";
        try {
            CpuSnapshot cpuSnapshot = CpuUsage.parse(deviceCpu0, appCpu0);
            Assert.fail();
        } catch (NumberFormatException ignore) {
        }
        String deviceCpu1 = "cpu  4473 1068 4565 21155 559 0 29 0 0 0";
        String appCpu1 = "3238 (d.godeye.sample) S 1189 1189 0 0 -1 1077961024 25948 0 3 0 94 111 0 0 20 0 35 0 4901 1332465664 16104 4294967295 3078545408 3078556388 3213513904 3213510820 3077933717 0 4612 0 1073779964 4294967295 0 0 17 3 0 0 1 0 0 3078561120 3078561772 3088125952 3213515788 3213515864 3213515864 3213516772 0";
        CpuSnapshot cpuSnapshot = CpuUsage.parse(deviceCpu1, appCpu1);
        Assert.assertEquals(1068, cpuSnapshot.user);
        Assert.assertEquals(21155, cpuSnapshot.system);
        Assert.assertEquals(559, cpuSnapshot.idle);
        Assert.assertEquals(0, cpuSnapshot.ioWait);
        Assert.assertEquals(27376, cpuSnapshot.total);
        Assert.assertEquals(205, cpuSnapshot.app);
    }

    @Test
    public void parseCpuRateOfAppByShell() {
        String line2 = "800%cpu   0%user   0%nice   0%sys 800%idle   0%iow   0%irq   0%sirq   0%host";
        String line3 = "[7m  PID USER         PR  NI VIRT  RES  SHR S[%CPU] %MEM     TIME+ ARGS            \u001B[0m";
        String line4 = "0 u0_a591      10 -10 5.4G 131M  80M S 16.0   1.7   0:02.98 cn.hikyson.andr+";
        String line5 = "0wfwf fweffwef  fwef6.064564f3402.98 cn.hikyson.andr+";
        int index = CpuUsage.parseCPUIndex(line3);
        Map<String, Float> tempCpuDevice = CpuUsage.parseCpuRateOfDeviceAndTotalByShell(line2);
        try {
            CpuUsage.parseCpuRateOfAppByShell(line5, index, tempCpuDevice.get("cpu"));
            Assert.fail();
        } catch (IllegalStateException ignore) {
        }
        try {
            CpuUsage.parseCpuRateOfAppByShell(line4, -1, tempCpuDevice.get("cpu"));
            Assert.fail();
        } catch (IllegalStateException ignore) {
        }
        try {
            CpuUsage.parseCpuRateOfAppByShell(line4, index, tempCpuDevice.get("unknown"));
            Assert.fail();
        } catch (IllegalStateException ignore) {
        }
        float appCpu0 = CpuUsage.parseCpuRateOfAppByShell(line4, index, tempCpuDevice.get("cpu"));
        Assert.assertEquals((float) Float.parseFloat("16.0") / 800, appCpu0, 0.1);
    }

    @Test
    public void parseCpuRateOfDeviceAndTotalByShell() {
        String line0 = "[s\u001B[999C\u001B[999B\u001B[6n\u001B[u\u001B[H\u001B[J\u001B[?25l\u001B[H\u001B[J\u001B[s\u001B[999C\u001B[999B\u001B[6n\u001B[uTasks: 2 total,   1 running,   1 sleeping,   0 stopped,   0 zombie";
        String line1 = "Mem:      7.2G total,      7.0G used,      266M free,       98M buffers";
        String line2 = "800%cpu   0%user   0%nice   0%sys 800%idle   0%iow   0%irq   0%sirq   0%host";
        String line3 = "[7m  PID USER         PR  NI VIRT  RES  SHR S[%CPU] %MEM     TIME+ ARGS            \u001B[0m";
        Map<String, Float> tempCpuDevice0 = CpuUsage.parseCpuRateOfDeviceAndTotalByShell(line0);
        Map<String, Float> tempCpuDevice1 = CpuUsage.parseCpuRateOfDeviceAndTotalByShell(line1);
        Map<String, Float> tempCpuDevice2 = CpuUsage.parseCpuRateOfDeviceAndTotalByShell(line2);
        Map<String, Float> tempCpuDevice3 = CpuUsage.parseCpuRateOfDeviceAndTotalByShell(line3);
        Assert.assertNull(tempCpuDevice0);
        Assert.assertNull(tempCpuDevice1);
        Assert.assertNull(tempCpuDevice3);
        Assert.assertEquals(800, tempCpuDevice2.get("cpu"), 0);
        Assert.assertEquals(0, tempCpuDevice2.get("sys"), 0);
        Assert.assertEquals(800, tempCpuDevice2.get("idle"), 0);
    }

    @Test
    public void parseCPUIndex() {
        String line0 = "[s\u001B[999C\u001B[999B\u001B[6n\u001B[u\u001B[H\u001B[J\u001B[?25l\u001B[H\u001B[J\u001B[s\u001B[999C\u001B[999B\u001B[6n\u001B[uTasks: 2 total,   1 running,   1 sleeping,   0 stopped,   0 zombie";
        String line1 = "Mem:      7.2G total,      7.0G used,      266M free,       98M buffers";
        String line2 = "800%cpu   0%user   0%nice   0%sys 800%idle   0%iow   0%irq   0%sirq   0%host";
        String line3 = "[7m  PID USER         PR  NI VIRT  RES  SHR S[%CPU] %MEM     TIME+ ARGS            \u001B[0m";
        Assert.assertEquals(-1, CpuUsage.parseCPUIndex(line0));
        Assert.assertEquals(-1, CpuUsage.parseCPUIndex(line1));
        Assert.assertEquals(-1, CpuUsage.parseCPUIndex(line2));
        Assert.assertEquals(8, CpuUsage.parseCPUIndex(line3));
    }

}
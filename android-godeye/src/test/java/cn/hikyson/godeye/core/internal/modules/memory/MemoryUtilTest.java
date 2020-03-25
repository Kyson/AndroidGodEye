package cn.hikyson.godeye.core.internal.modules.memory;

import android.os.Build;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.helper.RoboTestApplication;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class MemoryUtilTest {

    @Test
    public void getAppHeapInfo() {
        HeapInfo heapInfo = MemoryUtil.getAppHeapInfo();
        Assert.assertTrue(heapInfo.allocatedKb > 0);
        Assert.assertTrue(heapInfo.freeMemKb > 0);
        Assert.assertTrue(heapInfo.maxMemKb > 0);
    }

    @Test
    public void getAppPssInfo() {
        try {
            PssInfo pssInfo = MemoryUtil.getAppPssInfo(GodEye.instance().getApplication());
        } catch (NullPointerException ignore) {
        } catch (Throwable e) {
            Assert.fail();
        }
    }

    @Test
    public void getRamInfo() {
        RamInfo ramInfo = MemoryUtil.getRamInfo(GodEye.instance().getApplication());
        Assert.assertNotNull(ramInfo);
    }
}
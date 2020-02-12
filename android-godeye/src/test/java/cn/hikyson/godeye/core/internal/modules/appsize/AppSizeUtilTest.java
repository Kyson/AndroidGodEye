package cn.hikyson.godeye.core.internal.modules.appsize;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.helper.Log4Test;
import cn.hikyson.godeye.core.helper.RoboTestApplication;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class AppSizeUtilTest {

    @Test(timeout = 5000)
    @Config(sdk = Build.VERSION_CODES.LOLLIPOP)
    public void getAppSizeLowerO() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        final AppSizeInfo[] appSizeInfo = new AppSizeInfo[1];
        AppSizeUtil.getAppSize(ApplicationProvider.getApplicationContext(), new AppSizeUtil.OnGetSizeListener() {
            @Override
            public void onGetSize(AppSizeInfo ctAppSizeInfo) {
                countDownLatch.countDown();
                appSizeInfo[0] = ctAppSizeInfo;
            }

            @Override
            public void onError(Throwable t) {
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Assert.fail();
        }
        Log4Test.d(String.valueOf(appSizeInfo[0]));
        Assert.assertTrue(appSizeInfo[0].dataSize >= 0);
        Assert.assertTrue(appSizeInfo[0].codeSize >= 0);
        Assert.assertTrue(appSizeInfo[0].cacheSize >= 0);
    }

    @Test
    public void formatSize() {
        final long oneByte = 1;
        final long oneKByte = 1 * 1024;
        final long oneMByte = 1 * 1024 * 1024;
        final long oneGByte = 1 * 1024 * 1024 * 1024;
        Assert.assertEquals("2B", AppSizeUtil.formatSize(oneByte * 2));
        Assert.assertEquals("2KB", AppSizeUtil.formatSize(oneKByte * 2 + oneByte));
        Assert.assertEquals("2MB", AppSizeUtil.formatSize(oneMByte * 2 + 2 * oneKByte));
        Assert.assertEquals("2.1MB", AppSizeUtil.formatSize(oneMByte * 2 + 101 * oneKByte));
        Assert.assertEquals("2.1GB", AppSizeUtil.formatSize(oneGByte * 2 + 101 * oneMByte + 101 * oneKByte));
        Assert.assertEquals("2GB", AppSizeUtil.formatSize(oneGByte * 2 + 101 * oneKByte));
    }
}
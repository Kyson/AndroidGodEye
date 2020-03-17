package cn.hikyson.godeye.core.internal.notification;

import android.os.Build;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.godeye.core.utils.ThreadUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class NotificationConsumerTest {

    @Before
    public void setUp() throws Exception {
        ThreadUtil.setNeedDetectRunningThread(false);
    }

    @After
    public void tearDown() throws Exception {
        ThreadUtil.setNeedDetectRunningThread(true);
    }

    @Test
    public void accept() {
        try {
            new NotificationConsumer(null).accept(new NotificationContent("AndroidGodEye_message", null));
            CountDownLatch countDownLatch = new CountDownLatch(1);
            final NotificationContent[] notificationContent0 = new NotificationContent[1];
            new NotificationConsumer(Arrays.asList(new NotificationListener() {
                @Override
                public void onNotificationReceive(long timeMillis, NotificationContent notificationContent) {
                    notificationContent0[0] = notificationContent;
                    countDownLatch.countDown();
                }
            })).accept(new NotificationContent("AndroidGodEye_message", null));
            countDownLatch.await(1, TimeUnit.SECONDS);
            assertEquals("AndroidGodEye_message", notificationContent0[0].message);
        } catch (Exception e) {
            fail();
        }
    }

}
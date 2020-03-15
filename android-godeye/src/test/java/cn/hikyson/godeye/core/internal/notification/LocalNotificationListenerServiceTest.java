package cn.hikyson.godeye.core.internal.notification;

import android.content.Intent;
import android.os.Build;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.helper.RoboTestApplication;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class LocalNotificationListenerServiceTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void start() {
        LocalNotificationListenerService.start("AndroidGodEye");
    }

    @Test
    public void stop() {
        LocalNotificationListenerService.stop();
    }

    @Test
    public void onLifecycle() {
        Intent intent = new Intent(GodEye.instance().getApplication(), LocalNotificationListenerService.class);
        intent.putExtra("message", "AndroidGodEye");
        intent.setAction("START_FOREGROUND_ACTION");
        Robolectric.buildService(LocalNotificationListenerService.class, intent).bind().create().startCommand(0, 0).destroy();

        Intent intent2 = new Intent(GodEye.instance().getApplication(), LocalNotificationListenerService.class);
        intent.setAction("STOP_FOREGROUND_ACTION");
        Robolectric.buildService(LocalNotificationListenerService.class, intent2).bind().create().startCommand(0, 0).destroy();
    }

}
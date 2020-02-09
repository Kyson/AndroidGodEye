package cn.hikyson.godeye.core.helper;

import android.app.Notification;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class NotifierTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Note: use jacoco if java.lang.VerifyError: Bad return type
     */
    @Config(sdk = Build.VERSION_CODES.O)
    @Test
    public void noticeHigherThanO() {
        notice();
    }

    @Config(sdk = Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void noticeLowerThanO() {
        notice();
    }

    private void notice() {
        Notification notification = Notifier.create(ApplicationProvider.getApplicationContext(), new Notifier.Config("AndroidGodEye-Title", "AndroidGodEye-Message", "AndroidGodEye-Detail"));
        int id = Notifier.notice(ApplicationProvider.getApplicationContext(), Notifier.createNoticeId(), notification);
        ThreadUtil.sleep(10);
        Notifier.cancelNotice(ApplicationProvider.getApplicationContext(), id);

        int id2 = Notifier.notice(ApplicationProvider.getApplicationContext(), new Notifier.Config("AndroidGodEye-Title", "AndroidGodEye-Message", "AndroidGodEye-Detail"));
        ThreadUtil.sleep(10);
        Notifier.cancelNotice(ApplicationProvider.getApplicationContext(), id2);
    }
}
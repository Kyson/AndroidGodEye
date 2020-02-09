package cn.hikyson.godeye.core.helper;

import android.os.Build;
import android.os.SystemClock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class AndroidDebugTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isDebugging() {
        long startTime = SystemClock.elapsedRealtime();
        for (int i = 0; i < 1000; i++) {
            AndroidDebug.isDebugging();
        }
        Log4Test.d(String.format("AndroidDebugTest isDebugging cost %sms for 1000 times.", SystemClock.elapsedRealtime() - startTime));
    }
}
package cn.hikyson.godeye.core.utils;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import cn.hikyson.godeye.core.helper.RoboTestApplication;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class ProcessUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void myProcessName() {
        String myProcessName = ProcessUtils.myProcessName(ApplicationProvider.getApplicationContext());
        Assert.assertEquals("cn.hikyson.godeye.core", myProcessName);
        String myProcessName2 = ProcessUtils.myProcessName(ApplicationProvider.getApplicationContext());
        Assert.assertEquals("cn.hikyson.godeye.core", myProcessName2);
    }

    @Test
    public void getCurrentPid() {
        int pid = ProcessUtils.getCurrentPid();
    }

    @Test
    public void getCurrentUid() {
        int uid = ProcessUtils.getCurrentUid();
    }

    @Test
    public void isMainProcess() {
        boolean isMainProcess = ProcessUtils.isMainProcess(ApplicationProvider.getApplicationContext());
        Assert.assertTrue(isMainProcess);
    }
}
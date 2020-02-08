package cn.hikyson.godeye.core.utils;

import android.os.Build;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import cn.hikyson.godeye.core.helper.RoboTestApplication;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class StacktraceUtilTest {

    @Test
    public void stackTraceToStringArray() {
        List<String> stackTrace = StacktraceUtil.stackTraceToStringArray(Arrays.asList(Thread.currentThread().getStackTrace()));
        Assert.assertTrue(String.valueOf(stackTrace).contains(StacktraceUtilTest.class.getSimpleName()));
    }

    @Test
    public void getStackTraceOfThread() {
        List<String> stackTrace = StacktraceUtil.getStackTraceOfThread(Thread.currentThread());
        Assert.assertTrue(String.valueOf(stackTrace).contains(StacktraceUtilTest.class.getSimpleName()));
    }

    @Test
    public void getStringOfThrowable() {
        String detail = StacktraceUtil.getStringOfThrowable(new Throwable("AndroidGodEye-Exception"));
        Assert.assertTrue(detail.contains(StacktraceUtilTest.class.getSimpleName()));
        Assert.assertTrue(detail.contains("AndroidGodEye-Exception"));
    }
}
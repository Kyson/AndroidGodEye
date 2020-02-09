package cn.hikyson.godeye.core.utils;

import android.os.Build;

import androidx.annotation.Keep;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import cn.hikyson.godeye.core.helper.Log4Test;
import cn.hikyson.godeye.core.helper.RoboTestApplication;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class ReflectUtilTest {

    @Keep
    private static boolean testMethod(String extra, int extra2, boolean extra3) {
        Log4Test.d("AndroidGodEye: This is testMethod." + extra + extra2 + extra3);
        return true;
    }

    @Test
    public void invokeStaticMethod() {
        Object result = ReflectUtil.invokeStaticMethod(ReflectUtilTest.class, "testMethod",
                new Class<?>[]{String.class, int.class, boolean.class}, new Object[]{"AndroidGodEye-String", 10, false});
        Assert.assertTrue((Boolean) result);
        Object result2 = ReflectUtil.invokeStaticMethod(ReflectUtilTest.class.getName(), "testMethod2",
                new Class<?>[]{String.class, int.class, boolean.class}, new Object[]{"AndroidGodEye-String", 10, false});
        Assert.assertNull(result2);
        try {
            Object result3 = ReflectUtil.invokeStaticMethodUnSafe(ReflectUtilTest.class.getName(), "testMethod",
                    new Class<?>[]{String.class, int.class, boolean.class}, new Object[]{"AndroidGodEye-String", 10, false});
            Assert.assertTrue((Boolean) result3);
        } catch (Exception e) {
            Assert.fail();
        }
        try {
            Object result4 = ReflectUtil.invokeStaticMethodUnSafe(ReflectUtilTest.class.getName(), "testMethod",
                    new Class<?>[]{String.class, int.class}, new Object[]{"AndroidGodEye-String", 10});
            Assert.fail();
        } catch (Exception ignore) {
        }
    }
}
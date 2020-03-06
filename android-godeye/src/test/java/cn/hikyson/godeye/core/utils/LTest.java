package cn.hikyson.godeye.core.utils;

import android.os.Build;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.CountDownLatch;

import cn.hikyson.godeye.core.helper.RoboTestApplication;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class LTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        L.setProxy(null);
    }

    @Test
    public void d() {
        CountDownLatch countDownLatch = new CountDownLatch(4);
        L.setProxy(new L.LogProxy() {
            @Override
            public void d(String msg) {
                countDownLatch.countDown();
                if (countDownLatch.getCount() == 3) {
                    Assert.assertEquals("AndroidGodEye-String", msg);
                }
                if (countDownLatch.getCount() == 2) {
                    Assert.assertTrue(msg.contains("AndroidGodEye-Exception"));
                }
                if (countDownLatch.getCount() == 1) {
                    Assert.assertTrue(msg.contains(String.valueOf(LTest.this)));
                }
                if (countDownLatch.getCount() == 0) {
                    Assert.assertEquals("null", msg);
                }
            }

            @Override
            public void w(String msg) {

            }

            @Override
            public void e(String msg) {
                Assert.fail();
            }

            @Override
            public void onRuntimeException(RuntimeException e) {
                Assert.fail();
            }
        });
        L.d("AndroidGodEye-String");
        L.d(new Exception("AndroidGodEye-Exception"));
        L.d(this);
        L.d(null);
    }

    @Test
    public void d1() {
        L.setProxy(new L.LogProxy() {
            @Override
            public void d(String msg) {
                Assert.assertTrue(msg.contains("=>"));
                Assert.assertTrue(msg.contains("AndroidGodEye-String"));
                Assert.assertTrue(msg.contains("AndroidGodEye-Exception"));
                Assert.assertTrue(msg.contains(String.valueOf(LTest.this)));
            }

            @Override
            public void w(String msg) {

            }

            @Override
            public void e(String msg) {
                Assert.fail();
            }

            @Override
            public void onRuntimeException(RuntimeException e) {
                Assert.fail();
            }
        });
        L.d("%s=>%s=>%s", "AndroidGodEye-String", new Exception("AndroidGodEye-Exception"), this);
    }

    @Test
    public void e() {
        L.setProxy(new L.LogProxy() {
            @Override
            public void d(String msg) {
                Assert.fail();
            }

            @Override
            public void w(String msg) {

            }

            @Override
            public void e(String msg) {
                Assert.assertTrue(msg.contains("AndroidGodEye-Exception"));
            }

            @Override
            public void onRuntimeException(RuntimeException e) {
                Assert.fail();
            }
        });
        L.e(new Exception("AndroidGodEye-Exception"));
    }

    @Test
    public void onRuntimeException() {
        L.setProxy(new L.LogProxy() {
            @Override
            public void d(String msg) {
                Assert.fail();
            }

            @Override
            public void w(String msg) {

            }

            @Override
            public void e(String msg) {
                Assert.fail();
            }

            @Override
            public void onRuntimeException(RuntimeException e) {
                Assert.assertEquals("AndroidGodEye-Exception", e.getMessage());
            }
        });
        L.onRuntimeException(new RuntimeException("AndroidGodEye-Exception"));
    }
}
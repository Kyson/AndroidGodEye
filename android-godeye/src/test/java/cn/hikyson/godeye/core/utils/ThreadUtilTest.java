package cn.hikyson.godeye.core.utils;

import android.os.Build;
import android.os.Handler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import cn.hikyson.godeye.core.helper.RoboTestApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class ThreadUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isMainThread() {
        boolean isMainThread = ThreadUtil.isMainThread();
        assertTrue(isMainThread);
        final boolean[] isMainThreadInThread = {true};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                isMainThreadInThread[0] = ThreadUtil.isMainThread();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(isMainThreadInThread[0]);
    }

    @Test
    public void ensureMainOrWorkThread() {
        ThreadUtil.ensureMainThread();
        ThreadUtil.ensureMainThread("AndroidGodEye-Test");
        final int[] failCount = {0};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ThreadUtil.ensureMainThread();
                    failCount[0]++;
                } catch (IllegalStateException ignore) {
                }
                try {
                    ThreadUtil.ensureMainThread("AndroidGodEye-Test");
                    failCount[0]++;
                } catch (IllegalStateException ignore) {
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (failCount[0] > 0) {
            fail();
        }

        try {
            ThreadUtil.ensureWorkThread();
            fail();
        } catch (IllegalStateException ignore) {
        }
        try {
            ThreadUtil.ensureWorkThread("AndroidGodEye-Test");
            fail();
        } catch (IllegalStateException ignore) {
        }
        final int[] failCount2 = {0};

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ThreadUtil.ensureWorkThread();
                } catch (IllegalStateException ignore) {
                    failCount2[0]++;
                }
                try {
                    ThreadUtil.ensureWorkThread("AndroidGodEye-Test");
                } catch (IllegalStateException ignore) {
                    failCount2[0]++;
                }
            }
        });
        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (failCount2[0] > 0) {
            fail();
        }
    }

    @Test
    public void testHandlerThread() {
        assertNull(ThreadUtil.obtainHandler("AndroidGodEye-HandlerThread"));
        Handler handler = ThreadUtil.createIfNotExistHandler("AndroidGodEye-HandlerThread");
        assertNotNull(handler);
        Handler handler2 = ThreadUtil.obtainHandler("AndroidGodEye-HandlerThread");
        assertEquals(handler, handler2);
        ThreadUtil.destoryHandler("AndroidGodEye-HandlerThread");
        Handler handler3 = ThreadUtil.obtainHandler("AndroidGodEye-HandlerThread");
        assertNull(handler3);
    }


    @Test
    public void testHandlerThreadInMultiThread() {
        Thread thread0 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    Handler handler = ThreadUtil.createIfNotExistHandler("AndroidGodEye-HandlerThread" + i);
                    cn.hikyson.godeye.core.helper.ThreadUtil.sleep(1);
                    Handler handler2 = ThreadUtil.obtainHandler("AndroidGodEye-HandlerThread" + i);
                    cn.hikyson.godeye.core.helper.ThreadUtil.sleep(1);
                    ThreadUtil.destoryHandler("AndroidGodEye-HandlerThread" + i);
                    cn.hikyson.godeye.core.helper.ThreadUtil.sleep(1);
                }
            }
        });
        thread0.start();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    Handler handler = ThreadUtil.createIfNotExistHandler("AndroidGodEye-HandlerThread" + i);
                    cn.hikyson.godeye.core.helper.ThreadUtil.sleep(1);
                    Handler handler2 = ThreadUtil.obtainHandler("AndroidGodEye-HandlerThread" + i);
                    cn.hikyson.godeye.core.helper.ThreadUtil.sleep(1);
                    ThreadUtil.destoryHandler("AndroidGodEye-HandlerThread" + i);
                    cn.hikyson.godeye.core.helper.ThreadUtil.sleep(1);
                }
            }
        });
        thread1.start();
        try {
            thread0.join();
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 100; i++) {
            Handler handler = ThreadUtil.obtainHandler("AndroidGodEye-HandlerThread" + i);
            assertNull(handler);
        }
    }


}
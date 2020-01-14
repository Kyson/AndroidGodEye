package cn.hikyson.godeye.core.helper;

import android.os.Debug;
import android.os.SystemClock;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AndroidDebugTest {

    @Test
    public void isDebugging() throws InterruptedException {
        long startTime = SystemClock.elapsedRealtime();
        for (int i = 0; i < 1000; i++) {
            Debug.isDebuggerConnected();
        }
        assertTrue("Debug isDebuggerConnected cost too much.", (SystemClock.elapsedRealtime() - startTime) < 10);
    }
}
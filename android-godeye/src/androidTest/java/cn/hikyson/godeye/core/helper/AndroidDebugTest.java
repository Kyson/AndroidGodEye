package cn.hikyson.godeye.core.helper;

import android.os.Debug;

import org.junit.Test;

import static org.junit.Assert.*;

public class AndroidDebugTest {

    @Test
    public void isDebugging() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
//            Thread.sleep(10);
            boolean isDebuggerConnected = Debug.isDebuggerConnected();
        }
    }

    @Test
    public void setIsDebug() {
    }
}
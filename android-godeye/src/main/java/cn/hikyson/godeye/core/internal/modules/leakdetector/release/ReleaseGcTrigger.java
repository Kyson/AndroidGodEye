package cn.hikyson.godeye.core.internal.modules.leakdetector.release;

import com.squareup.leakcanary.GcTrigger;

import cn.hikyson.godeye.core.utils.L;


public class ReleaseGcTrigger implements GcTrigger {
    @Override
    public void runGc() {
        try {
            // Code taken from AOSP FinalizationTest:
            // https://android.googlesource.com/platform/libcore/+/master/support/src/test/java/libcore/
            // java/lang/ref/FinalizationTester.java
            // System.gc() does not garbage collect every time. Runtime.gc() is
            // more likely to perform a gc.
            Runtime.getRuntime().gc();
            enqueueReferences();
            System.runFinalization();
        } catch (Exception e) {
            L.d("GC failed: " + e.toString());
        }
    }

    private void enqueueReferences() {
        // Hack. We don't have a programmatic way to wait for the reference queue daemon to move
        // references to the appropriate queues.
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }
    }
}

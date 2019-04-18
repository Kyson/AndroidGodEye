package cn.hikyson.godeye.core.internal.modules.leakdetector.release;

import com.squareup.leakcanary.GcTrigger;

public class GodEyeReleaseGcTrigger implements GcTrigger {
    @Override
    public void runGc() {
        Runtime.getRuntime().gc();
        enqueueReferences();
        System.runFinalization();
    }

    private void enqueueReferences() {
        // Hack. We don't have a programmatic way to wait for the reference queue daemon to move
        // references to the appropriate queues.
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
    }
}

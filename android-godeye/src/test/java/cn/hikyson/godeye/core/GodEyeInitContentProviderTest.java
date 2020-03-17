package cn.hikyson.godeye.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class GodEyeInitContentProviderTest {

    @Test
    public void action() {
        GodEyeInitContentProvider godEyeInitContentProvider = new GodEyeInitContentProvider();
        try {
            godEyeInitContentProvider.delete(null, null, null);
            fail();
        } catch (UnsupportedOperationException ignore) {
        }
        try {
            godEyeInitContentProvider.getType(null);
            fail();
        } catch (UnsupportedOperationException ignore) {
        }
        try {
            godEyeInitContentProvider.insert(null, null);
            fail();
        } catch (UnsupportedOperationException ignore) {
        }
        try {
            godEyeInitContentProvider.query(null, null, null, null);
            fail();
        } catch (UnsupportedOperationException ignore) {
        }
        try {
            godEyeInitContentProvider.update(null, null, null, null);
            fail();
        } catch (UnsupportedOperationException ignore) {
        }
    }
}
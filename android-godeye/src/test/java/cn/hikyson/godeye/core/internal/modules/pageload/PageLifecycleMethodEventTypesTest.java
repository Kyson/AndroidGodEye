package cn.hikyson.godeye.core.internal.modules.pageload;

import android.os.Build;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.methodcanary.lib.MethodEvent;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class PageLifecycleMethodEventTypesTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isMatch() {
        Assert.assertTrue(PageLifecycleMethodEventTypes.isMatch(ActivityLifecycleEvent.ON_CREATE, ActivityLifecycleEvent.ON_CREATE));
        Assert.assertFalse(PageLifecycleMethodEventTypes.isMatch(FragmentLifecycleEvent.ON_CREATE, ActivityLifecycleEvent.ON_CREATE));
    }

    private MethodEvent mock(String methodName, String methodDesc) {
        return new MethodEvent("class0", 1, methodName, methodDesc, true, 0, 0);
    }

    @Test
    public void convert() {
        Assert.assertNull(PageLifecycleMethodEventTypes.convert(PageType.ACTIVITY, mock("class0", "desc0")));
        Assert.assertNull(PageLifecycleMethodEventTypes.convert(PageType.ACTIVITY, mock("onCreate", "(Landroid/os/Bundle;)")));
        Assert.assertEquals(ActivityLifecycleEvent.ON_CREATE, PageLifecycleMethodEventTypes.convert(PageType.ACTIVITY, mock("onCreate", "(Landroid/os/Bundle;)V")));
        Assert.assertEquals(FragmentLifecycleEvent.ON_CREATE, PageLifecycleMethodEventTypes.convert(PageType.FRAGMENT, mock("onCreate", "(Landroid/os/Bundle;)V")));
    }

    @Test
    public void equalMethodInfoForLifecycle() {
        PageLifecycleMethodEventTypes.MethodInfoForLifecycle methodInfoForLifecycle0 = new PageLifecycleMethodEventTypes.MethodInfoForLifecycle(PageType.ACTIVITY, "class0", "method0");
        PageLifecycleMethodEventTypes.MethodInfoForLifecycle methodInfoForLifecycle1 = new PageLifecycleMethodEventTypes.MethodInfoForLifecycle(PageType.ACTIVITY, "class0", "method0");
        Assert.assertTrue(methodInfoForLifecycle0.equals(methodInfoForLifecycle1));
        Assert.assertTrue(methodInfoForLifecycle0.equals(methodInfoForLifecycle0));
        Assert.assertFalse(methodInfoForLifecycle0.equals(null));
        Assert.assertNotNull(methodInfoForLifecycle0.toString());
    }

}
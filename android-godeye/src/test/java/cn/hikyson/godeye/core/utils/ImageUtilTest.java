package cn.hikyson.godeye.core.utils;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowBitmapFactory;

import cn.hikyson.godeye.core.helper.RoboTestApplication;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class ImageUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void convertToBase64() {
        String base642 = ImageUtil.convertToBase64(null, 5, 5);
        Assert.assertNull(base642);
        Bitmap bitmap = ShadowBitmapFactory.create("AndroidGodEye-Image", null, new Point(10, 10));
        String base64 = ImageUtil.convertToBase64(bitmap, 5, 5);
        Assert.assertNotNull(base64);
    }

    /**
     * width > target && height < target
     */
    @Test
    public void computeTargetSizeWidth1Height0() {
        Bitmap bitmap = ShadowBitmapFactory.create("AndroidGodEye", null, new Point(100, 20));
        int[] targetSize0 = ImageUtil.computeTargetSize(bitmap, 30, 30);
        Assert.assertEquals(30, targetSize0[0]);
        Assert.assertEquals(6, targetSize0[1]);
    }

    /**
     * width > target && height > target
     */
    @Test
    public void computeTargetSizeWidth1Height1() {
        Bitmap bitmap = ShadowBitmapFactory.create("AndroidGodEye", null, new Point(100, 50));
        int[] targetSize0 = ImageUtil.computeTargetSize(bitmap, 30, 30);
        Assert.assertEquals(30, targetSize0[0]);
        Assert.assertEquals(15, targetSize0[1]);
    }

    /**
     * width < target && height > target
     */
    @Test
    public void computeTargetSizeWidth0Height1() {
        Bitmap bitmap = ShadowBitmapFactory.create("AndroidGodEye", null, new Point(25, 50));
        int[] targetSize0 = ImageUtil.computeTargetSize(bitmap, 30, 30);
        Assert.assertEquals(15, targetSize0[0]);
        Assert.assertEquals(30, targetSize0[1]);
    }

    /**
     * width < target && height < target
     */
    @Test
    public void computeTargetSizeWidth0Height0() {
        Bitmap bitmap = ShadowBitmapFactory.create("AndroidGodEye", null, new Point(20, 10));
        int[] targetSize0 = ImageUtil.computeTargetSize(bitmap, 30, 30);
        Assert.assertEquals(20, targetSize0[0]);
        Assert.assertEquals(10, targetSize0[1]);
    }
}
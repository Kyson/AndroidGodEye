package cn.hikyson.godeye.core.internal.modules.imagecanary;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.helper.RoboTestApplication;
import cn.hikyson.godeye.core.helper.Test4ImageActivity;
import io.reactivex.functions.Predicate;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, application = RoboTestApplication.class)
public class ImageCanaryInternalTest {

    @Before
    public void setUp() throws Exception {
        GodEye.instance().init(ApplicationProvider.getApplicationContext());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void inspectInner() {
        ActivityController<Test4ImageActivity> activityController = Robolectric.buildActivity(Test4ImageActivity.class).create().start().resume();
        Test4ImageActivity activity = activityController.get();
        ImageCanaryInternal imageCanaryInternal = new ImageCanaryInternal(new DefaultImageCanaryConfigProvider());
        ImageCanary imageCanary = new ImageCanary();
        HashSet<ImageIssue> hashSet = new HashSet<ImageIssue>();
        // mock issue already exist
        ImageIssue imageIssue = new ImageIssue();
        imageIssue.activityHashCode = activity.hashCode();
        imageIssue.imageViewHashCode = activity.imageView3().hashCode();
        imageIssue.bitmapWidth = 200;
        imageIssue.bitmapHeight = 100;
        imageIssue.issueType = ImageIssue.IssueType.BITMAP_QUALITY_TOO_HIGH;
        hashSet.add(imageIssue);
        imageCanaryInternal.inspectInner(new WeakReference<>(activity), imageCanary, hashSet).run();
        List<ImageIssue> imageIssues = imageCanary.subject().test().values();
        imageCanary.subject().test().assertValueAt(0, new Predicate<ImageIssue>() {
            @Override
            public boolean test(ImageIssue imageIssue) throws Exception {
                return imageIssue.activityHashCode == activity.hashCode()
                        && imageIssue.bitmapWidth == 200
                        && imageIssue.bitmapHeight == 100
                        && imageIssue.imageViewWidth == 50
                        && imageIssue.imageViewHeight == 50
                        && imageIssue.issueType == ImageIssue.IssueType.BITMAP_QUALITY_TOO_HIGH;
            }
        }).assertValueAt(1, new Predicate<ImageIssue>() {
            @Override
            public boolean test(ImageIssue imageIssue) throws Exception {
                return imageIssue.activityHashCode == activity.hashCode()
                        && imageIssue.bitmapWidth == 200
                        && imageIssue.bitmapHeight == 100
                        && imageIssue.imageViewWidth == 500
                        && imageIssue.imageViewHeight == 500
                        && imageIssue.issueType == ImageIssue.IssueType.BITMAP_QUALITY_TOO_LOW;
            }
        }).assertValueCount(2);
        Assert.assertEquals(3, hashSet.size());
    }
}
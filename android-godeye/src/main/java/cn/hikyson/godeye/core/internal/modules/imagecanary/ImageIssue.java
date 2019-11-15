package cn.hikyson.godeye.core.internal.modules.imagecanary;

public class ImageIssue {

    public long timestamp;

    public enum IssueType {
        BITMAP_QUALITY_TOO_HIGH, BITMAP_QUALITY_TOO_LOW
    }
    public int imageViewId;
    public int bitmapWidth;
    public int bitmapHeight;
    public int imageViewWidth;
    public int imageViewHeight;
    public IssueType issueType;
    public String activityClassName;

    @Override
    public String toString() {
        return "ImageIssue{" +
                "imageViewId=" + imageViewId +
                ", bitmapWidth=" + bitmapWidth +
                ", bitmapHeight=" + bitmapHeight +
                ", imageViewWidth=" + imageViewWidth +
                ", imageViewHeight=" + imageViewHeight +
                ", issueType=" + issueType +
                ", activityClassName='" + activityClassName + '\'' +
                '}';
    }
}

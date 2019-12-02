package cn.hikyson.godeye.core.internal.modules.imagecanary;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class ImageIssue implements Serializable {

    @Keep
    public enum IssueType implements Serializable {
        BITMAP_QUALITY_TOO_HIGH, BITMAP_QUALITY_TOO_LOW
    }

    public long timestamp;
    public String activityClassName;
    public int activityHashCode;
    public int imageViewHashCode;
    public int bitmapWidth;
    public int bitmapHeight;
    public int imageViewWidth;
    public int imageViewHeight;
    public IssueType issueType;

    @Override
    public String toString() {
        return "ImageIssue{" +
                "timestamp=" + timestamp +
                ", activityClassName='" + activityClassName + '\'' +
                ", activityHashCode='" + activityHashCode + '\'' +
                ", imageViewHashCode=" + imageViewHashCode +
                ", bitmapWidth=" + bitmapWidth +
                ", bitmapHeight=" + bitmapHeight +
                ", imageViewWidth=" + imageViewWidth +
                ", imageViewHeight=" + imageViewHeight +
                ", issueType=" + issueType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageIssue that = (ImageIssue) o;

        if (activityHashCode != that.activityHashCode) return false;
        if (imageViewHashCode != that.imageViewHashCode) return false;
        if (bitmapWidth != that.bitmapWidth) return false;
        if (bitmapHeight != that.bitmapHeight) return false;
        if (imageViewWidth != that.imageViewWidth) return false;
        if (imageViewHeight != that.imageViewHeight) return false;
        if (activityClassName != null ? !activityClassName.equals(that.activityClassName) : that.activityClassName != null)
            return false;
        return issueType == that.issueType;
    }

    @Override
    public int hashCode() {
        int result = activityClassName != null ? activityClassName.hashCode() : 0;
        result = 31 * result + activityHashCode;
        result = 31 * result + imageViewHashCode;
        result = 31 * result + bitmapWidth;
        result = 31 * result + bitmapHeight;
        result = 31 * result + imageViewWidth;
        result = 31 * result + imageViewHeight;
        result = 31 * result + (issueType != null ? issueType.hashCode() : 0);
        return result;
    }
}

package cn.hikyson.godeye.core.internal.modules.imagecanary;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class ImageIssue implements Serializable {

    @Keep
    public enum IssueType implements Serializable {
        BITMAP_QUALITY_TOO_HIGH, BITMAP_QUALITY_TOO_LOW, INVISIBLE_BUT_MEMORY_OCCUPIED, NONE
    }

    public long timestamp;
    public String activityClassName;
    public int activityHashCode;
    public int imageViewHashCode;
    public int bitmapWidth;
    public int bitmapHeight;
    public int imageViewWidth;
    public int imageViewHeight;
    public String imageSrcBase64;
    public IssueType issueType;

    public ImageIssue() {
    }

    public ImageIssue(ImageIssue imageIssue) {
        this.timestamp = imageIssue.timestamp;
        this.activityClassName = imageIssue.activityClassName;
        this.activityHashCode = imageIssue.activityHashCode;
        this.imageViewHashCode = imageIssue.imageViewHashCode;
        this.bitmapWidth = imageIssue.bitmapWidth;
        this.bitmapHeight = imageIssue.bitmapHeight;
        this.imageViewWidth = imageIssue.imageViewWidth;
        this.imageViewHeight = imageIssue.imageViewHeight;
        this.imageSrcBase64 = imageIssue.imageSrcBase64;
        this.issueType = imageIssue.issueType;
    }

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
        return issueType == that.issueType;
    }

    @Override
    public int hashCode() {
        int result = activityHashCode;
        result = 31 * result + imageViewHashCode;
        result = 31 * result + bitmapWidth;
        result = 31 * result + bitmapHeight;
        result = 31 * result + (issueType != null ? issueType.hashCode() : 0);
        return result;
    }
}

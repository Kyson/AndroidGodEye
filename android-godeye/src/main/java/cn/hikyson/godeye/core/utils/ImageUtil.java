package cn.hikyson.godeye.core.utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Base64;

import androidx.annotation.VisibleForTesting;

import java.io.ByteArrayOutputStream;

public class ImageUtil {
    public static String convertToBase64(Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap == null) {
            return null;
        }
        long startTime = System.currentTimeMillis();
        int[] targetSize = computeTargetSize(bitmap, maxWidth, maxHeight);
        // 10-100ms量级
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, targetSize[0], targetSize[1]);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String result = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        L.d("ImageUtil.convertToBase64 cost %s ms", (System.currentTimeMillis() - startTime));
        return result;
    }

    @VisibleForTesting
    static int[] computeTargetSize(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float targetWidth = width;
        float targetHeight = height;
        if (width > maxWidth || height > maxHeight) {
            float scaleWidth = (width * 1.0f / maxWidth);
            float scaleHeight = (height * 1.0f / maxHeight);
            float scale = Math.max(scaleWidth, scaleHeight);
            targetWidth = width / scale;
            targetHeight = height / scale;
        }
        return new int[]{(int) targetWidth, (int) targetHeight};
    }
}

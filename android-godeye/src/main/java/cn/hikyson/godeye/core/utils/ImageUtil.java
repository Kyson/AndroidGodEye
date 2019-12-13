package cn.hikyson.godeye.core.utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtil {
    public static String convertToBase64(Bitmap bitmap, int maxHeight, int maxWidth) {
        if (bitmap == null) {
            return null;
        }
        long startTime = System.currentTimeMillis();
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float targetHeight = height;
        float targetWidth = width;
        if (height > maxHeight || width > maxWidth) {
            float scaleHeight = (height * 1.0f / maxHeight);
            float scaleWidth = (width * 1.0f / maxWidth);
            float scale = Math.max(scaleHeight, scaleWidth);
            targetHeight = height / scale;
            targetWidth = width / scale;
        }
        // 10-100ms量级
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, (int) targetWidth, (int) targetHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String result = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        L.d("ImageUtil.convertToBase64 cost %s ms", (System.currentTimeMillis() - startTime));
        return result;
    }
}

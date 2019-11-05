package cn.hikyson.godeye.core.internal.modules.viewcanary;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewBgUtil {

    public static int getLayer(View view) {
        // 不可见的即为没有背景
        if (view == null || view.getVisibility() != View.VISIBLE) {
            return 0;
        }
        int layer = 0;
        // 没有文字也为没有背景
        if (view instanceof TextView) {
            if (!TextUtils.isEmpty(((TextView) view).getText())) {
                layer ++;
            }
        }
        if (view instanceof ImageView) {
            if (((ImageView) view).getDrawable() != null) {
                layer ++;
            }
        }
        Drawable background = view.getBackground();
        if (background != null) {
            Drawable current = background.getCurrent();
            if (current == null) {
                // may be null e.g. StateListDrawable
                return layer;
            }
            if (current instanceof ColorDrawable) {
                int color = ((ColorDrawable) current).getColor();
                // 有背景且背景为透明
                if (color != 0) {
                    layer ++;
                    return layer;
                }
            }
            Bitmap bitmap = drawableToBitmap(current);
            if (!isBitmapTransparent(bitmap)) {
                layer ++;
                return layer;
            }
        }
        return layer;
    }

    private static boolean isBitmapTransparent(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        boolean isTransparent = true;

        for (int x = width / 4 / 2; x < width; x += width / 4) {
            for (int y= height / 4 / 2; y < height; y += height / 4) {
                if (checkPixelNotTransparency(bitmap, x, y)) {
                    isTransparent = false;
                    break;
                }
            }
        }
        return isTransparent;
    }

    private static boolean checkPixelNotTransparency(Bitmap bitmap, int x, int y) {
        return ((bitmap.getPixel(x, y) & 0xff000000) >> 24) != 0;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}

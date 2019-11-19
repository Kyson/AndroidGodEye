package cn.hikyson.godeye.core.internal.modules.imagecanary;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class DefaultBitmapInfoAnalyzer implements BitmapInfoAnalyzer {
    @Override
    public BitmapInfo analyze(ImageView imageView) {
        BitmapInfo bitmapInfo = new BitmapInfo();
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            bitmapInfo.bitmapWidth = bitmap.getWidth();
            bitmapInfo.bitmapHeight = bitmap.getHeight();
        }
        return bitmapInfo;
    }
}

package cn.hikyson.godeye.core.internal.modules.imagecanary;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Keep;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Keep
public class DefaultBitmapInfoAnalyzer implements BitmapInfoAnalyzer {

    @Override
    public List<BitmapInfo> analyze(View view) {
        BitmapInfo ivBitMapInfo = null;
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                ivBitMapInfo = new BitmapInfo();
                ivBitMapInfo.bitmapWidth = bitmap.getWidth();
                ivBitMapInfo.bitmapHeight = bitmap.getHeight();
                ivBitMapInfo.bitmap = new WeakReference<>(bitmap);
            }
        }
        BitmapInfo vBitMapInfo;
        Drawable drawable = view.getBackground();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            vBitMapInfo = new BitmapInfo();
            vBitMapInfo.bitmapWidth = bitmap.getWidth();
            vBitMapInfo.bitmapHeight = bitmap.getHeight();
            vBitMapInfo.bitmap = new WeakReference<>(bitmap);
            if (ivBitMapInfo != null) {
                return Arrays.asList(ivBitMapInfo, vBitMapInfo);
            }
            return Arrays.asList(vBitMapInfo);
        }
        if (ivBitMapInfo != null) {
            return Arrays.asList(ivBitMapInfo);
        }
        return Collections.emptyList();
    }
}

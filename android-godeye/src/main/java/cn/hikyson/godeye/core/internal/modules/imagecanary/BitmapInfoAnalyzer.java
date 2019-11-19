package cn.hikyson.godeye.core.internal.modules.imagecanary;

import android.widget.ImageView;

import androidx.annotation.Keep;

import io.reactivex.annotations.NonNull;

@Keep
public interface BitmapInfoAnalyzer {
    @NonNull
    BitmapInfo analyze(ImageView imageView);
}

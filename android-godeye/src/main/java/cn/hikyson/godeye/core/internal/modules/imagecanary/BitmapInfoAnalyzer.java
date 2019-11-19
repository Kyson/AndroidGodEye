package cn.hikyson.godeye.core.internal.modules.imagecanary;

import android.widget.ImageView;

import io.reactivex.annotations.NonNull;

public interface BitmapInfoAnalyzer {
    @NonNull
    BitmapInfo analyze(ImageView imageView);
}

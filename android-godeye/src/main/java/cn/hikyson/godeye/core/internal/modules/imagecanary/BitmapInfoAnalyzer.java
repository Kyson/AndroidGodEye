package cn.hikyson.godeye.core.internal.modules.imagecanary;

import android.view.View;

import androidx.annotation.Keep;

import java.util.List;

import io.reactivex.annotations.NonNull;

@Keep
public interface BitmapInfoAnalyzer {
    @NonNull
    List<BitmapInfo> analyze(View view);
}

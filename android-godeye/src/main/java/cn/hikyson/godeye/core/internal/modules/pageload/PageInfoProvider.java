package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Map;

@Keep
public interface PageInfoProvider {
    @Nullable
    Map<String, String> getInfoByActivity(Activity activity);

    @Nullable
    Map<String, String> getInfoByV4Fragment(Fragment fragment);

    @Nullable
    Map<String, String> getInfoByFragment(android.app.Fragment fragment);
}

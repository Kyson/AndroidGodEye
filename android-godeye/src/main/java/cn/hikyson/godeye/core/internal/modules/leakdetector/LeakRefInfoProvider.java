package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Activity;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * @deprecated use {@link Leak } instead
 */
@Deprecated
@Keep
public interface LeakRefInfoProvider {
    @NonNull
    LeakRefInfo getInfoByActivity(Activity activity);
    @NonNull
    LeakRefInfo getInfoByV4Fragment(Fragment fragment);
    @NonNull
    LeakRefInfo getInfoByFragment(android.app.Fragment fragment);
}

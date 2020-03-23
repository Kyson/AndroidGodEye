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
public class DefaultLeakRefInfoProvider implements LeakRefInfoProvider {

    @NonNull
    @Override
    public LeakRefInfo getInfoByActivity(Activity activity) {
        return new LeakRefInfo(false, null);
    }

    @NonNull
    @Override
    public LeakRefInfo getInfoByV4Fragment(Fragment fragment) {
        return new LeakRefInfo(false, null);
    }

    @NonNull
    @Override
    public LeakRefInfo getInfoByFragment(android.app.Fragment fragment) {
        return new LeakRefInfo(false, null);
    }
}

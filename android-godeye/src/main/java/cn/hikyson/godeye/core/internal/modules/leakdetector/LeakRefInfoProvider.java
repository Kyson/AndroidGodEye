package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Activity;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

@Keep
public interface LeakRefInfoProvider {
    @NonNull
    LeakRefInfo getInfoByActivity(Activity activity);
    @NonNull
    LeakRefInfo getInfoByV4Fragment(Fragment fragment);
    @NonNull
    LeakRefInfo getInfoByFragment(android.app.Fragment fragment);
}

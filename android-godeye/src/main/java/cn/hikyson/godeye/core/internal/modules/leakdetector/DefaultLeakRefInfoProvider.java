package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Activity;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

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

package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Activity;
import android.support.v4.app.Fragment;

import cn.hikyson.godeye.core.internal.modules.leakdetector.release.LeakRefNameProvider;

public class DefaultLeakRefNameProvider implements LeakRefNameProvider {

    @Override
    public String convertActivity(Activity activity) {
        return activity.getClass().getSimpleName();
    }

    @Override
    public String convertV4Fragment(Fragment fragment) {
        return fragment.getClass().getSimpleName();
    }

    @Override
    public String convertFragment(android.app.Fragment fragment) {
        return fragment.getClass().getSimpleName();
    }
}

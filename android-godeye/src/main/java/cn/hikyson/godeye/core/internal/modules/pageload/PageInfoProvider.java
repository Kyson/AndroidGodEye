package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

@Keep
public interface PageInfoProvider {
    @NonNull
    PageInfo<Activity> getInfoByActivity(Activity activity);

    @NonNull
    PageInfo<Fragment> getInfoByV4Fragment(Fragment fragment);

    @NonNull
    PageInfo<android.app.Fragment> getInfoByFragment(android.app.Fragment fragment);
}

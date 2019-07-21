package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

@Keep
public interface PageInfoProvider {
    @Nullable
    PageInfo<Activity> getInfoByActivity(Activity activity);

    @Nullable
    PageInfo<Fragment> getInfoByV4Fragment(Fragment fragment);

    @Nullable
    PageInfo<android.app.Fragment> getInfoByFragment(android.app.Fragment fragment);
}

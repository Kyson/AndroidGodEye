package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

@Keep
public interface PageInfoProvider {
    @NonNull
    PageInfo getInfoByActivity(Activity activity);
    @NonNull
    PageInfo getInfoByV4Fragment(Fragment fragment);
    @NonNull
    PageInfo getInfoByFragment(android.app.Fragment fragment);
}

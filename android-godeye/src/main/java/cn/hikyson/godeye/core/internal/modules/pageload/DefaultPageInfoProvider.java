package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

@Keep
public class DefaultPageInfoProvider implements PageInfoProvider {

    @NonNull
    @Override
    public PageInfo getInfoByActivity(Activity activity) {
        return new PageInfo<>(activity, null);
    }

    @NonNull
    @Override
    public PageInfo getInfoByV4Fragment(Fragment fragment) {
        return new PageInfo<>(fragment, null);
    }

    @NonNull
    @Override
    public PageInfo getInfoByFragment(android.app.Fragment fragment) {
        return new PageInfo<>(fragment, null);
    }
}

package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;

import java.util.Map;

@Keep
public class DefaultPageInfoProvider implements PageInfoProvider {

    @Override
    public Map<String, String> getInfoByActivity(Activity activity) {
        return null;
    }

    @Override
    public Map<String, String> getInfoByV4Fragment(Fragment fragment) {
        return null;
    }

    @Override
    public Map<String, String> getInfoByFragment(android.app.Fragment fragment) {
        return null;
    }
}

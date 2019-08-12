package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

@Keep
public class DefaultPageInfoProvider implements PageInfoProvider {

    @Override
    public Map<String, String> getInfoByActivity(Activity activity) {
        Map<String, String> map = new HashMap<>();
        map.put("pageId", activity.getClass().getSimpleName());
        return map;
    }

    @Override
    public Map<String, String> getInfoByV4Fragment(Fragment fragment) {
        Map<String, String> map = new HashMap<>();
        map.put("pageId", fragment.getClass().getSimpleName());
        return map;
    }

    @Override
    public Map<String, String> getInfoByFragment(android.app.Fragment fragment) {
        Map<String, String> map = new HashMap<>();
        map.put("pageId", fragment.getClass().getSimpleName());
        return map;
    }
}

package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Activity;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

@Keep
public class DefaultLeakRefInfoProvider implements LeakRefInfoProvider {

    @NonNull
    @Override
    public LeakRefInfo getInfoByActivity(Activity activity) {
        Map<String, String> map = new HashMap<>();
        map.put("pageId", activity.getClass().getSimpleName());
        return new LeakRefInfo(false, map);
    }

    @NonNull
    @Override
    public LeakRefInfo getInfoByV4Fragment(Fragment fragment) {
        Map<String, String> map = new HashMap<>();
        map.put("pageId", fragment.getClass().getSimpleName());
        return new LeakRefInfo(false, map);
    }

    @NonNull
    @Override
    public LeakRefInfo getInfoByFragment(android.app.Fragment fragment) {
        Map<String, String> map = new HashMap<>();
        map.put("pageId", fragment.getClass().getSimpleName());
        return new LeakRefInfo(false, map);
    }
}

package cn.hikyson.godeye.core;


import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pair;

import java.util.Map;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakDetector;
import cn.hikyson.godeye.core.internal.modules.sm.Sm;

/**
 * 入口
 * install -> module.subject() -> uninstall
 * Created by kysonchao on 2017/11/22.
 */
public class GodEye {

    private GodEye() {
    }

    private static class InstanceHolder {
        private static final GodEye sInstance = new GodEye();
    }

    public static GodEye instance() {
        return InstanceHolder.sInstance;
    }

    public final <T> GodEye install(Class<? extends Install<T>> clz, T config) {
        getModule(clz).install(config);
        return this;
    }

    @SafeVarargs
    public final <T> GodEye install(Pair<Class<? extends Install<T>>, T>... pairs) {
        for (Pair<Class<? extends Install<T>>, T> p : pairs) {
            install(p.first, p.second);
        }
        return this;
    }

    private Map<Class, Object> mCachedModules = new ArrayMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getModule(Class<T> clz) {
        Object module = mCachedModules.get(clz);
        if (module != null) {
            if (!clz.isInstance(module)) {
                throw new IllegalStateException(clz.getName() + " must be instance of " + String.valueOf(module));
            }
            return (T) module;
        }
        try {
            T createdModule;
            if (LeakDetector.class.equals(clz)) {
                createdModule = (T) LeakDetector.instance();
            } else if (Sm.class.equals(clz)) {
                createdModule = (T) Sm.instance();
            } else {
                createdModule = clz.newInstance();
            }
            mCachedModules.put(clz, createdModule);
            return createdModule;
        } catch (Throwable e) {
            throw new IllegalStateException("Can not create instance of " + clz.getName() + ", " + String.valueOf(e));
        }
    }
}

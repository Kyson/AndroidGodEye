package cn.hikyson.godeye.monitor.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by kysonchao on 2017/9/3.
 */
public class GsonUtil {
    private static Gson sGson = new GsonBuilder().create();

    public static String toJson(Object t) {
        return sGson.toJson(t);
    }

    public static <T> T fromJson(String json, Class<T> clz) {
        return sGson.fromJson(json, clz);
    }
}

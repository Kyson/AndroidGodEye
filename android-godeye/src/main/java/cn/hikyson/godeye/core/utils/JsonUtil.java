package cn.hikyson.godeye.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Created by kysonchao on 2017/11/22.
 */

public class JsonUtil {
    public static Gson sGson = new GsonBuilder().create();

    private JsonUtil() {
    }

    public static String toJson(Object o) {
        return sGson.toJson(o);
    }
}

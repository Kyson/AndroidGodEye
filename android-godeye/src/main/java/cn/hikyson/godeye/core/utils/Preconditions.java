package cn.hikyson.godeye.core.utils;

/**
 * Created by kysonchao on 2017/12/19.
 */
public class Preconditions {
    public static <T> T checkNotNull(T instance) {
        if (instance == null) {
            throw new NullPointerException("must not be null");
        } else {
            return instance;
        }
    }
}

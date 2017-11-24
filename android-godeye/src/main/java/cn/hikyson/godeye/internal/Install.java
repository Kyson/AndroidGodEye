package cn.hikyson.godeye.internal;

/**
 * Created by kysonchao on 2017/11/24.
 */
public interface Install<T> {
    void install(T config);

    void uninstall();
}

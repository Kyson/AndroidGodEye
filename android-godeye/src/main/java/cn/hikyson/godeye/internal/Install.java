package cn.hikyson.godeye.internal;


import android.content.Context;

/**
 * 安装之后内部自动生产数据类型
 * Created by kysonchao on 2017/11/21.
 */
public interface Install<T> {
    void install(Context context, T config) throws Throwable;

    void uninstall(Context context) throws Throwable;
}

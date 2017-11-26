package cn.hikyson.godeye.monitor.utils;

import java.io.Closeable;

/**
 * Created by kysonchao on 2017/9/3.
 */
public class IoUtil {

    public static void closeSilent(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Throwable e) {
        }
    }
}

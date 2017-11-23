package cn.hikyson.godeye.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by kysonchao on 2017/11/22.
 */

public class IoUtil {
    public static final int DEFAULT_BUFFER_SIZE = 32768;

    private IoUtil() {
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException var2) {
            }
        }
    }
}

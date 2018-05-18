package cn.hikyson.godeye.monitor.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

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

    public static String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append("\n");
            sb.append(line);
        }
        return sb.toString();
    }
}

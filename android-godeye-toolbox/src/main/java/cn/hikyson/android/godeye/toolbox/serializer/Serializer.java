package cn.hikyson.android.godeye.toolbox.serializer;

import java.io.Reader;

/**
 * Created by kysonchao on 2017/12/19.
 */
public interface Serializer {
    String serialize(Object o);

    <T> T deserialize(Reader reader, Class<T> clz);
}

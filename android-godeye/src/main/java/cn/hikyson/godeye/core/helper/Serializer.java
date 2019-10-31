package cn.hikyson.godeye.core.helper;

import java.io.Reader;
import java.lang.reflect.Type;

/**
 * Created by kysonchao on 2017/12/19.
 */
public interface Serializer {
    String serialize(Object o);

    <T> T deserialize(Reader reader, Type type);
}

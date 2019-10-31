package cn.hikyson.godeye.core.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.lang.reflect.Type;

/**
 * Created by kysonchao on 2018/1/19.
 */
public class GsonSerializer implements Serializer {
    private Gson mGson;

    public GsonSerializer() {
        mGson = new GsonBuilder().create();
    }

    @Override
    public String serialize(Object o) {
        return mGson.toJson(o);
    }

    @Override
    public <T> T deserialize(Reader reader, Type type) {
        return mGson.fromJson(reader, type);
    }
}

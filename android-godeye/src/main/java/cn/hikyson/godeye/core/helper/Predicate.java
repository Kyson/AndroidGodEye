package cn.hikyson.godeye.core.helper;

import androidx.annotation.NonNull;

public interface Predicate<T> {
    boolean test(@NonNull T t) throws Exception;
}

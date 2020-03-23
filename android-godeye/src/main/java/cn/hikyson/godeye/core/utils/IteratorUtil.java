package cn.hikyson.godeye.core.utils;

import androidx.core.util.Consumer;

import java.util.Iterator;

public class IteratorUtil {
    public static <T> void forEach(Iterator<T> iterator, Consumer<T> action) {
        while (iterator.hasNext()) {
            action.accept(iterator.next());
        }
    }
}

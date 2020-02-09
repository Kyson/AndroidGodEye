package cn.hikyson.godeye.core.utils;


import androidx.annotation.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

public class ReflectUtil {

    /**
     * 根据类名获取类.
     *
     * @param clsName
     * @return
     */
    @Nullable
    private static Class<?> getClass(String clsName) {
        try {
            return Class.forName(clsName);
        } catch (ClassNotFoundException e) {
            L.e("ReflectUtil: " + e.toString());
            return null;
        }
    }

    /**
     * 获取带参函数.
     *
     * @param cls
     * @param methodName
     * @param types
     * @return
     */
    @Nullable
    private static Method getMethod(Class<?> cls, String methodName, Class<?>... types) {
        try {
            Method method = cls.getDeclaredMethod(methodName, types);
            if (!Modifier.isPublic(method.getModifiers())) {
                method.setAccessible(true);
            }
            return method;
        } catch (Exception e) {
            L.e("ReflectUtil: " + e.toString());
            return null;
        }
    }

    /**
     * 获取类的方法，如果异常会抛出来
     *
     * @param cls
     * @param methodName
     * @param types
     * @return
     * @throws NoSuchMethodException
     */
    private static Method getMethodUnSafe(Class<?> cls, String methodName, Class<?>... types)
            throws NoSuchMethodException {
        Method method = cls.getDeclaredMethod(methodName, types);
        if (!Modifier.isPublic(method.getModifiers())) {
            method.setAccessible(true);
        }
        return method;
    }

    public static <T> T invokeStaticMethod(String clsName, String methodName, Class<?>[] argsTypes, Object[] args) {
        return invokeStaticMethod(getClass(clsName), methodName, argsTypes, args);
    }

    /**
     * 静态方法调用.
     *
     * @param cls
     * @param methodName
     * @param argsTypes
     * @param args
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T invokeStaticMethod(Class<?> cls, String methodName, Class<?>[] argsTypes, Object[] args) {
        try {
            final Method method = getMethod(cls, methodName, argsTypes);
            if (method != null) {
                return (T) method.invoke(null, args);
            }
        } catch (Exception e) {
            L.e("ReflectUtil: " + e.toString());
        }
        return null;
    }

    public static <T> T invokeStaticMethodUnSafe(String className, String methodName, Class<?>[] argsTypes, Object[] args)
            throws Exception {
        final Method method = getMethodUnSafe(Objects.requireNonNull(getClass(className)), methodName, argsTypes);
        return (T) method.invoke(null, args);
    }
}

package cn.hikyson.godeye.core.utils;


import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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
    public static Class<?> getClass(String clsName) {
        try {
            return Class.forName(clsName);
        } catch (ClassNotFoundException e) {
            L.e("ReflectUtil: " + e.toString());
            return null;
        }
    }

    /**
     * 根据指定类名创建对象(无参构造)
     *
     * @param clsName
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T getTarget(String clsName) {
        try {
            return getTarget(clsName, new Object[]{});
        } catch (Exception e) {
            L.e("ReflectUtil: " + e.toString());
            return null;
        }
    }

    public static <T> T getTarget(Class<T> cls) {
        return getTarget(cls, new Object[]{});
    }

    /**
     * 根据指定类名创建对象(带参构造)
     *
     * @param clsName
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T getTarget(String clsName, Object... args) {
        return (T) getTarget(getClass(clsName), args);
    }

    /**
     * 根据指定类创建对象(带参)
     *
     * @param cls
     * @param args
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T getTarget(Class<T> cls, Object... args) {
        if (cls == null) {
            return null;
        }

        Class<?>[] argsTypes = null;

        if (args != null && args.length > 0) {
            final int argsCount = args.length;
            argsTypes = new Class<?>[argsCount];
            for (int i = 0; i < argsCount; i++) {
                argsTypes[i] = args[i].getClass();
            }
        }

        return getTarget(cls, args, argsTypes);
    }

    /**
     * 根据指定类创建对象(带参以及参数类型)
     *
     * @param cls
     * @param args      参数列表
     * @param argsTypes 参数类型列表
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T getTarget(Class<T> cls, Object[] args, @Nullable Class<?>[] argsTypes) {
        if (cls == null) {
            return null;
        }

        if (args == null || args.length == 0) {
            try {
                return cls.newInstance();
            } catch (Exception e) {
                L.e("ReflectUtil: " + e.toString());
                return null;
            }
        }

        if (argsTypes == null) {
            return null;
        }

        if (args.length != argsTypes.length) {
            return null;
        }

        try {
            Constructor<T> constructor = cls.getDeclaredConstructor(argsTypes);
            return constructor.newInstance(args);
        } catch (Exception e) {
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
    public static Method getMethod(Class<?> cls, String methodName, Class<?>... types) {
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
    public static Method getMethodUnSafe(Class<?> cls, String methodName, Class<?>... types)
            throws NoSuchMethodException {
        Method method = cls.getDeclaredMethod(methodName, types);
        if (!Modifier.isPublic(method.getModifiers())) {
            method.setAccessible(true);
        }
        return method;
    }

    /**
     * 获取无参函数.
     *
     * @param cls
     * @param methodName
     * @return
     */
    @Nullable
    public static Method getMethod(Class<?> cls, String methodName) {
        try {
            Method method = cls.getDeclaredMethod(methodName);
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
     * 获取类成员变量.
     *
     * @param cls
     * @param fieldName
     * @return
     */
    @Nullable
    public static Field getField(Class<?> cls, String fieldName) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
            }
            return field;
        } catch (Exception e) {
            L.e("ReflectUtil: " + e.toString());
            return null;
        }
    }

    /**
     * 调用无参函数.
     *
     * @param target
     * @param methodName
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T invokeMethod(Object target, String methodName) {
        try {
            final Method method = getMethod(target.getClass(), methodName);
            if (method != null) {
                return (T) method.invoke(target);
            }
        } catch (Exception e) {
            L.e("ReflectUtil: " + e.toString());
        }
        return null;
    }

    /**
     * 调用带参函数,不精确
     *
     * @param target
     * @param methodName
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T invokeMethod(Object target, String methodName, Object... args) {
        final int argsCount = args.length;
        final Class<?>[] argsTypes = new Class<?>[argsCount];
        for (int i = 0; i < argsCount; i++) {
            argsTypes[i] = args[i].getClass();
        }

        return invokeMethod(target, methodName, argsTypes, args);
    }

    /**
     * 调用带参函数
     *
     * @param target
     * @param methodName
     * @param args
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T invokeMethod(Object target, String methodName, Class<?>[] argsTypes, Object[] args) {
        try {
            final Method method = getMethod(target.getClass(), methodName, argsTypes);
            if (method != null) {
                return (T) method.invoke(target, args);
            }
        } catch (Exception e) {
            L.e("ReflectUtil: " + e.toString());
        }
        return null;
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

    /**
     * 调用静态方法，如果异常会抛出来
     *
     * @param cls
     * @param methodName
     * @param argsTypes
     * @param args
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T invokeStaticMethodUnSafe(Class<?> cls, String methodName, Class<?>[] argsTypes, Object[] args)
            throws Exception {
        final Method method = getMethodUnSafe(cls, methodName, argsTypes);
        return (T) method.invoke(null, args);
    }

    /**
     * 获取指定变量值.
     *
     * @param target
     * @param fieldName
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T getFieldValue(Object target, String fieldName) {
        try {
            Field field = getField(target.getClass(), fieldName);
            if (field == null) {
                return null;
            }
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
            }
            return (T) field.get(target);
        } catch (Exception e) {
            L.e("ReflectUtil: " + e.toString());
            return null;
        }
    }

    /**
     * 对指定变量赋值.
     *
     * @param target
     * @param fieldName
     * @param value
     * @return
     */
    public static boolean setFieldValue(Object target, String fieldName, Object value) {
        final Field field = getField(target.getClass(), fieldName);
        if (field == null) {
            return false;
        }
        try {
            field.set(target, value);
            return true;
        } catch (IllegalAccessException e) {
            L.e("ReflectUtil: " + e.toString());
        }
        return false;
    }
}

package world.xuewei.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 断言判定
 *
 * @author XUEW
 */
public class Assert {

    public static boolean isEmpty(CharSequence s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        for (int i = 0; i < s.length(); ++i) {
            if (' ' != s.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(Collection<?> obj) {
        return obj == null || obj.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> obj) {
        return obj == null || obj.isEmpty();
    }

    public static boolean isEmpty(Object[] obj) {
        return obj == null || obj.length == 0;
    }

    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    public static boolean isEmpty(List<?> obj) {
        return obj == null || obj.size() == 0;
    }

    public static boolean notEmpty(CharSequence s) {
        return !isEmpty(s);
    }

    public static boolean notEmpty(Collection<?> obj) {
        return !isEmpty(obj);
    }

    public static boolean notEmpty(Map<?, ?> obj) {
        return !isEmpty(obj);
    }

    public static boolean notEmpty(Object[] obj) {
        return !isEmpty(obj);
    }

    public static boolean notEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean notEmpty(List<?> obj) {
        return !isEmpty(obj);
    }

    public static void assertNotEmpty(CharSequence s, String name) {
        if (isEmpty(s)) {
            throwException(name);
        }
    }

    public static void assertNotEmpty(Collection<?> obj, String name) {
        if (isEmpty(obj)) {
            throwException(name);
        }
    }

    public static void assertNotEmpty(Map<?, ?> obj, String name) {
        if (isEmpty(obj)) {
            throwException(name);
        }
    }

    public static void assertNotEmpty(Object[] obj, String name) {
        if (isEmpty(obj)) {
            throwException(name);
        }
    }

    public static void assertNotEmpty(Object obj, String name) {
        if (isEmpty(obj)) {
            throwException(name);
        }
    }

    public static void assertNotEmpty(List<?> obj, String name) {
        if (isEmpty(obj)) {
            throwException(name);
        }
    }

    private static String throwException(String name) {
        throw new RuntimeException("REQUEST_PARAM_IS_NULL 请求参数<" + name + ">为空");
    }

}

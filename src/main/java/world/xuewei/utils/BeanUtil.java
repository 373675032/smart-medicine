package world.xuewei.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import java.util.*;

/**
 * Bean 工具
 *
 * @author XUEW
 */
public class BeanUtil {

    private static Map<String, BeanCopier> beanCopierMap = new HashMap();


    public static <T> T copy(Object src, Class<T> clazz)
            throws InstantiationException, IllegalAccessException {
        if ((null == src) || (null == clazz)) return null;
        Object des = clazz.newInstance();
        copy(src, des);
        return (T) des;
    }


    public static void copy(Object src, Object des) {
        if ((null == src) || (null == des)) return;
        String key = generateKey(src.getClass(), des.getClass());
        BeanCopier copier = (BeanCopier) beanCopierMap.get(key);
        if (null == copier) {
            copier = BeanCopier.create(src.getClass(), des.getClass(), false);
            beanCopierMap.put(key, copier);
        }
        copier.copy(src, des, null);
    }


    public static <T> T map2Bean(Map<String, Object> map, Class<T> clazz)
            throws InstantiationException, IllegalAccessException {
        if ((null == map) || (null == clazz)) return null;
        T bean = clazz.newInstance();
        map2Bean(map, bean);
        return bean;
    }


    public static <T> void map2Bean(Map<String, Object> map, T bean) {
        if ((null == map) || (null == bean)) return;
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
    }


    public static Map<String, Object> bean2Map(Object bean) {
        if (null == bean) return null;
        return copy(BeanMap.create(bean));
    }


    public static <T> List<Map<String, Object>> mapList(List<T> beanList) {
        if ((null == beanList) || (beanList.size() < 1)) return null;
        List<Map<String, Object>> mapList = new ArrayList();
        int i = 0;
        for (int size = beanList.size(); i < size; i++) {
            mapList.add(bean2Map(beanList.get(i)));
        }
        return mapList;
    }

    public static <K, V> Map<K, V> copy(Map<K, V> src) {
        if (null == src) return null;
        Map<K, V> des = new HashMap();
        des.putAll(src);


        return des;
    }


    public static void apply(Object des, Object... srcs) {
        if ((null == des) || (null == srcs) || (srcs.length < 1)) return;
        BeanMap desBeanMap = BeanMap.create(des);
        Set<?> keys = desBeanMap.keySet();
        BeanMap srcBeanMap = null;
        for (Object src : srcs) {
            if (null != src) {
                srcBeanMap = BeanMap.create(src);
                for (Object key : keys) {
                    Object value = srcBeanMap.get(key);
                    if ((null != value) && (null == desBeanMap.get(key))) {
                        desBeanMap.put(des, key, value);
                    }
                }
            }
        }
    }


    public static void apply(Object des, List<Map<String, Object>> srcList) {
        Map<String, Object> src;
        if ((null == des) || (null == srcList) || (srcList.isEmpty())) return;
        BeanMap desBeanMap = BeanMap.create(des);
        Set<?> keys = desBeanMap.keySet();
        for (Iterator localIterator1 = srcList.iterator(); localIterator1.hasNext(); ) {
            src = (Map) localIterator1.next();
            if ((null != src) && (!src.isEmpty())) {
                for (Object key : keys) {
                    Object value = src.get(key);
                    if (null != value) {
                        desBeanMap.put(des, key, value);
                    }
                }
            }
        }
    }

    private static String generateKey(Class<?> src, Class<?> des) {
        return src.getName() + des.getName();
    }

    /**
     * bean 转 String
     */
    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            return JSON.toJSONString(value);
        }
    }


    /**
     * string 转 bean
     */
    public static <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }


}

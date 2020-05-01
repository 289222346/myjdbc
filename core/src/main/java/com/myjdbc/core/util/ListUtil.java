package com.myjdbc.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 集合相关工具类
 *
 * @Author 陈文
 * @Date 2020/4/13  20:50
 */
public final class ListUtil {

    /**
     * 是否是空集合
     *
     * @return boolean
     * @author ChenWen
     * @description
     * @date 2019/7/10 21:12
     */
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 是否是非空集合
     *
     * @param list
     * @return boolean
     * @author ChenWen
     * @description
     * @date 2019/7/10 21:12
     */
    public static boolean isNotEmpty(List list) {
        return list != null && !list.isEmpty();
    }

    /**
     * 对象是否为数组对象
     *
     * @param obj 对象
     * @return 是否为数组对象，如果为{@code null} 返回false
     */
    public static boolean isArray(Object obj) {
        if (null == obj) {
//            throw new NullPointerException("Object check for isArray is null");
            return false;
        }
//        反射 获得类型
        return obj.getClass().isArray();
    }

    /**
     * 对象是否为List集合对象
     *
     * @param obj 对象
     * @return 是否为List集合对象，如果为{@code null} 返回false
     * @Author 陈文
     * @Date 2020/4/13  20:55
     */
    public static boolean isList(Object obj) {
        if (null == obj) {
            return false;
        }
        Class<?> cls = obj.getClass();
        return List.class.isAssignableFrom(cls);
    }

    /**
     * 对象为List集合对象,且为非空集合
     *
     * @param obj 对象
     * @return 是否为List集合对象，如果为{@code null} 返回false
     * @Author 陈文
     * @Date 2020/4/13  20:55
     */
    public static boolean isListAndNotEmpty(Object obj) {
        if (null == obj) {
            return false;
        }
        Class<?> cls = obj.getClass();
        if (List.class.isAssignableFrom(cls)) {
            return isNotEmpty((List) obj);
        }
        return false;
    }

}

package com.myjdbc.core.util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 注解操作工具
 *
 * @author 陈文
 */
public class AnnotationUtil<A extends Annotation> {

    private static final AnnotationUtil annotationUtil = new AnnotationUtil();

    public static <A extends Annotation> A get(Field field, Class<A> annotationCls) {
        Annotation annotation = annotationUtil.getAnnotation(field, annotationCls);
        if (annotation == null) {
            return null;
        } else {
            return (A) annotation;
        }
    }

    public static <A> A get(Class cls, Class<A> annotationCls) {
        Annotation annotation = annotationUtil.getAnnotation(cls, annotationCls);
        if (annotation == null) {
            return null;
        } else {
            return (A) annotation;
        }
    }

    /**
     * 注解存储池
     */
    private final Map<String, A> pool = new LinkedHashMap<>();

    /**
     * 获取注解
     *
     * @param field         属性
     * @param annotationCls 注解类型
     * @return
     */
    private A getAnnotation(Field field, Class<A> annotationCls) {
        String key = transformClass(field.getDeclaringClass(), annotationCls) + field.getName();
        if (pool.containsKey(key)) {
            return pool.get(key);
        }
        A annotation = field.getAnnotation(annotationCls);
        pool.put(key, annotation);
        return annotation;
    }

    /**
     * 获取注解
     *
     * @param cls           类
     * @param annotationCls 注解类型
     * @return
     */
    private A getAnnotation(Class cls, Class<A> annotationCls) {
        String key = transformClass(cls, annotationCls);
        if (pool.containsKey(key)) {
            return pool.get(key);
        }
        A annotation = (A) cls.getAnnotation(annotationCls);
        pool.put(key, annotation);
        return annotation;
    }

    /**
     * 将类和注解类型转换为Key值
     *
     * @param cls           类
     * @param annotationCls 注解类型
     * @return
     */
    private String transformClass(Class cls, Class<? extends Annotation> annotationCls) {
        String key = cls.getName() + annotationCls.getName();
        return key;
    }

}

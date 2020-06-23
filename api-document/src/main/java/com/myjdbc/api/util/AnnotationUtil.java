package com.myjdbc.api.util;


import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 注解操作工具
 *
 * @author 陈文
 */
public class AnnotationUtil {

    /**
     * 获取类上的注解
     *
     * @param cls           类
     * @param annotationCls 指定注解类型
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A findAnnotaion(Class cls, Class<A> annotationCls) {
        return AnnotatedElementUtils.findMergedAnnotation(cls, annotationCls);
    }

    /**
     * 获取属性上的注解
     *
     * @param field         属性
     * @param annotationCls 指定注解类型
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A findAnnotaion(Field field, Class<A> annotationCls) {
        return AnnotatedElementUtils.findMergedAnnotation(field, annotationCls);
    }

    /**
     * 获取方法上的注解
     *
     * @param method        方法
     * @param annotationCls 指定注解类型
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A findAnnotaion(Method method, Class<A> annotationCls) {
        return AnnotatedElementUtils.findMergedAnnotation(method, annotationCls);
    }
}

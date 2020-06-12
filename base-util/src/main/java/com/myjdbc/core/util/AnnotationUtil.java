package com.myjdbc.core.util;

import com.myjdbc.api.annotations.MergeInjection;
import com.myjdbc.api.model.AnnotationObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 注解操作工具
 *
 * @author 陈文
 */
public class AnnotationUtil {

    /**
     * 单例工具
     */
    private static final AnnotationUtil annotationUtil = new AnnotationUtil();

    /**
     * 注解对象存储池
     */
    private final Map<String, AnnotationObject> pool = new LinkedHashMap<>();

    public static <A extends Annotation> AnnotationObject get(Field field, Class<A> annotationCls) {
        A[] annotations = annotationUtil.getAnnotation(field, annotationCls);
        return annotationUtil.get(annotations, annotationCls);
    }

    public static <A extends Annotation> AnnotationObject get(Class cls, Class<A> annotationCls) {
        Annotation[] annotations = annotationUtil.getAnnotation(cls, annotationCls);
        return annotationUtil.get(annotations, annotationCls);
    }


    private <A extends Annotation> AnnotationObject get(Annotation[] annotations, Class<A> annotationCls) {
        try {
            return get(annotations, annotationCls, new HashSet<>());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取注解上的注解,直到找到目标注解为止
     *
     * @param annotations
     * @param annotationCls
     * @return
     */
    private <A extends Annotation> AnnotationObject get(Annotation[] annotations, Class<A> annotationCls, Set<Annotation> aonNotaryOffice) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
        if (annotations == null) {
            return null;
        }

        //先遍历一遍，如果有该注解则直接返回
        for (Annotation annotation : annotations) {
            if (aonNotaryOffice.contains(annotation)) {
                continue;
            }
            if (annotation.annotationType().equals(annotationCls)) {
                return generateAnnotationObject(annotation);
            }
        }
        //在所有注解的子注解中去查找需要的注解
        for (Annotation annotation : annotations) {
            if (aonNotaryOffice.contains(annotation)) {
                continue;
            }
            aonNotaryOffice.add(annotation);
            if (!annotation.annotationType().equals(annotationCls)) {
                AnnotationObject parentAnnotationObject = get(annotation.annotationType().getAnnotations(), annotationCls, aonNotaryOffice);
                if (parentAnnotationObject != null) {
                    //从上级注解获取的，需要解析是否有合并注解
                    for (Method method : annotation.annotationType().getMethods()) {
                        MergeInjection mergeInjection = method.getAnnotation(MergeInjection.class);
                        if (mergeInjection != null) {
                            if (mergeInjection.annotation().equals(parentAnnotationObject.getAnnotation().annotationType())) {
                                Object value = method.invoke(annotation);
                                parentAnnotationObject.addValue(method.getName(), value);
                            }
                        }
                    }
                    return parentAnnotationObject;
                }
            }
        }
        return null;
    }

    private AnnotationObject generateAnnotationObject(Annotation annotation) {
        AnnotationObject annotationObject = new AnnotationObject();
        annotationObject.setAnnotation(annotation);
        for (Method method : annotation.annotationType().getDeclaredMethods()) {
            try {
                Object value = method.invoke(annotation);
                if (value != null) {
                    annotationObject.addValue(method.getName(), value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return annotationObject;
    }


    /**
     * 获取注解
     *
     * @param field         属性
     * @param annotationCls 注解类型
     * @return
     */
    private <A extends Annotation> A[] getAnnotation(Field field, Class<A> annotationCls) {
        String key = transformClass(field.getDeclaringClass(), annotationCls) + field.getName();
//        if (pool.containsKey(key)) {
//            return (A[]) pool.get(key);
//        }
        A[] annotations = (A[]) field.getDeclaredAnnotations();
//        pool.put(key, annotations);
        return annotations;
    }

    /**
     * 获取注解
     *
     * @param cls           类
     * @param annotationCls 注解类型
     * @return
     */
    private <A extends Annotation> A[] getAnnotation(Class cls, Class<A> annotationCls) {
        String key = transformClass(cls, annotationCls);
//        if (pool.containsKey(key)) {
//            return (A[]) pool.get(key);
//        }
        Annotation[] annotations = cls.getDeclaredAnnotations();
//        pool.put(key, annotations);
        return (A[]) annotations;
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

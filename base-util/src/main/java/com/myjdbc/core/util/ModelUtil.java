package com.myjdbc.core.util;

import com.myjdbc.api.annotations.MyApiModel;
import com.myjdbc.api.annotations.MyApiModelProperty;
import com.myjdbc.api.util.AnnotationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Model工具类
 *
 * @author 陈文
 */
public class ModelUtil {

    private static Logger logger = LoggerFactory.getLogger(ModelUtil.class);

    /**
     * 获取属性名称
     * 如果存在{@link MyApiModelProperty}注解的 ,且其{@code name}参数不为空，则用{@code name}作为属性名称
     * 如果不存在{@link MyApiModelProperty}注解的 ,或者{@code name}参数为空的，则使用属性本身名字
     *
     * @param field 实体属性
     * @return 经过处理后的属性名称
     * @Author 陈文
     * @Date 2020/4/21  17:03
     */
    public static String getPropertyName(Field field) {
        MyApiModelProperty myApiModelProperty = AnnotationUtil.findAnnotaion(field, MyApiModelProperty.class);
        /**
         * 没有模型属性，或者{@code name}参数为空，则返回属性原本的名字
         */
        if (myApiModelProperty == null || StringUtil.isEmpty(myApiModelProperty.name())) {
            return field.getName();
        }
        //返回模型属性重定义的名字
        return myApiModelProperty.name();
    }

    /**
     * 获取模型名称(数据库表名)
     *
     * @Author: 陈文
     * @Date: 2020/4/20 11:26
     */
    public static String getModelName(Class cls) {
        MyApiModel apiModel = (MyApiModel) cls.getAnnotation(MyApiModel.class);
        String modelName = null;
        if (apiModel != null) {
            //collectionName
            modelName = apiModel.value();
        }
        if (StringUtil.isEmpty(modelName)) {
            modelName = cls.getSimpleName();
        }
        return modelName;
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2020/3/29  20:09
     * @Description 将对象属性反射成Map(Document)
     */
    public static <T> Map<String, Object> poToMap(T obj) {
        //字段禁止为空
        Class<?> cls = obj.getClass();
        Field[] fields = ClassUtil.getValidFields(cls);
        // 声明Map对象，存储属性
        Map map = new LinkedHashMap();
        for (Field field : fields) {
            // 获取要设置的属性的set方法名称
            try {
                String getField = ClassUtil.getField(field.getName());
                // 声明类函数方法，并获取和设置该方法型参类型
                Method getMethod = cls.getMethod(getField);
                //属性名
                String propertyName = getPropertyName(field);
                // 把获得的值设置给map对象
                Object value = getMethod.invoke(obj);
                if (!ObjectUtils.isEmpty(value)) {
                    map.put(propertyName, value);
                }
            } catch (Exception e) {
                //单个属性如果映射不成功，则警告
                logger.warn(e.getMessage());
            }
        }
        return map;
    }

    public static <T> T mapToPO(Map<String, Object> map, Class<T> cls) {
        T t = null;
        try {
            t = cls.newInstance();
            Field[] fields = ClassUtil.getValidFields(cls);
            for (Field field : fields) {
                try {
                    //数据库字段名
                    String propertyName = getPropertyName(field);
                    //预备赋与的属性值
                    Object value = map.get(propertyName);
                    //属性类型
                    Class fileType = field.getType();

                    //如果属性值为空，则不赋值
                    if (ObjectUtils.isEmpty(value)) {
                        continue;
                    }

                    // 获取要设置的属性的set方法名称
                    String setField = ClassUtil.setField(field.getName());
                    // 声明类函数方法，并获取和设置该方法型参类型
                    Method setMethod = cls.getMethod(setField, fileType);
                    setMethod.invoke(t, value);
                } catch (Exception e) {
                    //单个属性如果映射不成功，则警告
                    StringBuffer msg = new StringBuffer()
                            .append(cls.getName()).append("  ")
                            .append(field.getName())
                            .append("  ")
                            .append(e.getMessage());
                    logger.warn(msg.toString());
                }
            }
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

//    /**
//     * 获取注解对象
//     *
//     * @param cls           注解封装对象类型
//     * @param modelCls      模型类
//     * @param annotationCls 注解对象类型
//     * @param <A>           注解对象-泛型
//     * @param <T>           注解封装对象-泛型，继承至A
//     * @return
//     */
//    public static <A extends Annotation, T extends A> T getAnnotationObject(Class<T> cls, Class modelCls, Class<A> annotationCls) {
//        AnnotationObject apiModelProperty = AnnotationUtil.get(modelCls, annotationCls);
//        return getAnnotationObject(cls, apiModelProperty);
//    }
//
//    /**
//     * 获取注解对象
//     *
//     * @param cls           注解封装对象类型
//     * @param field         属性(字段)
//     * @param annotationCls 注解对象类型
//     * @param <A>           注解对象-泛型
//     * @param <T>           注解封装对象-泛型，继承至A
//     * @return
//     */
//    public static <A extends Annotation, T extends A> T getAnnotationObject(Class<T> cls, Field field, Class<A> annotationCls) {
//        AnnotationObject apiModelProperty = AnnotationUtil.get(field, annotationCls);
//        return getAnnotationObject(cls, apiModelProperty);
//    }
//
//    /**
//     * @param cls              注解封装对象类型
//     * @param apiModelProperty 注解封装对象
//     * @param <T>              注解封装对象-泛型
//     * @return
//     */
//    private static <T extends Annotation> T getAnnotationObject(Class<T> cls, AnnotationObject apiModelProperty) {
//        if (apiModelProperty == null || cls == null) {
//            return null;
//        }
//        T t = mapToPO(apiModelProperty.getValues(), cls);
//        return t;
//    }

}

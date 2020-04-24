package com.myjdbc.core.util;

import org.apache.commons.lang.Validate;

import javax.persistence.MappedSuperclass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author 陈文
 * @Date 2020/3/26  12:50
 * @return
 * @Description 类和字段的工具类
 */
public class ClassUtil {
    /**
     * 获取用于数据库映射的字段
     */
    public static final String DB = "db";

    /**
     * 获取包括父类的属性
     *
     * @return Map[key, value]中，key是模型属性名，value是字段
     * @Author 陈文
     * @Date 2020/3/26  12:59
     * @Description 获取包括父类的属性（所有）
     */
    public static List<String> getAllFieldName(final Class<?> cls) {
        List<Field> list = getAllFieldsList(cls);
        List<String> names = new ArrayList<>();
        list.forEach(field -> names.add(ModelUtil.getPropertyName(field)));
        return names;
    }

    /**
     * 获取包括父类的属性
     *
     * @return Map[key, value]中，key是模型属性名，value是字段
     * @Author 陈文
     * @Date 2020/3/26  12:59
     * @Description 获取包括父类的属性（所有）
     */
    public static Map<String, Field> getAllFieldMap(final Class<?> cls) {
        List<Field> list = getAllFieldsList(cls);
        Map<String, Field> map = new HashMap<>(16);
        list.forEach(field -> map.put(ModelUtil.getPropertyName(field), field));
        return map;
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2020/3/26  12:59
     * @Description 获取包括父类的属性（所有）
     */
    public static Field[] getAllFields(final Class<?> cls) {
        return getAllFields(cls, null);
    }

    /**
     * @Author 陈文
     * @Date 2019/12/10  10:46
     * @Description 获取包括父类的属性
     * （用于数据库映射，只允许MappedSuperclass注解的父类的属性
     * 作为可能的数据库字段被映射）
     */
    public static Field[] getAllFields(final Class<?> cls, String type) {
        final List<Field> allFieldsList = getAllFieldsList(cls, type);
        return allFieldsList.toArray(new Field[allFieldsList.size()]);
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2020/3/26  13:18
     * @Description 获取包括父类的所有属性
     */
    public static List<Field> getAllFieldsList(final Class<?> cls) {
        return getAllFieldsList(cls, null);
    }

    /**
     * @Author 陈文
     * @Date 2019/12/10  10:47
     * @Description 获取包括父类的所有属性
     * （用于数据库映射，只允许MappedSuperclass注解的父类的属性
     * 作为可能的数据库字段被映射）
     */
    public static List<Field> getAllFieldsList(final Class<?> cls, String type) {
        Validate.isTrue(cls != null, "The class must not be null");
        final List<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
                //排除静态字段
                if (!Modifier.isStatic(field.getModifiers())) {
                    allFields.add(field);
                }
            }
            currentClass = currentClass.getSuperclass();
            if (DB.equals(type)) {
                MappedSuperclass mappedSuperclass = currentClass.getAnnotation(MappedSuperclass.class);
                if (mappedSuperclass == null) {
                    //只允许MappedSuperclass注解的父类的属性作为可能的数据库字段被映射
                    currentClass = null;
                }
            }
        }
        return allFields;
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2019/12/27  11:42
     * @Description 获取指定名称的属性（包括可能存在父类中的属性，但如果子类由重写父类属性，则返回子类的属性）
     */
    public static Field findField(final Class<?> cls, String fieldName) {
        Validate.isTrue(cls != null, "The class must not be null");
        Field myField = null;
        Class<?> currentClass = cls;
        while (currentClass != null) {
            try {
                myField = currentClass.getDeclaredField(fieldName);
                if (myField != null) {
                    return myField;
                }
            } catch (NoSuchFieldException e) {
                //这个错误是正常的，如果属性存在于父类，则总会报错
            }
            currentClass = currentClass.getSuperclass();
        }
        return myField;
    }


    /**
     * 获取get方法名称
     *
     * @param fieldName 属性名
     * @return java.lang.String
     * @author ChenWen
     * @date 2019/7/11 20:05
     */
    public static String getField(String fieldName) {
        return "get" + field(fieldName);
    }

    /**
     * 获取set方法名称
     *
     * @param fieldName 属性名
     * @return java.lang.String
     * @author ChenWen
     * @date 2019/7/11 20:05
     */
    public static String setField(String fieldName) {
        return "set" + field(fieldName);
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2020/3/26  12:47
     * @Description 属性的原始名称，且首字母变为大写
     */
    private static String field(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }


    /**
     * @Author 陈文
     * @Date 2019/12/13  16:45
     * @Description 获取get方法
     */
    public static Method getGetMethod(Class<? extends Object> cls, String fieldName, Class<? extends Object>... classes) throws NoSuchMethodException {
        return getMethod(cls, getField(fieldName), classes);
    }


    /**
     * @Author 陈文
     * @Date 2019/12/13  16:45
     * @Description 获取get方法
     */
    public static Method getGetMethod(Class<? extends Object> cls, Field field, Class<? extends Object>... classes) throws NoSuchMethodException {
        return getGetMethod(cls, field.getName(), classes);
    }


    /**
     * @Author 陈文
     * @Date 2019/12/13  16:45
     * @Description 获取get方法
     */
    public static Method getSetMethod(Class<? extends Object> cls, Field field, Class<? extends Object>... classes) throws NoSuchMethodException {
        return getSetMethod(cls, field.getName(), classes);
    }

    /**
     * @Author 陈文
     * @Date 2019/12/13  16:45
     * @Description 获取get方法
     */
    public static Method getSetMethod(Class<? extends Object> cls, String fieldName, Class<? extends Object>... classes) throws NoSuchMethodException {
        return getMethod(cls, setField(fieldName), classes);
    }

    /**
     * @Author 陈文
     * @Date 2019/12/13  16:45
     * @Description 获取set方法
     */
    public static Method getMethod(Class<? extends Object> cls, String fieldMethodName, Class<? extends Object>... classes) throws NoSuchMethodException {
        Method getMethod = cls.getMethod(fieldMethodName, classes);
        return getMethod;
    }

}

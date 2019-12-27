package com.myjdbc.core.util;

import org.apache.commons.lang.Validate;

import javax.persistence.MappedSuperclass;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil {

    /* 获取用于数据库映射的字段 */
    public static final String DB = "db";


    /**
     * @Author 陈文
     * @Date 2019/12/10  10:46
     * @Description 获取包括父类的属性（用于数据库映射）
     */
    public static Field[] getAllFields(final Class<?> cls, String type) {
        final List<Field> allFieldsList = getAllFieldsList(cls, type);

        return allFieldsList.toArray(new Field[allFieldsList.size()]);
    }

    /**
     * @Author 陈文
     * @Date 2019/12/10  10:47
     * @Description 获取包括父类的所有属性（用于数据库映射）
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
                MappedSuperclass mappedSuperclass = cls.getAnnotation(MappedSuperclass.class);
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

}

package com.myjdbc.core.util;

import org.apache.commons.lang.Validate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil {

    /**
     * @Author 陈文
     * @Date 2019/12/10  10:46
     * @Description 获取包括父类的属性
     */
    public static Field[] getAllFields(final Class<?> cls) {
        final List<Field> allFieldsList = getAllFieldsList(cls);
        return allFieldsList.toArray(new Field[allFieldsList.size()]);
    }

    /**
     * @Author 陈文
     * @Date 2019/12/10  10:47
     * @Description 获取包括父类的属性
     */
    public static List<Field> getAllFieldsList(final Class<?> cls) {
        Validate.isTrue(cls != null, "The class must not be null");
        final List<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
                //排除静态字段
                if (!Modifier.isStatic(field.getModifiers())) {
                    allFields.add(field);
                }else {
                    System.out.println("");
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

}

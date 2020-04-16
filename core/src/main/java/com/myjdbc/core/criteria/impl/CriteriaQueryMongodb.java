package com.myjdbc.core.criteria.impl;


import com.myjdbc.core.criteria.CriteriaQuery;
import com.myjdbc.core.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 查询条件构造器
 *
 * @author 陈文
 * @Description
 * @date 2019/7/15 9:47
 */
public class CriteriaQueryMongodb<T> extends CriteriaQueryBase<T> implements CriteriaQuery {
    private static final Logger logger = LoggerFactory.getLogger(CriteriaQueryMongodb.class);

    public CriteriaQueryMongodb(Class<T> cls) {
        super(cls);
    }

    public CriteriaQueryMongodb(Class<T> cls, T t) {
        super(cls);
        try {
            Class<?> currentClass = cls;
            while (currentClass != null) {
                Field[] fields = cls.getDeclaredFields();
                for (Field field : fields) {
                    String getField = ClassUtil.getField(field.getName());
                    Method getMethod = cls.getMethod(getField);
                    Object value = getMethod.invoke(t);
                    if (value != null && !(value + "").equals("")) {
                        if (field.getType().getSimpleName().equals("String")) {
                            String v1 = value + "";
                            int index1 = v1.indexOf("*");
                            if (index1 == -1) {
                                eq(field.getName(), value);
                            } else {
                                v1 = v1.replace("*", "");
                                like(field.getName(), v1);
                            }
                        } else {
                            eq(field.getName(), value);
                        }
                    }
                }
                currentClass = currentClass.getSuperclass();
            }
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
        }
    }

}

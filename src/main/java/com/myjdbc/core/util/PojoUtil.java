package com.myjdbc.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PojoUtil {

    /**
     * 复制Bo，如果值为null或不存在则不复制
     *
     * @param bo
     * @param newBo
     * @param <T>
     * @return
     */
    public static <T, V> T copy(T bo, V newBo) {
        if (null == bo || newBo == null) {
            return null;
        }
        Class cls = bo.getClass();
        Class newcCls = newBo.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            // 获取要设置的属性的set方法名称
            String getField = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            String setField = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            try {
                Method getMethod = newcCls.getMethod(getField);
                Object value = getMethod.invoke(newBo);
                if (value != null) {
                    Method setMethod = cls.getMethod(setField, field.getType());
                    setMethod.invoke(bo, value);
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return bo;
    }


}

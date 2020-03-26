package com.myjdbc.jdbc.util;


import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2018.05.05 转换类 通过反射或方法实现对象互相转换
 *
 * @author 陈文
 * @version 1.02
 */
public class BeanUtilOracle extends BeanUtil {

//    /**
//     * 将对象转换成拼接参数(增加时)
//     *
//     * @param po
//     * @param <T>
//     * @return
//     */
//    public static <T> SavaEntity poToParameter(T po) {
//        String fieles = "";//字段名
//        String values = "";//值
//        List objs = new ArrayList();
//
//
//        Class<?> cls = po.getClass();
//        // 获取该类所有属性名
//        Field[] fields = cls.getDeclaredFields();
//
//
//        // 声明Map对象，存储属性
//        for (Field field : fields) {
//            try {
//                //主键使用新生成的UUID，然后将UUID反射进实体，传递给调用者
//                if (field.getName().equals(key)) {
//                    String uuid = UUIDHexGenerator.getUUID();
//                    fieles += key;
//                    values = "?";
//                    objs.add(uuid);
//
//                    String setField = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
//                    Method setMethod = cls.getMethod(setField, String.class);
//                    setMethod.invoke(po, uuid);
//                    continue;
//                }
//                // 获取要设置的属性的set方法名称
//                String getField = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
//                // 声明类函数方法，并获取和设置该方法型参类型
//                Method getMethod = cls.getMethod(getField);
//                Object value = getMethod.invoke(po);
//                if (value != null && !value.toString().equals("null")) {
//                    fieles += "," + getSqlFormatName(field.getName());
//                    values += ",?";
//                    objs.add(value);
//                }
//            } catch (NoSuchMethodException e) {
//                //e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                //e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                //e.printStackTrace();
//            }
//        }
//        Map<String, Object> map = new HashMap<>();
//        if (fieles.length() > 1) {
//            map.put("fieles", fieles);
//            map.put("values", values);
//            map.put("objs", objs.toArray());
//        }
//        return map;
//    }

    /**
     * 将对象转换成拼接参数(批量增加时的拼接)
     *
     * @param pos
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> poToParameters(List<T> pos, String key) {

        Class<?> cls = pos.get(0).getClass();
        // 获取该类所有属性名
        String fieles = "";//字段名
        String values = "";//？好（值位置）
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!BeanUtil.isNotTransient(field, cls)) {
                fieles += "," + getSqlFormatName(cls, field);
                values += ",?";
            }
        }
        // 声明Map对象，存储属性
        List<Object[]> list = new ArrayList<>();
        for (T po : pos) {
            List objs = new ArrayList();//值
            for (Field field : fields) {
                try {
                    //主键使用新生成的UUID，然后将UUID反射进实体，传递给调用者
                    if (field.getName().equals(key)) {
                        String uuid = UUIDHexGenerator.getUUID();
                        objs.add(uuid);
                        String setField = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
                        Method setMethod = cls.getMethod(setField, String.class);
                        setMethod.invoke(po, uuid);
                        continue;
                    }
                    // 获取要设置的属性的set方法名称
                    String getField = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    // 声明类函数方法，并获取和设置该方法型参类型
                    Method getMethod = cls.getMethod(getField);
                    Transient aTransient = getMethod.getAnnotation(Transient.class);
                    if (aTransient == null) {
                        Object value = getMethod.invoke(po);
                        objs.add(value);
                    }
                } catch (NoSuchMethodException e) {
                    //e.printStackTrace();
                } catch (IllegalAccessException e) {
                    //e.printStackTrace();
                } catch (InvocationTargetException e) {
                    //e.printStackTrace();
                }
            }
            list.add(objs.toArray());
        }
        Map<String, Object> map = new HashMap<>();
        if (fieles.length() > 1) {
            map.put("fieles", fieles.substring(1));
            map.put("values", values.substring(1));
            map.put("objs", list);
        }
        return map;
    }


//    /**
//     * 将对象转换成拼接参数(修改时的拼接)
//     *
//     * @param po
//     * @param <T>
//     * @return
//     */
//    public static <T> SavaEntity poToParameter2(T po) {
//        String fieles = "";//字段名
//        Object keyValue = null;//值
//        List objs = new ArrayList();
//        Class<?> cls = po.getClass();
//        // 获取该类所有属性名
//        Field[] fields = cls.getDeclaredFields();
//        // 声明Map对象，存储属性
//        for (Field field : fields) {
//            // 获取要设置的属性的set方法名称
//            String getField = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
//            // 声明类函数方法，并获取和设置该方法型参类型
//            Method getMethod = null;
//            try {
//                getMethod = cls.getMethod(getField);
//                Object value = getMethod.invoke(po);
//                //排除主键
////                if (field.getName().equals(key)) {
////                    if (value == null) {
////                        //主键为空，调用新增拼接参数方法
//////                        return poToParameter(key, po);
////                    }
////                    keyValue = value;
////                } else if (value != null && !value.toString().equals("null")) {
////                    fieles += "," + getSqlFormatName(field.getName()) + "=? ";
////                    objs.add(value);
////                }
//            } catch (NoSuchMethodException e) {
//                //e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                //e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                //e.printStackTrace();
//            }
//
//            //主键放在最后添加
//            objs.add(keyValue);
//            Map<String, Object> map = new HashMap<>();
//            SavaEntity savaEntity = new SavaEntity();
//            savaEntity.setFieles(fieles.substring(1));
//            savaEntity.setObjs(objs);
////        savaEntity.setValues(va);
////        map.put(key, keyValue);//主键
//        }
//        return null;
//    }

}




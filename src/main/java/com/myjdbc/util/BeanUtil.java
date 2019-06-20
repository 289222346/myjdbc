package com.myjdbc.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 2018.05.05 转换类 通过反射或方法实现对象互相转换
 *
 * @author 陈文
 * @version 1.02
 */
public class BeanUtil {

    /**
     * 2018.04.17 ResultSet—>ArrayList<T>数组反射
     *
     * @param cls-Object类型
     * @param rs-数据集合
     * @return ArrayList<cls>数组
     */
    public static <T> List<T> populate(Class<T> cls, ResultSet rs) {
        List<T> list = new ArrayList<>();
        try {
            Field[] fields = cls.getDeclaredFields();
            while (rs.next()) {
                T obj = cls.newInstance();
                for (Field field : fields) {
                    // 获取要设置的属性的set方法名称
                    String setField = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    String getRs = "get" + field.getType().getSimpleName().substring(0, 1).toUpperCase()
                            + field.getType().getSimpleName().substring(1);
                    Method setMethod;
                    Object value;
                    try {
                        if (getRs.equals("getBigInteger")) {
                            //BigInteger在Po类中都是Long表示，所以要转换
                            setMethod = cls.getMethod(setField, cls.getDeclaredField(field.getName()).getType());
                            Method getMethod = ResultSet.class.getMethod("getObject", String.class);
                            value = new BigInteger(getMethod.invoke(rs, getSqlFormatName(field.getName()).toString()).toString());
                        } else {
                            setMethod = cls.getMethod(setField, cls.getDeclaredField(field.getName()).getType());
                            Method getMethod = ResultSet.class.getMethod(getRs, String.class);
                            value = getMethod.invoke(rs, getSqlFormatName(field.getName()).toString());
                        }
                        if (value != null)
                            setMethod.invoke(obj, value);
                    } catch (Exception e) {
                        //抛弃掉异常，异常直接跳过，不赋值
                    }
                }
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 给类名转换，加下划线
     *
     * @param cls
     * @return
     */
    public static String getTableName(Class cls) {
        return getSqlFormatName(cls.getSimpleName());
    }

    /**
     * 在大写字母前加下划线
     *
     * @param name
     * @return
     */
    public static String getSqlFormatName(String name) {
        String str = name.substring(1);
        StringBuffer newStr = new StringBuffer(name.substring(0, 1));
        for (char c : str.toCharArray()) {
            newStr.append(Character.isUpperCase(c) ? "_" + c : c);//大写字母前面加下划线
        }
        return newStr.toString();
    }

    /**
     * 将带下划线的字段转成Pojo类形式
     *
     * @param name
     * @return
     */
    public static String getPojoSql(String name) {
        String str = name;
        StringBuffer newStr = new StringBuffer();
        boolean flag = false;
        for (char c : str.toCharArray()) {
            if (c == '_') {
                flag = true;
            } else {
                if (flag) {
                    newStr.append((c + "").toUpperCase());
                    flag = false;
                } else {
                    newStr.append((c + "").toLowerCase());
                }
            }
        }
        return newStr.toString();
    }

    /**
     * 将对象转换成拼接参数(增加时)
     *
     * @param po
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> poToParameter(T po) {
        String fieles = "";//字段名
        String values = "";//值
        List objs = new ArrayList();
        Class<?> cls = po.getClass();
        // 获取该类所有属性名
        Field[] fields = cls.getDeclaredFields();
        // 声明Map对象，存储属性
        for (Field field : fields) {
            // 获取要设置的属性的set方法名称
            String getField = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            // 声明类函数方法，并获取和设置该方法型参类型
            Method getMethod = null;
            try {
                getMethod = cls.getMethod(getField);
                Object value = getMethod.invoke(po);
                if (value != null && !value.toString().equals("null")) {
                    fieles += "," + getSqlFormatName(field.getName());
                    values += ",?";
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
        Map<String, Object> map = new HashMap<>();
        if (fieles.length() > 1) {
            map.put("fieles", fieles.substring(1));
            map.put("values", values.substring(1));
            map.put("objs", objs.toArray());
        }
        return map;
    }

    /**
     * 将对象转换成拼接参数(批量增加时的拼接)
     *
     * @param pos
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> poToParameters(T[] pos) {
        String fieles = "";//字段名
        String values = "";//值
        List objs = new ArrayList();
        Class<?> cls = pos[0].getClass();
        // 获取该类所有属性名
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            fieles += "," + getSqlFormatName(field.getName());
            values += ",?";
        }
        // 声明Map对象，存储属性
        for (T po : pos) {
            for (Field field : fields) {
                // 获取要设置的属性的set方法名称
                String getField = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                // 声明类函数方法，并获取和设置该方法型参类型
                Method getMethod = null;
                try {
                    getMethod = cls.getMethod(getField);
                    Object value = getMethod.invoke(po);
                    objs.add(value);

                } catch (NoSuchMethodException e) {
                    //e.printStackTrace();
                } catch (IllegalAccessException e) {
                    //e.printStackTrace();
                } catch (InvocationTargetException e) {
                    //e.printStackTrace();
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        if (fieles.length() > 1) {
            map.put("fieles", fieles.substring(1));
            map.put("values", values.substring(1));
            map.put("objs", objs.toArray());
        }
        return map;
    }


    /**
     * 将对象转换成拼接参数(修改时的拼接)
     *
     * @param po
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> poToParameter(T po, String key) {
        String fieles = "";//字段名
        Object keyValue = null;//值
        List objs = new ArrayList();
        Class<?> cls = po.getClass();
        // 获取该类所有属性名
        Field[] fields = cls.getDeclaredFields();
        // 声明Map对象，存储属性
        for (Field field : fields) {
            // 获取要设置的属性的set方法名称
            String getField = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            // 声明类函数方法，并获取和设置该方法型参类型
            Method getMethod = null;
            try {
                getMethod = cls.getMethod(getField);
                Object value = getMethod.invoke(po);
                //排除主键
                if (field.getName().equals(key)) {
                    if (value == null) {
                        //主键为空，调用新增拼接参数方法
                        return poToParameter(po);
                    }
                    keyValue = value;
                } else if (value != null && !value.toString().equals("null")) {
                    fieles += "," + getSqlFormatName(field.getName()) + "=? ";
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
        //主键放在最后添加
        objs.add(keyValue);
        Map<String, Object> map = new HashMap<>();
        map.put("fieles", fieles.substring(1));
        map.put("objs", objs.toArray());
        map.put(key, keyValue);//主键
        return map;
    }

    /**
     * 将对象转换成拼接参数(修改时的拼接)
     *
     * @param po
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> findParameter(T po) {
        String fieles = "";//字段名
        List objs = new ArrayList();
        Class<?> cls = po.getClass();
        // 获取该类所有属性名
        Field[] fields = cls.getDeclaredFields();
        // 声明Map对象，存储属性
        for (Field field : fields) {
            // 获取要设置的属性的set方法名称
            String getField = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            // 声明类函数方法，并获取和设置该方法型参类型
            Method getMethod = null;
            try {
                getMethod = cls.getMethod(getField);
                Object value = getMethod.invoke(po);
                if (value != null && !value.toString().equals("null")) {
                    fieles += "," + getSqlFormatName(field.getName()) + "=? ";
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
        //主键放在最后添加
        Map<String, Object> map = new HashMap<>();
        map.put("fieles", fieles.substring(1));
        map.put("objs", objs.toArray());
        return map;
    }

    /**
     * 2018.05.04 Object—>Map<String,Object>
     * <p>
     * 将对象属性反射成 Map(属性名,属性值)
     *
     * @param obj
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Map<String, Object> obj_Map(T obj) {
        Class<?> cls = obj.getClass();
        // 获取该类所有属性名
        Field[] fields = cls.getDeclaredFields();
        // 声明Map对象，存储属性
        Map map = new HashMap();
        for (Field field : fields) {
            // 获取要设置的属性的set方法名称
            String getField = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);

            try {
                // 声明类函数方法，并获取和设置该方法型参类型
                Method getMethod = cls.getMethod(getField);
                // 把获得的值设置给map对象
                Object value = getMethod.invoke(obj);
                if (value != null) {
                    map.put(field.getName(), value);
                }
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 2018.05.04 字符串转Sql日期格式
     * 暂时没有被调用的方法
     *
     * @param strDate
     * @return
     */
    public static java.sql.Date strToDate(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (strDate.length() > 10) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        java.util.Date d = null;
        try {
            d = format.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Date date = new java.sql.Date(d.getTime());
        return date;
    }

    public static <T> T pMap_Obj(Class<T> cls, Map<String, String[]> maps) {
        Map<String, String> map = new HashMap<>();
        Set<String> set = maps.keySet();
        for (String key : set) {
            map.put(key, maps.get(key)[0]);
        }
        return map_Obj(cls, map);
    }

    /**
     * 2018.05.04 Object—>Map<String,Object>
     * <p>
     * 将对象属性反射成 Map(属性名,属性值)
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T map_Obj(Class cls, Map map) {
        Object obj = null;
        try {
            obj = cls.newInstance();
        } catch (InstantiationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 获取该类所有属性名
        Field[] fields = cls.getDeclaredFields();
        // 声明Map对象，存储属性
        for (Field field : fields) {
            try {
                // 获取要设置的属性的set方法名称
                String setField = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);

                // 获取Map中对应键(属性)的值
                // 并转换成指定数据类型
                Object maxValue = map.get(field.getName());
                if (maxValue != null) {
                    Object value = BeanUtilE.objE(field.getType().toString(), maxValue);
                    if (value != null) {
                        // 声明类函数方法，并获取和设置该方法型参类型
                        Method setMethod = cls.getMethod(setField, new Class[]{field.getType()});
                        // 把获得的值设置给obj对象
                        setMethod.invoke(obj, value);
                    }
                }
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return (T) obj;
    }
}

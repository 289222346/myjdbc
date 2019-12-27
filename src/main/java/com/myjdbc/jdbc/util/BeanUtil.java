package com.myjdbc.jdbc.util;

import com.myjdbc.core.util.ClassUtil;
import com.myjdbc.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author ChenWen
 * @Description 转换类 通过反射或方法实现对象互相转换，以及其他核心工具类
 * @date 2019/7/10 19:16
 */
public class BeanUtil {
    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * @Author 陈文
     * @Date 2019/12/8  0:20
     * @Description 获取数据库查询值, 并进行处理
     */
    public static Object getValue(Field field, int i, ResultSet rs) throws SQLException {
//        Dictionary dictionary = BeanUtil.getDictionary(field);
//        Class type;
//        if (dictionary != null && dictionary.copyClass() != Class.class) {
//            type = dictionary.copyClass();
//        } else {
//            type = field.getType();
//        }
        Class type = field.getType();
        if (type == Short.class || type == short.class) {
            return rs.getShort(i);
        } else if (type == Integer.class || type == int.class) {
            return rs.getInt(i);
        } else if (type == Long.class || type == long.class) {
            return rs.getLong(i);
        } else if (type == Double.class || type == double.class) {
            return rs.getDouble(i);
        } else if (type == Float.class || type == float.class) {
            return rs.getFloat(i);
        } else if (type == BigInteger.class) {
            return rs.getInt(i);
        } else if (type == BigDecimal.class) {
            return rs.getBigDecimal(i);
        } else if (type == Date.class) {
            return rs.getTimestamp(i);
        } else {
            return rs.getString(i);
        }
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2019/12/27  10:44
     * @Description 将数据库查询值映射进实体
     */
    public static <T> T setValue(Class<T> cls, Field field, T obj, Object value) {
        try {
            ManyToOne manyToOne = getManyToOne(cls, field);
            if (manyToOne != null) {
                //多对一，要先New一个对应实体，然后把ID赋值给他
                //把生成并赋值的实体赋值给value
                Class fieldClass = field.getType();
                String referencedColumnName = getReferencedColumnName(cls, field);
                Object fieldValue = fieldClass.newInstance();
                if (fieldValue != null) {
                    Field fieldChild = ClassUtil.findField(fieldClass, referencedColumnName);
                    fieldValue = setValue(fieldClass, fieldChild, fieldValue, value);

                    String setField = setField(field.getName());
                    Method setMethod = cls.getMethod(setField, field.getType());
                    setMethod.invoke(obj, fieldValue);
                }
            } else {
                String setField = setField(field.getName());
                Method setMethod = cls.getMethod(setField, field.getType());
                setMethod.invoke(obj, value);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/7  21:32
     * @Description 对特定类型进行转换
     */
    public static String assembleRs(String type) {
        if (type.equals("Integer")) {
            type = "Int";
        } else if (type.equals("BigInteger")) {
            type = "Object";
        } else if (type.equals("Date")) {
            //解決日期时分秒不显示问题
            //暂不确定Mysql会不会出现问题
            type = "Timestamp";
        }
        String str = type.substring(0, 1).toUpperCase() + type.substring(1);
        return str;
    }

    /**
     * 获取实体映射的数据库表名
     *
     * @param cls
     * @return
     */
    public static String getTableName(Class cls) {
        Table table = (Table) cls.getAnnotation(Table.class);
        if (table != null) {
            return table.name();
        } else {
            return getUpperName(cls.getSimpleName());
        }
    }

    /**
     * 获取主键属性
     *
     * @param cls 实体类
     * @return java.lang.String
     * @author ChenWen
     * @date 2019/7/12 11:48
     */
    public static <T> String getPrimaryKey(Class<T> cls) {
        Field[] fields = ClassUtil.getAllFields(cls, null);
        String pk = "";
        for (Field field : fields) {
            String getField = BeanUtil.getField(field.getName());
            Method getMethod = null;
            try {
                getMethod = cls.getMethod(getField);
                Id id = getMethod.getAnnotation(Id.class);
                if (id != null) {
                    pk += field.getName();
//                    Column column = getMethod.getAnnotation(Column.class);
//                    if (column != null && column.name() != null) {
//                        pk += "," + column.name();
//                    } else {
//                        pk += "id";
//                    }
                    return pk;
                }
            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
            }
        }
        return "id";
    }

    /**
     * 获取主键字段名
     *
     * @param cls 实体类
     * @return java.lang.String
     * @author ChenWen
     * @date 2019/7/12 11:48
     */
    public static <T> String getPrimaryKeyFieldName(Class<T> cls) {
        String id = getPrimaryKey(cls);
        id = getSqlFormatName(cls, id);
        return id;
    }

    /**
     * 获取主键值
     *
     * @param t 实体
     * @return java.lang.String
     * @author ChenWen
     * @date 2019/12/11 16:57
     */
    public static String getId(Object t) {
        try {
            Class cls = t.getClass();
            String fieleName = getPrimaryKey(t.getClass());
            String getField = getField(fieleName);//"getId";
            Method getMethod = cls.getMethod(getField);
            Object id = getMethod.invoke(t);
            if (id != null) {
                return id + "";
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @Author 陈文
     * @Date 2019/12/13  20:10
     * @Description 通过数据库字段名获取实体属性名称
     */
    public static String getPrimaryName(Class cls, String fieldName) {
        //运行到此处，说明没有找到该方法，那么有可能fieldName传入的不是实体属性名，而是数据库表字段名
        //遍历所有字段，找到符合的属性，并获取对应值
        for (Field field : ClassUtil.getAllFields(cls, null)) {
            Column column = getColumn(cls, field);
            if (column != null && fieldName.equals(column.name())) {
                return field.getName();
            }
        }
        return fieldName;
    }


    /**
     * 获取主键值
     *
     * @param t 实体
     * @return java.lang.String
     * @author ChenWen
     * @date 2019/12/11 16:57
     */
    public static Object getPrimaryValue(Object t, String fieldName) {
        Class cls = t.getClass();
        Object value = getPrimaryValue(cls, t, fieldName);
        if (value != null) {
            return value;
        }
        //运行到此处，说明没有找到该方法，那么有可能fieldName传入的不是实体属性名，而是数据库表字段名
        //遍历所有字段，找到符合的属性，并获取对应值
        for (Field field : ClassUtil.getAllFields(cls, null)) {
            Column column = getColumn(cls, field);
            if (column != null && fieldName.equals(column.name())) {
                value = getPrimaryValue(cls, t, field.getName());
                break;
            }
        }
        return null;
    }

    private static Object getPrimaryValue(Class cls, Object t, String fieldName) {
        try {
            Method getMethod = getMethod(cls, getField(fieldName));
            Object value = getMethod.invoke(t);
            if (value != null) {
                return value + "";
            }
        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
        } catch (IllegalAccessException e) {
//            e.printStackTrace();
        } catch (InvocationTargetException e) {
//            e.printStackTrace();
        }
        return null;
    }


    /**
     * 实体属性转换成字段名（实体类转换成表名）
     *
     * @param field
     * @return java.lang.String
     * @author ChenWen
     * @description 有注释走注释
     * 无注释在字段名大写字母前加下划线（首字母除外）
     * @date 2019/7/11 21:14
     */
    public static String getSqlFormatName(Class<?> cls, Field field) {
        //优先以Column注解为准
        Column column = getColumn(cls, field);
        if (column != null) {
            return column.name().toUpperCase();
        } else {
            //JoinColumn注解为第二优先
            JoinColumn joinColumn = getJoinColumn(cls, field);
            if (joinColumn != null) {
                return joinColumn.name().toUpperCase();
            } else {
                return getUpperName(field.getName());
            }
        }
    }

    public static String getSqlFormatName(Class<?> cls, String fieldName) {
        try {
            Method getMethod = getMethod(cls, getField(fieldName));
            Column column = getMethod.getAnnotation(Column.class);
            if (column != null) {
                return column.name().toUpperCase();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return getUpperName(fieldName);
    }

    private static String getUpperName(String name) {
//        String str = name.substring(1);
//        StringBuffer newStr = new StringBuffer(name.substring(0, 1));
//        for (char c : str.toCharArray()) {
//            newStr.append(Character.isUpperCase(c) ? "_" + c : c);//大写字母前面加下划线
//        }
//        return newStr.toString().toUpperCase();
        return name.toUpperCase();
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
                    fieles += "," + getSqlFormatName(cls, field);
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
            fieles += "," + getSqlFormatName(cls, field);
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
//
//    /**
//     * 将对象转换成拼接参数(修改时的拼接)
//     *
//     * @param po
//     * @param <T>
//     * @return
//     */
//    public static <T> Map<String, Object> poToParameter(T po) {
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
//                if (field.getName().equals(key)) {
//                    if (value == null) {
//                        //主键为空，调用新增拼接参数方法
//                        return poToParameter(po);
//                    }
//                    keyValue = value;
//                } else if (value != null && !value.toString().equals("null")) {
//                    fieles += "," + getSqlFormatName(field.getName()) + "=? ";
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
//        //主键放在最后添加
//        objs.add(keyValue);
//        Map<String, Object> map = new HashMap<>();
//        map.put("fieles", fieles.substring(1));
//        map.put("objs", objs.toArray());
//        map.put(key, keyValue);//主键
//        return map;
//    }

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
                    fieles += "," + getSqlFormatName(cls, field) + "=? ";
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
     * @param flag True表示按照数据库访问模式来匹配
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Map<String, Object> pojoToMap(T obj, boolean flag) {
        Class<?> cls = obj.getClass();
        // 获取该类所有属性名
        Field[] fields = cls.getDeclaredFields();
        // 声明Map对象，存储属性
        Map map = new LinkedHashMap();
        for (Field field : fields) {
            // 获取要设置的属性的set方法名称
            String getField = getField(field.getName());
            try {
                // 声明类函数方法，并获取和设置该方法型参类型
                Method getMethod = cls.getMethod(getField);

                //不是数据库字段，不转换
                if (flag) {
                    Transient aTransient = getMethod.getAnnotation(Transient.class);
                    if (aTransient != null) {
                        continue;
                    }
                }
                String fieldName = getSqlFormatName(cls, field);

                // 把获得的值设置给map对象
                Object value = getMethod.invoke(obj);
                if (value != null) {
                    map.put(fieldName, value);
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

        Date d = null;
        try {
            d = format.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Date date = new java.sql.Date(d.getTime());
        return date;
    }

//    public static <T> T pMap_Obj(Class<T> cls, Map<String, String[]> maps) {
//        Map<String, String> map = new HashMap<>();
//        Set<String> set = maps.keySet();
//        for (String key : set) {
//            map.put(key, maps.get(key)[0]);
//        }
//        return map_Obj(cls, map);
//    }

    /**
     * 2018.05.04 Object—>Map<String,Object>
     * <p>
     * 将对象属性反射成 Map(属性名,属性值)
     *
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T mapToPrjo(Class cls, Map map) {
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
                String setField = setField(field.getName());

                // 获取Map中对应键(属性)的值
                // 并转换成指定数据类型
                Object value = null;
                if (map.containsKey(field.getName())) {
                    value = map.get(field.getName());
                } else {
                    String columnName = getSqlFormatName(cls, field);
                    value = map.get(columnName);
                }
                if (value != null) {
                    // 声明类函数方法，并获取和设置该方法型参类型
                    Class type = field.getType();
                    Method setMethod = BeanUtil.getSetMethod(cls, field, type);

                    // 把获得的值设置给obj对象
                    if (type == String.class) {
                        setMethod.invoke(obj, value.toString());
                    } else if (type == Integer.class || type == int.class) {
                        setMethod.invoke(obj, Integer.parseInt(value.toString()));
                    } else if (type == Double.class || type == double.class) {
                        setMethod.invoke(obj, Double.parseDouble(value.toString()));
                    } else if (type == Float.class || type == float.class) {
                        setMethod.invoke(obj, Float.parseFloat(value.toString()));
                    } else if (type == List.class) {
                        setMethod.invoke(obj, value);
                    } else {
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


    /**
     * @return boolean
     * @author ChenWen
     * @description 检查是否非数据库字段（字段）
     * @date 2019/7/10 19:39
     */
    public static boolean isNotTransient(Field field, Class cls) {
        try {
            String getField = getField(field.getName());
            Method getMethod = cls.getMethod(getField);
            Transient aTransient = getMethod.getAnnotation(Transient.class);
            if (aTransient != null) {
                return true;
            }
        } catch (NoSuchMethodException e) {
            return true;
        }
        return false;
    }

    /**
     * @return java.lang.Object
     * @author ChenWen
     * @description 反射某个字段的值
     * @date 2019/7/10 19:43
     */
    public static <T> Object reflexField(T t, String key) {
        try {
            String getField = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
            Method getMethod = t.getClass().getMethod(getField);
            Object value = getMethod.invoke(t);
            return value;
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
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

    private static String field(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * @Author 陈文
     * @Date 2019/12/13  16:45
     * @Description 获取get方法
     */
    public static Method getGetMethod(Class<? extends Object> cls, Field field, Class<? extends Object>... classes) throws NoSuchMethodException {
        return getMethod(cls, getField(field.getName()), classes);
    }

    /**
     * @Author 陈文
     * @Date 2019/12/13  16:45
     * @Description 获取get方法
     */
    public static Method getSetMethod(Class<? extends Object> cls, Field field, Class<? extends Object>... classes) throws NoSuchMethodException {
        return getMethod(cls, setField(field.getName()), classes);

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

    /**
     * @Author 陈文
     * @Date 2019/12/13  16:45
     * @Description 获取一对多注解
     */
    public static ManyToOne getManyToOne(Class<? extends Object> cls, Field field) {
        try {
            Method getMethod = getGetMethod(cls, field);
            ManyToOne annotation = getMethod.getAnnotation(ManyToOne.class);
            return annotation;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/13  16:45
     * @Description 获取一对多注解
     */
    public static OneToMany getOneToMany(Class<? extends Object> cls, Field field) {
        try {
            Method getMethod = getGetMethod(cls, field);
            OneToMany annotation = getMethod.getAnnotation(OneToMany.class);
            return annotation;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/13  16:45
     * @Description 获取外键关联注解
     */
    public static JoinColumn getJoinColumn(Class<? extends Object> cls, Field field) {
        try {
            Method getMethod = getGetMethod(cls, field);
            JoinColumn annotation = getMethod.getAnnotation(JoinColumn.class);
            return annotation;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/13  16:45
     * @Description 获取字段注解
     */
    public static Column getColumn(Class<? extends Object> cls, Field field) {
        try {
            Method getMethod = getGetMethod(cls, field);
            Column annotation = getMethod.getAnnotation(Column.class);
            return annotation;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getReferencedColumnName(Class cls, Field field) {
        String referencedColumnName;
        JoinColumn joinColumn = getJoinColumn(cls, field);
        if (joinColumn != null) {
            if (StringUtil.isNotEmpty(joinColumn.referencedColumnName())) {
                referencedColumnName = joinColumn.referencedColumnName();
            } else {
                referencedColumnName = getPrimaryKey(field.getType());
            }
        } else {
            //不存在joinColumn，则使用属性名
            referencedColumnName = getUpperName(field.getName());
        }
        return referencedColumnName;
    }

    public static String getPrimaryKeyJoinColumnName(Class cls) {
        PrimaryKeyJoinColumn primaryKeyJoinColumn = (PrimaryKeyJoinColumn) cls.getAnnotation(PrimaryKeyJoinColumn.class);
        if (primaryKeyJoinColumn != null && StringUtil.isNotEmpty(primaryKeyJoinColumn.name())) {
            return primaryKeyJoinColumn.name();
        } else {
            return getPrimaryKeyFieldName(cls);
        }
    }
}

package com.myjdbc.jdbc.core.dao.impl;

import com.myjdbc.core.util.ClassUtil;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.jdbc.util.BeanUtil;
import com.myjdbc.jdbc.annotation.Dictionary;
import com.myjdbc.redis.service.MyRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @Author 陈文
 * @Date 2019/12/15  1:26
 * @Description DataBaseToPOJO
 */
@Repository
public class DBToPojo {
    private static final Logger logger = LoggerFactory.getLogger(DBToPojo.class);

    @Autowired
    private MyRedisService redisService;

    /**
     * @Author 陈文
     * @Date 2019/12/14  12:12
     * @Description 拼接Sql语句，以供JDBC调用
     */
    public PreparedStatement getPS(Connection con, String sql, Object... values) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps = getPSValue(ps, values, 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/8  20:39
     * @Description 插入?占位符数据，遇到数组类型则递归解析
     */
    public PreparedStatement getPSValue(PreparedStatement ps, Object[] values, int i) throws SQLException {
        for (Object objValue : values) {
            if (objValue != null) {
                if (objValue.getClass().isArray()) {
                    //如果是数组类型，则继续解析他
                    getPSValue(ps, (Object[]) objValue, i);
                } else {
                    if (!objValue.equals("")) {
                        ps.setObject(i++, objValue);
                    }
                }
            }
        }
        return ps;
    }


    /**
     * 2018.04.17 ResultSet—>ArrayList<T>数组反射
     *
     * @param cls-Object类型
     * @param rs-数据集合
     * @return ArrayList<cls>数组
     */
    public <T> List<T> populate(Class<T> cls, ResultSet rs) {
        List<T> list = new ArrayList<>();
        try {
            Field[] fields = ClassUtil.getAllFields(cls);
            Map<String, Field> fieldMap = new HashMap<>();
            Map<Field, Dictionary> dictionaryMap = new HashMap<>();
            for (Field field : fields) {
                String key = BeanUtil.getSqlFormatName(cls, field);
                if (key != null) {
                    fieldMap.put(key, field);
                }
                Dictionary dictionary = BeanUtil.getDictionary(field);
                if (dictionary != null && StringUtil.isNotEmpty(dictionary.copyName())) {
                    dictionaryMap.put(field, dictionary);
                }
            }

            while (rs.next()) {
                T obj = cls.newInstance();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String rname = rs.getMetaData().getColumnName(i);
                    //rname统一采用大写，用于抹平Mysql和Oracle的差异性
                    Field field = fieldMap.get(rname.toUpperCase());
                    if (field == null) {
                        continue;
                    }
                    // 获取要设置的属性的set方法名称
                    String setField = BeanUtil.setField(field.getName());
                    String getRs = "get" + BeanUtil.assembleRs(field.getType().getSimpleName());
                    try {
                        Object value = null;
                        Method setMethod = cls.getMethod(setField, field.getType());
                        //获取值
                        value = BeanUtil.getValue(field, i, rs);
                        if (value != null) {
                            //字典映射
                            value = valueToDictionary(cls, field, value);
                            setMethod.invoke(obj, value);
                        }
                    } catch (Exception e) {
                        //抛弃掉异常，异常直接跳过，不赋值
                        logger.error("发现异常：" + e.getMessage());
                    }
                }
                for (Field field : dictionaryMap.keySet()) {
                    Dictionary dictionary = dictionaryMap.get(field);
                    String copyName = BeanUtil.getPrimaryName(dictionary.tableClass(), dictionary.copyName());
                    String getField = BeanUtil.getField(copyName);
                    Method getMethod = BeanUtil.getMethod(cls, getField);
                    Object value = null;
                    value = getMethod.invoke(obj);

                    Method setMethod = BeanUtil.getSetMethod(cls, field, field.getType());
                    if (value != null) {
                        //字典映射
                        value = valueToDictionary(cls, field, value);
                        setMethod.invoke(obj, value);
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
     * @Author 陈文
     * @Date 2019/12/10  16:05
     * @Description 字典映射
     * 如果有字典注解，将值替换成字典映射值
     */
    private Object valueToDictionary(Class<? extends Object> cls, Field field, Object value) {
        Dictionary dictionary = field.getAnnotation(Dictionary.class);
        if (dictionary != null) {
            //从缓存中获取
            if (dictionary.fieldName().equals("")) {
                value = redisService.getDictionaryValue(dictionary.tableClass(), dictionary.replaceName().toUpperCase(), value + "");
            } else {
                value = redisService.getDictionaryValue(dictionary.tableClass(), dictionary.fieldName(), dictionary.replaceName().toUpperCase(), value + "");
            }
        }
        return value;
    }


}

package com.myjdbc.jdbc.core.dao.impl;

import com.myjdbc.core.util.ClassUtil;
import com.myjdbc.jdbc.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 陈文
 * @Date 2019/12/15  1:26
 * @Description DataBaseToPOJO
 */
@Repository
public class DBToPojo {
    private static final Logger logger = LoggerFactory.getLogger(DBToPojo.class);

//    @Autowired
//    private MyRedisService redisService;

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
            Field[] fields = ClassUtil.getAllFields(cls, null);
            Map<String, Field> fieldMap = new HashMap<>();
            for (Field field : fields) {
                String key = BeanUtil.getSqlFormatName(cls, field);
                if (key != null) {
                    fieldMap.put(key, field);
                }
            }

            while (rs.next()) {
                T obj = cls.newInstance();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String rname = rs.getMetaData().getColumnName(i).toUpperCase();
                    //rname统一采用大写，用于抹平Mysql和Oracle的差异性
                    Field field = fieldMap.get(rname.toUpperCase());
                    if (field == null) {
                        continue;
                    }
                    // 获取要设置的属性的set方法名称
                    String getRs = "get" + BeanUtil.assembleRs(field.getType().getSimpleName());
                    try {
                        Object value = null;
//
                        //获取值
                        value = BeanUtil.getValue(field, i, rs);
                        if (value != null) {
                            //将值写入实体
                            obj = BeanUtil.setValue(cls, field, obj, value);
                        }
                    } catch (Exception e) {
                        //抛弃掉异常，异常直接跳过，不赋值
                        logger.error(field.getType().getName() + "->发现异常：" + e.getMessage());
                    }
                }
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}

package com.myjdbc.jdbc.core.dao.impl;


import com.myjdbc.jdbc.core.dao.Dao;
import com.myjdbc.jdbc.util.TypeCconvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.wip.management.trainingandexam.exam.common.myjdbc.util.core.BeanUtilOracle;

/**
 * 2019.02.28 数据访问对象
 * 本类是JDBC的封装类，对Connection进行操作，并将PreparedStatement转换成Pojo对象
 * Pojo类的反射转换通过BeanUtil来完成
 * （BeanUtilE类好像已经没有效果了，是针对Oracle的，但因设计时间过长，记忆可能错乱而不敢确定）
 *
 * @author 陈文
 * @version 1.1
 */
@Repository("dao")
public class DaoImpl implements Dao {
    private static final Logger logger = LoggerFactory.getLogger(DaoImpl.class);

    @Autowired
    private DBToPojo dtop;

    @Override
    public List<Integer> getSeq(Connection con, String seqName, int size) throws SQLException {

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT  ").append(seqName).append(".NEXTVAL FROM dual");
        PreparedStatement ps = null;

        List<Integer> list = new ArrayList<>();
        ps = con.prepareStatement(sql.toString());
        for (int i = 0; i < size; i++) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            Integer value = rs.getInt(1);
            list.add(value);
            rs.close();
        }
        ps.close();
        return list;
    }

    @Override
    public int update(Connection con, String sql, Object... values) throws SQLException {
        PreparedStatement ps = dtop.getPS(con, sql, values);
        int row = 0;
        row = ps.executeUpdate();
        ps.close();
        return row;
    }

    @Override
    public int batchUpdate(Connection con, String sql, List<Object[]> list) throws SQLException {
        PreparedStatement ps = null;
        int sum = 0;
        boolean autoCommit = con.getAutoCommit();
        if (autoCommit) {
            con.setAutoCommit(false);
        }
        ps = con.prepareStatement(sql);
        for (int x = 0; x < list.size(); x++) {
            Object[] obj = list.get(x);
            for (int i = 0; i < obj.length; i++) {
                ps.setObject(i + 1, obj[i]);
            }
            ps.addBatch();
            if ((x != 0 && x % 1000 == 0) || x == list.size() - 1) {//可以设置不同的大小；如50，100，200，500，1000等等                      ps.executeBatch();
                ps.executeBatch();
                ps.clearBatch();
            }
            sum++;
        }
        if (autoCommit) {
            con.commit();
            con.setAutoCommit(true);
        }
        ps.close();
        return sum;
    }

    @Override
    public int batchAdd(Connection con, String sql, List<Object[]> list) throws SQLException {
        int length = 50;
        if (list.size() < length) {
            return batchUpdate(con, "insert " + sql, list);
        }
        PreparedStatement ps = null;

        Boolean autoCommit = con.getAutoCommit();
        if (autoCommit) {
            con.setAutoCommit(false);
        }
        String sql2 = "insert all ";
        for (int q = 0; q < length; q++) {
            sql2 += sql + "  ";
        }
        sql2 += " select 1 from dual ";
        ps = con.prepareStatement(sql2);
        int g = 0;
        for (int x = 0; x < list.size(); x += length) {
            if (x + length < list.size()) {
//                    long l1 = new Date().getTime();
                int i2 = 1;
                for (int q = x; q < x + length; q++) {
                    Object[] obj = list.get(q);
                    for (int i = 0; i < obj.length; i++) {
                        ps.setObject(i + i2, obj[i]);
                    }
                    i2 += obj.length;
                }
                ps.addBatch();
                if ((g != 0 && g % 500 == 0) || g == list.size() - 1) {//可以设置不同的大小；如50，100，200，500，1000等等                      ps.executeBatch();
//                        long l2 = new Date().getTime();
//                        System.out.println(l2 - l1);
                    ps.executeBatch();
                    ps.clearBatch();
//                        long l3 = new Date().getTime();
//                        System.out.println(l3 - l2);
                }
                g++;
            } else {
                ps.executeBatch();
                ps.clearBatch();
                List<Object[]> objects = new ArrayList<>();
                for (int q = x; q < list.size(); q++) {
                    objects.add(list.get(q));
                }
                con.setAutoCommit(false);
                g += batchUpdate(con, "insert " + sql, objects);
            }
        }
        if (autoCommit) {
            con.commit();
            con.setAutoCommit(true);
        }
        ps.close();
        return g;
    }

    @Override
    public <T> List<T> find(Connection con, String sql, Class<T> cls, Object... values) {
        PreparedStatement ps = dtop.getPS(con, sql, values);
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
            // 通过反射获取传入对象属性名称
            List<T> list = dtop.populate(cls, rs);
            rs.close();
            ps.close();
            return list;
        } catch (SQLException e) {
            logger.error("查询数据故障：" + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Object[]> findObjects(Connection con, String sql, Object... values) {
        PreparedStatement ps = dtop.getPS(con, sql, values);
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();

            // 通过反射获取传入对象属性名称
            List<Object[]> list = new ArrayList<>();
            while (rs.next()) {
                Object[] objs = new Object[rs.getMetaData().getColumnCount()];
                for (int i = 1; i <= objs.length; i++) {
                    objs[i - 1] = rs.getObject(i);
                }
                list.add(objs);
            }
            rs.close();
            ps.close();
            return list;
        } catch (SQLException e) {
            logger.error("查询Object[]数据故障：" + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> findMap(Connection con, String sql, Object... values) {
        PreparedStatement ps = dtop.getPS(con, sql, values);
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
            // 通过反射获取传入对象属性名称
            List<Map<String, Object>> list = new ArrayList<>();
            while (rs.next()) {
                int size = rs.getMetaData().getColumnCount();
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= size; i++) {
                    Object o = rs.getObject(i);
                    if (o != null) {
                        if (o.getClass() == Clob.class) {
                            //大数据类型进行适配
                            o = TypeCconvert.ClobToString((Clob) o);
                        }
                        map.put(rs.getMetaData().getColumnName(i), o);
                    }
                }
                list.add(map);
            }
            rs.close();
            ps.close();
            return list;
        } catch (SQLException e) {
            logger.error("查询Map数据故障：" + e.getMessage());
        }
        return null;
    }

    @Override
    public int findCount(Connection con, String sql, Object... values) {
        PreparedStatement ps = dtop.getPS(con, sql, values);
        ResultSet rs = null;
        int count = 0;
        try {
            rs = ps.executeQuery();
            // 通过反射获取传入对象属性名称
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            logger.error("查询数据总量故障：" + e.getMessage());
        }
        return count;
    }
}

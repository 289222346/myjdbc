package com.myjdbc.jdbc.impl;

import com.myjdbc.jdbc.Dao;
import com.myjdbc.util.BeanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2019.02.28 数据访问对象
 * 本类是JDBC的封装类，对Connection进行操作，并将PreparedStatement转换成Pojo对象
 * Pojo类的反射转换通过BeanUtil来完成
 * （BeanUtilE类好像已经没有效果了，是针对Oracle的，但因设计时间过长，记忆可能错乱而不敢确定）
 *
 * @author 陈文
 * @version 1.1
 */
public class DaoImpl implements Dao {

    //拼接Sql语句，以供JDBC调用
    private PreparedStatement getPS(Connection con, String sql, Object... obj) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            for (int i = 0; i < obj.length; i++) {
                ps.setObject(i + 1, obj[i]);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }

    /**
     * 2018.04.17 基础增删改类
     *
     * @param sql-增删改语句
     * @param obj-<?>参数集合
     * @return
     * @author 陈文
     * @version 1.0
     */
    public int update(Connection con, String sql, Object... obj) {
        PreparedStatement ps = getPS(con, sql, obj);
        int row = 0;
        try {
            row = ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }

    /***
     * 2018.05.04 基础查询类（）
     *
     * @author 陈文
     * @version 1.01
     * @param sql-查询语句
     * @param cls-对象类
     * @param obj-查询条件数组
     * @return ArrayList<cls>集合
     */
    public <T> List<T> find(Connection con, String sql, Class<T> cls, Object... obj) {
        try {
            PreparedStatement ps = getPS(con, sql, obj);
            ResultSet rs = ps.executeQuery();
            // 通过反射获取传入对象属性名称
            List<T> list = BeanUtil.populate(cls, rs);
            rs.close();
            ps.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Object[]> findObjects(Connection con, String sql, Object... obj) {
        try {
            PreparedStatement ps = getPS(con, sql, obj);
            ResultSet rs = ps.executeQuery();
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
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> findMap(Connection con, String sql, Object... obj) {
        try {
            PreparedStatement ps = getPS(con, sql, obj);
            ResultSet rs = ps.executeQuery();
            // 通过反射获取传入对象属性名称
            List<Map<String, Object>> list = new ArrayList<>();
            while (rs.next()) {
                int size = rs.getMetaData().getColumnCount();
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= size; i++) {
                    map.put(BeanUtil.getPojoSql(rs.getMetaData().getColumnName(i)), rs.getObject(i));
                }
                list.add(map);
            }
            rs.close();
            ps.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}

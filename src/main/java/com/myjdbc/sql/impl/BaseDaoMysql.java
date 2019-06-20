package com.myjdbc.sql.impl;

import com.myjdbc.jdbc.impl.DaoImpl;
import com.myjdbc.sql.BaseDao;
import com.myjdbc.util.BeanUtil;
import com.myjdbc.util.DBUtil;
import com.myjdbc.util.PoolConnection;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 按照分层原则，每一张数据表都有一个Dao与之对应，为此设计此实现类
 * 具体的Dao类只要继承该类，即可拥有最基础的增删该查功能
 * 支持复杂Sql自定义，可选择List<T>、List<Object[]>、List<Map<String,Object>>三种返回形式。
 * 此实现类仅适用于MySql，如需用于Oracle或其他数据库，请选择相应实现类
 * 以下请注意：*****************************************************
 * 使用本类应该严格按照MyJDBC命名规范
 * Po类名对应数据库表名，例如：LoginLog.java  对应  login_log表
 * Po类中的属性名应该严格对应数据库字段名，例如：userName  对应  user_name
 *
 * @param <T> 数据库表对应的Pojo类
 */
public class BaseDaoMysql<T> extends DaoImpl implements BaseDao<T> {

    /**
     * 自定义连接
     * 如果该连接存在则使用该连接，否则建立一个临时连接
     */
    protected PoolConnection conn = null;

    /**
     * 主键
     */
    private String mainKey;

    /**
     * 数据库表对应的Pojo类，具体类
     */
    private Class<T> mainCls;

    protected BaseDaoMysql(Class<T> mainCls, String mainKey) {
        this.mainCls = mainCls;
        this.mainKey = mainKey;
    }

    protected BaseDaoMysql(Class<T> mainCls, String mainKey, PoolConnection conn) {
        this.mainCls = mainCls;
        this.mainKey = mainKey;
        this.conn = conn;
    }

    //如果自定义连接存在，使用自定义连接
    public PoolConnection getConn() {
        if (conn != null) {
            return conn;
        }
        return DBUtil.getConnection();
    }

    /**
     * 临时连接使用完毕后将连接归还给连接池
     * 这里只归还在本类中创建的，如果是引用外部的，则由外部负责关闭，方便事务处理。
     */
    public void closeConn(PoolConnection con) {
        if (conn != con) {
            con.setFlag(false);
        }
    }

    private String sqlFields() {
        //拼接所有字段
        Field[] fields = mainCls.getDeclaredFields();
        String fieldNames = "";
        for (Field field : fields) {
            fieldNames += "," + BeanUtil.getSqlFormatName(field.getName());
        }
        fieldNames = fieldNames.substring(1);
        return fieldNames;
    }

    /**
     * 重载父对象的方法，在这里处理po,将其拼接为标准Sql
     */
    private List<T> find(Connection con, T po) {
        //拼接所有非null字段
        Map<String, Object> map = BeanUtil.poToParameter(po);
        String sql = "select " + sqlFields() + " from " + BeanUtil.getTableName(mainCls) + " where " + map.get("values");
        return find(con, sql, mainCls, (Object[]) map.get("objs"));
    }

    /**
     * 重载父对象的方法，在这里处理map,将其拼接为标准Sql
     */
    private List<T> find(Connection con, Map<String, Object> map) {
        //拼接所有Key-value键值对
        String parameterName = "";
        List<Object> values = new ArrayList<>();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            parameterName += "and " + BeanUtil.getSqlFormatName(key) + "=? ";
            values.add(value);
        }
        parameterName = parameterName.substring(4);
        String sql = "select " + sqlFields() + " from " + BeanUtil.getTableName(mainCls) + " where " + parameterName;
        return find(con, sql, mainCls, values.toArray());
    }

    @Override
    public T findById(Object value) {
        PoolConnection poolConnection = getConn();
        Connection con = poolConnection.getConn();
        String sql = "select " + sqlFields() + " from " + BeanUtil.getTableName(mainCls) + " where " + BeanUtil.getSqlFormatName(mainKey) + "=?";
        List<T> list;
        list = find(con, sql, mainCls, value);
        T t = null;
        if (list != null && list.size() > 0) {
            t = list.get(0);
        }
        closeConn(poolConnection);
        return t;
    }

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public List<T> findAll(T po) {
        PoolConnection poolConnection = getConn();
        Connection con = poolConnection.getConn();
        List<T> list = find(con, po);
        closeConn(poolConnection);
        return list;
    }

    @Override
    public List<T> findAll(Map<String, Object> map) {
        PoolConnection poolConnection = getConn();
        Connection con = poolConnection.getConn();
        List<T> list = find(con, map);
        closeConn(poolConnection);
        return list;
    }

    @Override
    public List<T> findAll(String sql, Object... objs) {
        PoolConnection poolConnection = getConn();
        Connection con = poolConnection.getConn();
        List<T> list = find(con, sql, mainCls, objs);
        closeConn(poolConnection);
        return list;
    }

    @Override
    public List<Object[]> findAllObjects(String sql, Object... objs) {
        PoolConnection poolConnection = getConn();
        Connection con = poolConnection.getConn();
        List<Object[]> list = findObjects(con, sql, objs);
        closeConn(poolConnection);
        return list;
    }

    @Override
    public List<Map<String, Object>> findAllMap(String sql, Object... objs) {
        PoolConnection poolConnection = getConn();
        Connection con = poolConnection.getConn();
        List<Map<String, Object>> list = findMap(con, sql, objs);
        closeConn(poolConnection);
        return list;
    }


    @Override
    public boolean save(T po) {
        PoolConnection connection = getConn();
        Connection con = connection.getConn();
        Map<String, Object> map = BeanUtil.poToParameter(po, mainKey);
        String sql;
        if (map.get(mainKey) != null) {
            sql = "UPDATE " + BeanUtil.getTableName(po.getClass()) + " SET " + map.get("fieles") + " WHERE \n"
                    + BeanUtil.getSqlFormatName(mainKey) + " =? ";
        } else {
            sql = "insert into " + BeanUtil.getTableName(po.getClass()) + "(" + map.get("fieles") + ") values("
                    + map.get("values") + ")";
        }
        boolean flag = update(con, sql, (Object[]) map.get("objs")) > 0 ? true : false;
        closeConn(connection);
        return flag;
    }

    @Override
    public boolean saves(Object[] po) {
        PoolConnection connection = getConn();
        Connection con = connection.getConn();
        Map<String, Object> map = BeanUtil.poToParameters(po);
        StringBuffer sql = new StringBuffer("insert into " + BeanUtil.getTableName(po[0].getClass()) + "(" + map.get("fieles") + ") values");
        for (int i = 0; i < po.length; i++) {
            sql.append("(" + map.get("values") + ")");
            if (i < po.length - 1) {
                sql.append(",");
            }
        }
        boolean flag = update(con, sql.toString(), (Object[]) map.get("objs")) > 0 ? true : false;
        closeConn(connection);
        return flag;
    }

    @Override
    public BigInteger saveReturnId(T po) {
        PoolConnection connection = getConn();
        Connection con = connection.getConn();
        if (save(po)) {
            String sql = "select last_insert_id();";
            List<Object[]> list = findObjects(con, sql);
            BigInteger big = new BigInteger(list.get(0)[0].toString());
            closeConn(connection);
            return big;
        }
        closeConn(connection);
        return null;
    }

    @Override
    public boolean delete(Object value) {
        PoolConnection connection = getConn();
        Connection con = connection.getConn();
        String sql = "DELETE FROM " + BeanUtil.getTableName(mainCls) + " where " + BeanUtil.getSqlFormatName(mainKey) + "=?";
        boolean flag = update(con, sql, value) > 0 ? true : false;
        closeConn(connection);
        return flag;
    }

    @Override
    public boolean deletes(Object[] value) {
        PoolConnection connection = getConn();
        Connection con = connection.getConn();
        StringBuffer sql = new StringBuffer("DELETE FROM " + BeanUtil.getTableName(mainCls) + " where " + BeanUtil.getSqlFormatName(mainKey) + " in(?");
        for (int i = 1; i < value.length; i++) {
            sql.append(",?");
        }
        sql.append(")");
        boolean flag = update(con, sql.toString(), value) > 0 ? true : false;
        closeConn(connection);
        return flag;
    }

}
package com.myjdbc.jdbc.core.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author ChenWen
 * @Description 该类直接分装JDBC，具有增删改基础方法和多重返回类型的查询方法。 如果需要增加查询后的返回类型，需要在此接口先声明
 * @date 2019/7/11 9:59
 */
public interface Dao {


    /**
     * 查询序列
     *
     * @param seqName
     * @return
     */
    public List<Integer> getSeq(Connection con, String seqName, int size);

    /**
     * 数据的增加、修改、删除
     *
     * @param con    数据库连接对象
     * @param sql    要执行的Sql语句
     * @param values 要传入的参数
     * @return int
     * @author ChenWen
     * @date 2019/7/11 9:59
     */
    int update(Connection con, String sql, Object... values) throws SQLException;

    /**
     * 批量操作
     *
     * @param con  数据库连接对象
     * @param sql  要执行的SQL
     * @param list 批量参数
     * @return int
     * @author ChenWen
     * @description
     * @date 2019/7/12 16:30
     */
    int batchUpdate(Connection con, String sql, List<Object[]> list) throws SQLException;


    int batchAdd(Connection con, String sql, List<Object[]> list) throws SQLException;


    /**
     * 返回对象集合
     *
     * @param con    数据库连接对象
     * @param sql    要执行的Sql语句
     * @param cls    需要返回的数据类型
     * @param values 要传入的参数
     * @return java.util.List<T>
     * @author ChenWen
     * @date 2019/7/11 10:00
     */
    <T> List<T> find(Connection con, String sql, Class<T> cls, Object... values);//查询

    /**
     * 返回Object数组集合
     *
     * @param con    数据库连接对象
     * @param sql    要执行的Sql语句
     * @param values 要传入的参数
     * @author ChenWen
     * @date 2019/7/11 10:00
     */
    List<Object[]> findObjects(Connection con, String sql, Object... values);//查询

    /**
     * 返回List<Map>
     *
     * @param con    数据库连接对象
     * @param sql    要执行的Sql语句
     * @param values 要传入的参数
     * @author ChenWen
     * @date 2019/7/11 10:00
     */
    List<Map<String, Object>> findMap(Connection con, String sql, Object... values);//查询


    /**
     * @param con    数据库连接对象
     * @param sql    要执行的Sql语句
     * @param values 要传入的参数
     * @Author 陈文
     * @Date 2019/12/14  11:47
     * @Description 返回要查询的数量(一半用于查询数据总数)
     */
    int findCount(Connection con, String sql, Object... values);
}

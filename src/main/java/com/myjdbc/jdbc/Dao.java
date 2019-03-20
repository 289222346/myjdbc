package com.myjdbc.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 2019.02.28
 * 该类直接分装JDBC，具有增删改基础方法和多重返回类型的查询方法。
 * 如果需要增加查询后的返回类型，需要在此接口先声明
 */
public interface Dao {
    /**
     * 数据的增加、修改、删除
     *
     * @param con 数据库连接对象
     * @param sql 要执行的Sql语句
     * @param obj 要传入的参数
     * @return
     */
    int update(Connection con, String sql, Object... obj) throws SQLException;

    /**
     * 返回对象集合
     *
     * @param con 数据库连接对象
     * @param sql 要执行的Sql语句
     * @param cls 返回集合中包含的数据类型
     * @param obj 要传入的参数
     * @param <T>
     * @return
     */
    <T> List<T> find(Connection con, String sql, Class<T> cls, Object... obj);//查询

    /**
     * 返回Object数组集合
     *
     * @param con 数据库连接对象
     * @param sql 要执行的Sql语句
     * @param obj 要传入的参数
     * @return
     */
    List<Object[]> findObjects(Connection con, String sql, Object... obj);//查询

    /**
     * 返回List<Map>
     *
     * @param con 数据库连接对象
     * @param sql 要执行的Sql语句
     * @param obj 要传入的参数
     * @return
     */
    List<Map<String, Object>> findMap(Connection con, String sql, Object... obj);//查询


}

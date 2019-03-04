package com.myjdbc.sql;

import com.myjdbc.jdbc.Dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface BaseDao<T> extends Dao {


    /**
     * 返回单个对象（根据主键查询）
     *
     * @param value 主键值
     * @return
     */
    T findById(Object value);

    /**
     * 返回对象数组(根据pojo类中的非null数据去完全匹配，找到全部数据)
     * 如果pojo中不含主键或其他唯一值，则可能查询出多条数据
     *
     * @param po pojo对象
     * @return
     */
    List<T> findAll(T po);

    /**
     * 返回对象数组(将Map转换成参数，再去匹配该数据)
     *
     * @param map key对应数据表中的字段，value对应该字段的值
     * @return
     */
    List<T> findAll(Map<String, Object> map);

    /**
     * 查询多条数据
     * 返回对象数组
     *
     * @param sql  自定义Sql语句
     * @param objs 传入的查询参数
     * @return
     */
    List<T> findAll(String sql, Object... objs);

    /**
     * 保存对象(主键为null则新增，否则为根据主键Id去修改)
     *
     * @param po 要保存的对象
     */
    boolean save(T po);

    /**
     * 批量保存对象
     *
     * @param po 要保存的对象
     */
    boolean saves(T... po);

    /**
     * 保存新增对象并返回主键Id
     *
     * @param po 要保存的对象
     * @return
     */
    BigInteger saveReturnId(T po);

    /**
     * 删除对象（根据主键删除）
     *
     * @param value 主键值
     * @return
     */
    boolean delete(Object value);

    /**
     * 批量删除对象（根据主键删除）
     *
     * @param value 主键值
     * @return
     */
    boolean deletes(Object... value);


    /**
     * 查询多条数据
     * 返回List<Object[]>
     *
     * @param sql  自定义Sql语句
     * @param objs 传入的查询参数
     * @return
     */
    List<Object[]> findAllObjects(String sql, Object... objs);

    /**
     * 查询多条数据
     * 返回List<Map>
     *
     * @param sql  自定义Sql语句
     * @param objs 传入的查询参数
     * @return
     */
    List<Map<String, Object>> findAllMap(String sql, Object... objs);

}

package com.myjdbc.jdbc.core.service;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 公共数据库服务层
 *
 * @author ChenWen
 * @Description 公共数据库服务层
 * @date 2019/7/10 19:49
 */
public interface BaseService {

    /**
     * @Author 陈文
     * @Date 2019/12/7  11:23
     * @Description 开启事务
     * 注意：开启事务，一定要记得提交或者回滚
     * 默认执行半自动事务处理
     */
    void transactionStatus() throws SQLException;

    /**
     * @param connectionFlag True表示半自动事务处理,false表示全手动事务处理
     * @Author 陈文
     * @Date 2019/12/7  11:29
     * @Description 开启事务
     * 半自动事务处理：回滚或者提交的时候结束事务
     * 全手动事务处理：需要手动结束事务
     * 但即使是半自动方式，没有执行提交或者回滚操作，仍然需要手动关闭事务
     */
    void transactionStatus(boolean connectionFlag) throws SQLException;

    /**
     * @Author 陈文
     * @Date 2019/12/7  11:27
     * @Description 回滚事务
     */
    void transactionRollback() throws SQLException;

    /**
     * @Author 陈文
     * @Date 2019/12/7  11:34
     * @Description 提交事务
     */
    void transactionCommit() throws SQLException;

    /**
     * 获取序列
     *
     * @author ChenWen
     * @description 获取SEQ
     * @date 2019/8/16 11:14
     */
    public List<Integer> getSeq(String seqName, int size) throws SQLException;


    /**
     * 手动设置连接
     *
     * @param connection
     * @return void
     * @author ChenWen
     * @description 手动设置连接时，务必记得使用完毕后，主动清除
     * @date 2019/7/13 18:53
     */
    public void setConnection(Connection connection);

    /**
     * 手动连接清除
     *
     * @return void
     * @author ChenWen
     * @description 使用手动连接时，MyJDBC不会去主动关闭连接，需要手动调用该方法。
     * @date 2019/7/13 18:56
     */
    public void closeConnection();


//    /**
//     * 查询表中所有数据
//     *
//     * @param cls      数据库实体类
//     * @param pageNo   当前页
//     * @param pageSize 每页显示数
//     * @return java.util.List<T>
//     * @author ChenWen
//     * @description 查询表中所有数据，不建议大数据时使用，所以强制分页，请合理分配页面大小。
//     * @description 数据库查询十万条数据大概只要两百毫秒，但是封装成POJO对象会使用大量的反射等，需要50秒左右
//     * @date 2019/7/10 20:14
//     */
//    <T> List<T> finAll(Class<T> cls, int pageNo, int pageSize);


    /**
     * 查询单个实体
     *
     * @param cls 实体类
     * @param id  主键值
     * @return T 实体对象或者Null
     * @author 陈文
     * @date 2019/7/15 8:57
     */
    public <T> T findById(Class<T> cls, Serializable id) throws SQLException;


    /**
     * @Author 陈文
     * @Date 2019/12/11  11:23
     * @Description 查询第三位
     */
    public Map<String, Object> findMapById(Class<? extends Object> cls, Serializable id) throws SQLException;

    /**
     * 返回条件查询语句(单字段查询)
     *
     * @param cls        实体类
     * @param fieldName  要查询的字段名称
     * @param filedValue 字段值
     * @return java.util.List<T> 该实体的List或者Null
     * @author 陈文
     * @date 2019/7/15 9:28
     */

    <T> List<T> criteriaEq(Class<T> cls, String fieldName, Object filedValue) throws SQLException;

    <T> List<T> criteriaEq(Class<T> cls, String fieldName, Object filedValue, boolean parentFlag) throws SQLException;

    /**
     * @param cls PO类
     * @Author 陈文
     * @Date 2019/12/12  14:45
     * @Description 查询表中所有数据
     */
    public <T> List<T> findAll(Class<T> cls) throws SQLException;


    /**
     * @Author 陈文
     * @Date 2019/12/3  16:48
     * @Description 不加注释，反正加了你们也看不懂
     */
    public <T> List<T> findAll(CriteriaQuery<T> criteriaQuery) throws SQLException;

    /**
     * 查询多条数据
     * 返回对象数组
     *
     * @param sql    自定义Sql语句
     * @param values 传入的查询参数
     * @return
     */
    List findAll(CriteriaQuery criteriaQuery, String sql, Object... values) throws SQLException;

    /**
     * 查询多条数据
     * 返回对象数组
     *
     * @param sql    自定义Sql语句
     * @param values 传入的查询参数
     * @return
     */
    List<Object[]> findAll(String sql, Object... values) throws SQLException;

    /**
     * @Author 陈文
     * @Date 2019/12/9  1:46
     * @Description 传入SQL，返回对象
     */
    <T> List<T> findAll(Class<T> cls, String sql, Object... values) throws SQLException;

    /**
     * in查询
     *
     * @param cls
     * @param fieldName
     * @param values
     * @return java.util.List<T>
     * @author 陈文
     * @description
     * @date 2019/7/15 11:12
     */
    public <T> List<T> criteriaIn(Class<T> cls, String fieldName, Object[] values) throws SQLException;


    <T> void delete(List<T> t) throws Exception;

    /**
     * 保存实体
     *
     * @param t 实体
     * @return void
     * @author ChenWen
     * @description
     * @date 2019/7/12 12:43
     */
    <T> void save(T t) throws SQLException, Exception;

    /**
     * 批量保存实体
     *
     * @param t 实体
     * @return void
     * @author ChenWen
     * @description
     * @date 2019/7/12 16:21
     */
    public <T> void save(List<T> t) throws SQLException;

    /**
     * 删除实体
     *
     * @param t 数据库实体
     * @return void
     * @author ChenWen
     * @description 依据主键来删除
     * @date 2019/7/10 20:19
     */
    <T> void delete(T t) throws Exception;


}


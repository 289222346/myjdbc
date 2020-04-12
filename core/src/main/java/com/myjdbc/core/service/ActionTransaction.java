package com.myjdbc.core.service;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 公共数据库服务层
 *
 * @author ChenWen
 * @Description 公共数据库服务层
 * @date 2019/7/10 19:49
 */

/**
 * 公共数据库服务层
 *
 * @Author 陈文
 * @Date 2020/4/8  10:28
 * @return
 * @Description 事务管理接口
 * 现支持数据库：
 * 1.Oracle
 * 2.Mysql
 * 3.MongoDB
 */
public interface ActionTransaction {

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

}


package com.myjdbc.core.service;

import java.util.List;

/**
 * 保存或者更新操作
 * * 现支持数据库：
 * 1.Oracle
 * 2.Mysql
 * 3.MongoDB
 *
 * @author 陈文
 * @Date 2020/4/11  17:21
 */

public interface ActionSaveAndUpdate extends BaseServiceRetrieve {

    /**
     * 动作状态：保存
     */
    int ACTION_SAVE = 100;
    /**
     * 动作状态：更新
     */
    int ACTION_UPDATE = 101;
    /**
     * 动作状态：替换
     */
    int ACTION_REPLACE = 102;
    /**
     * 动作状态：删除
     */
    int ACTION_DELETE = 103;

    /**
     * 保存实体
     *
     * @param t 需要更新的实体
     * @return 操作结果状态码
     * @author ChenWen
     * @description
     * @date 2019/7/12 12:43
     */
    <T> int save(T t);

    /**
     * 批量保存实体
     *
     * @param t 需要更新的实体
     * @return 操作结果状态码
     * @author ChenWen
     * @date 2019/7/12 16:21
     */
    <T> int batchSave(List<T> t);

    /**
     * 更新实体（只替换非空字段）
     *
     * @param t 需要更新的实体
     * @return 操作结果状态码
     * @Author 陈文
     * @Date 2020/4/8  10:15
     * @Description
     */
    <T> int update(T t);

    /**
     * 替换实体（完全替换）
     *
     * @param t 需要更新的实体
     * @return 操作结果状态码
     * @Author 陈文
     * @Date 2020/4/10  20:41
     */
    <T> int replace(T t);

    /**
     * 删除实体
     *
     * @param t 数据库实体
     * @return void
     * @author ChenWen
     * @description 依据主键来删除
     * @date 2019/7/10 20:19
     */
    <T> int delete(T t);

    /**
     * 批量删除实体
     *
     * @param list
     * @return
     * @Author 陈文
     * @Date 2020/4/8  10:21
     * @description 依据主键来删除
     */
    <T> int batchDelete(List<T> list);


}


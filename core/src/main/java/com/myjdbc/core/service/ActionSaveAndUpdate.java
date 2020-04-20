package com.myjdbc.core.service;

import com.myjdbc.core.criteria.CriteriaQuery;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 保存或者更新操作
 * <p>
 * 本接口提供数据存储、修改、删除的操作
 * <p>
 * 两个方法会与其他接口存在耦合：
 * {@link #findAllAndDelete(Class, Map)}和{@link #findAllAndDelete(CriteriaQuery)}
 *
 * @author 陈文
 * @version 1.0.4
 * @Date: 2020/4/20 8:45
 */
public interface ActionSaveAndUpdate extends BaseServiceType {

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
     * @param list 需要更新的实体集合
     * @return 操作结果状态码
     * @author ChenWen
     * @date 2019/7/12 16:21
     */
    <T> int batchSave(List<T> list);

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
     * 删除实体
     *
     * @param id  唯一ID
     * @param cls 实体类型
     * @return
     * @Author 陈文
     * @Date 2020/4/13  21:52
     */
    int delete(Serializable id, Class<?> cls);

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

    /**
     * 删除实体
     * 该方法是带有查询限制条件的删除方法
     *
     * @param cls   实体类
     * @param query 查询条件,Map<key,value>{@see key} 字段名，{@see value} 限定值。限定条件全部为EQ(完全相等)
     * @return 查询到的结果集（已经删除）
     * @Author 陈文
     * @Date: 2020/4/20 8:29
     * @description 本方法查询条件应该始终和 {@link ActionRetrieve#findAll(Class, Map)} 保持一致
     */
    <T> List<T> findAndDelete(Class<T> cls, Map<String, Object> query);

    /**
     * 删除实体
     * 该方法是带有查询限制条件的删除方法
     *
     * @param criteriaQuery 查询构造器
     * @return 查询到的结果集（已经删除）
     * @Author 陈文
     * @Date: 2020/4/20 8:29
     * @description 本方法查询条件应该始终和 {@link ActionCriteriaQuery#findAll(CriteriaQuery)} 保持一致
     */
    <T> List<T> findAndDelete(CriteriaQuery<T> criteriaQuery);


}


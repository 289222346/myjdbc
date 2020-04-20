package com.myjdbc.core.service;

import com.myjdbc.core.criteria.CriteriaQuery;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 查询操作
 * <p>
 * 本接口提供查询的基本操作，不涉及其他依赖
 * 但是如使要用依赖于{@link CriteriaQuery}的查询构造器，
 * 具体方法由{@link ActionCriteriaQuery}接口提供
 *
 * @author 陈文
 * @version 1.0.4
 * @Date: 2020/4/20 8:45
 */
public interface ActionRetrieve extends ActionCriteriaQuery {

    /**
     * 查询单个实体
     *
     * @param cls 实体类
     * @param id  主键值
     * @return T 实体对象或者Null
     * @author 陈文
     * @date 2019/7/15 8:57
     */
    <T> T findById(Class<T> cls, Serializable id);

    /**
     * 查询表中所有数据
     *
     * @param cls 映射实体类型
     * @return T实体集合，当查询不到数据时为Null
     * @Author 陈文
     * @Date 2019/12/12  14:45
     */
    <T> List<T> findAll(Class<T> cls);

    /**
     * 查询表中符合条件的数据
     *
     * @param cls   实体类
     * @param query 查询条件,Map<key,value>{@code key} 字段名，{@code value} 限定值。限定条件全部为EQ(完全相等)
     * @return List<T>  T实体集合，当查询不到数据时为Null
     * @author 陈文
     * @date 2019/7/15 8:57
     */
    <T> List<T> findAll(Class<T> cls, Map<String, Object> query);
}


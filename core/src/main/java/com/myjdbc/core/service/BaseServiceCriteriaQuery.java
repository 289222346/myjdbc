package com.myjdbc.core.service;

import com.myjdbc.core.criteria.CriteriaQuery;

import java.util.List;

/**
 * 公共数据库服务层
 *
 * @author 陈文
 * @Description 查询构造器使用接口，该接口的实现类都应该使用查询构造器
 * 现支持数据库：
 * 1.Oracle
 * 2.Mysql
 * 3.MongoDB
 */
public interface BaseServiceCriteriaQuery {

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
    <T> List<T> criteriaEq(Class<T> cls, String fieldName, Object filedValue);

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
    <T> List<T> criteriaIn(Class<T> cls, String fieldName, Object[] values);

    /**
     * @Author 陈文
     * @Date 2019/12/3  16:48
     * @Description 查询表中符合要求的数据
     */
    <T> List<T> findAll(CriteriaQuery<T> criteriaQuery);

    /**
     * @param criteriaQuery 查询构造器
     * @Author 陈文
     * @Date 2020/3/8  5:45
     * @Description 返回，存在符合条件记录的，数量
     */
    int getCount(CriteriaQuery criteriaQuery);

}


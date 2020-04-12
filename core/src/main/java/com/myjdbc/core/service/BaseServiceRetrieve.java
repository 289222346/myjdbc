package com.myjdbc.core.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 公共数据库服务层
 *
 * @author 陈文
 * @Description 查询接口
 * 现支持数据库：
 * 1.Oracle
 * 2.Mysql
 * 3.MongoDB
 */
public interface BaseServiceRetrieve extends BaseServiceCriteriaQuery {

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
     * @param cls 映射实体类型
     * @Author 陈文
     * @Date 2019/12/12  14:45
     * @Description 查询表中所有数据
     */
    <T> List<T> findAll(Class<T> cls);

    /**
     * @param cls   实体类
     * @param query 查询条件
     * @return List<T>  查询表中符合条件的数据
     * @author 陈文
     * @date 2019/7/15 8:57
     */
    <T> List<T> findAll(Class<T> cls, Map<String, Object> query);
}


package com.myjdbc.core.criteria.impl;


import com.myjdbc.core.criteria.CriteriaQuery;

/**
 * 查询条件构造器
 *
 * @author 陈文
 * @Description
 * @date 2019/7/15 9:47
 */
public class CriteriaQueryMysql<T> extends CriteriaQueryJdbc<T> implements CriteriaQuery {


    public CriteriaQueryMysql(Class<T> cls) {
        super(cls);
    }

    public CriteriaQueryMysql(Class<T> cls, T t) {
        super(cls, t);
    }

}

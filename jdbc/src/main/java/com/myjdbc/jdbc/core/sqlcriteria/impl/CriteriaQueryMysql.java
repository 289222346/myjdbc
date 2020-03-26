package com.myjdbc.jdbc.core.sqlcriteria.impl;


import com.myjdbc.jdbc.core.sqlcriteria.CriteriaQuery;

/**
 * 查询条件构造器
 *
 * @author 陈文
 * @Description
 * @date 2019/7/15 9:47
 */
public class CriteriaQueryMysql extends CriteriaQueryBase implements CriteriaQuery {


    public CriteriaQueryMysql(Class cls) {
        super(cls);
    }

    public <T> CriteriaQueryMysql(Class<T> cls, T t) {
        super(cls, t);
    }

}

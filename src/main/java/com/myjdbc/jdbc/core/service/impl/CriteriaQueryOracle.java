package com.myjdbc.jdbc.core.service.impl;


import com.myjdbc.jdbc.core.service.CriteriaQuery;

/**
 * 查询条件构造器
 *
 * @author 陈文
 * @Description
 * @date 2019/7/15 9:47
 */
public class CriteriaQueryOracle extends CriteriaQueryBase implements CriteriaQuery {


    public CriteriaQueryOracle(Class cls) {
        super(cls);
    }

    public <T> CriteriaQueryOracle(Class<T> cls, T t) {
        super(cls, t);
    }

}

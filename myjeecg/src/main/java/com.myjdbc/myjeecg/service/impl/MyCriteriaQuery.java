package com.myjdbc.myjeecg.service.impl;


import com.myjdbc.jdbc.core.sqlcriteria.CriteriaQuery;
import com.myjdbc.jdbc.core.sqlcriteria.impl.CriteriaQueryOracle;

public class MyCriteriaQuery extends CriteriaQueryOracle implements CriteriaQuery {

    public MyCriteriaQuery(Class cls) {
        super(cls);
    }

    public <T> MyCriteriaQuery(Class<T> cls, T t) {
        super(cls, t);
    }
}

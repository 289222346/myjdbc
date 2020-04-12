package com.myjdbc.core.criteria.impl;

import com.myjdbc.core.constants.DBType;
import com.myjdbc.core.criteria.CriteriaQuery;
import com.myjdbc.core.pool.DBconfig;

/**
 * 查询条件构造器
 *
 * @author 陈文
 * @Description
 * @date 2019/7/15 9:47
 */
public class CriteriaQueryFactory<T> {

    private CriteriaQueryFactory() {
    }

    public static CriteriaQuery creatCriteriaQuery(Class cls) {
        if (DBconfig.DBTYPE == DBType.ORACLE) {
            return new CriteriaQueryOracle(cls);
        }
        if (DBconfig.DBTYPE == DBType.MYSQL) {
            return new CriteriaQueryMysql(cls);
        }
        return null;
    }

    public static <T> CriteriaQuery creatCriteriaQuery(Class<T> cls, T t) {
        if (DBconfig.DBTYPE == DBType.ORACLE) {
            return new CriteriaQueryOracle(cls, t);
        }
        if (DBconfig.DBTYPE == DBType.MYSQL) {
            return new CriteriaQueryMysql(cls, t);
        }
        return null;
    }


}

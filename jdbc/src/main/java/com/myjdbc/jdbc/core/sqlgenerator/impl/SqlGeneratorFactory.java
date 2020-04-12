package com.myjdbc.jdbc.core.sqlgenerator.impl;


import com.myjdbc.core.constants.DBType;
import com.myjdbc.core.entity.DeleteEntity;
import com.myjdbc.core.entity.SaveEntity;
import com.myjdbc.core.entity.SavaListEntity;
import com.myjdbc.core.criteria.CriteriaQuery;
import com.myjdbc.jdbc.core.sqlgenerator.SqlGenerator;
import com.myjdbc.core.pool.DBconfig;

import java.io.IOException;
import java.util.List;

/**
 * @Author 陈文
 * @Date 2019/08/12  23:53
 * SQL生成器工厂
 * 使用SQL生成器，只要自动装配时指定sqlGenerator（本类）实现
 * 不用关心到底有多少种生成器，也不用关心到底是使用哪一种生成器
 */

public class SqlGeneratorFactory implements SqlGenerator {

    private SqlGenerator sqlGenerator;

    private SqlGenerator creatSqlGenerator() {
        //配置数据库类别和SQL生成器
        if (DBconfig.DBTYPE == DBType.ORACLE) {
            return new SqlGeneratorOracle();
        }
        if (DBconfig.DBTYPE == DBType.MYSQL) {
            return new SqlGeneratorMysql();
        }
        return new SqlGeneratorMysql();
    }

    private SqlGenerator sql() {
        if (sqlGenerator != null) {
            return sqlGenerator;
        }
        sqlGenerator = creatSqlGenerator();
        return sqlGenerator;
    }

    @Override
    public String getCount(CriteriaQuery criteriaQuery) {
        return sql().getCount(criteriaQuery);
    }

    @Override
    public <T> String findById(Class<T> cls) {
        return sql().findById(cls);
    }

    @Override
    public <T> String findAll(CriteriaQuery criteriaQuery) {
        return sql().findAll(criteriaQuery);
    }

    @Override
    public String findAll(CriteriaQuery criteriaQuery, String sql) {
        return sql().findAll(criteriaQuery, sql);
    }

    @Override
    public <T> DeleteEntity delete(T t) {
        return sql().delete(t);
    }

    @Override
    public <T> SaveEntity save(T po) {
        return sql().save(po);
    }

    @Override
    public <T> SavaListEntity save(List<T> po) {
        return sql().save(po);
    }
}

package com.myjdbc.jdbc.core.sqlgenerator.impl;


import com.myjdbc.jdbc.core.bo.DeleteEntity;
import com.myjdbc.jdbc.core.bo.SavaEntity;
import com.myjdbc.jdbc.core.bo.SavaListEntity;
import com.myjdbc.jdbc.core.service.CriteriaQuery;
import com.myjdbc.jdbc.core.sqlgenerator.SqlGenerator;
import com.myjdbc.jdbc.pool.DBconfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * SQL生成器工厂
 * 使用SQL生成器，只要自动装配时指定sqlGenerator（本类）实现
 * 不用关心到底有多少种生成器，也不用关心到底是使用哪一种生成器
 */
@Service("sqlGenerator")
public class SqlGeneratorFactory implements SqlGenerator {

    @Autowired
    private Map<String, SqlGenerator> map;

    private SqlGenerator sqlGenerator;

    private SqlGenerator creatSqlGenerator() throws IOException {
        //配置数据库类别和SQL生成器
        return map.get(DBconfig.DBTYPE.getGenerator());
    }

    private SqlGenerator sql() {
        if (sqlGenerator != null) {
            return sqlGenerator;
        }
        try {
            sqlGenerator = creatSqlGenerator();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlGenerator;
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

//    @Override
//    public <T> String findAll(Class<T> cls) {
//        return sql().findAll(cls);
//    }

    @Override
    public <T> DeleteEntity delete(T t) {
        return sql().delete(t);
    }

    @Override
    public <T> SavaEntity save(T po) {
        return sql().save(po);
    }

    @Override
    public <T> SavaListEntity save(List<T> po) {
        return sql().save(po);
    }
}

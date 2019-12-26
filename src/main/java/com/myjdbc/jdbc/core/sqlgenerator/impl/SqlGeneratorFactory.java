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
 * 按照分层原则，每一张数据表都有一个Dao与之对应，为此设计此实现类
 * 具体的Dao类只要继承该类，即可拥有最基础的增删该查功能
 * 支持复杂Sql自定义，可选择List<T>、List<Object[]>、List<Map<String,Object>>三种返回形式。
 * 此实现类仅适用于Oracle
 * 以下请注意：*****************************************************
 * 使用本类应该严格按照MyJDBC命名规范
 * Po类名对应数据库表名，例如：LoginLog.java  对应  login_log表
 * Po类中的属性名应该严格对应数据库字段名，例如：userName  对应  user_name
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
    public <T> String findById(Class<T> cls, boolean parentFlag) {
        return sql().findById(cls, parentFlag);
    }

    @Override
    public <T> String findAll(CriteriaQuery criteriaQuery, boolean parentFlag) {
        return sql().findAll(criteriaQuery, parentFlag);
    }

    @Override
    public String findAll(CriteriaQuery criteriaQuery, String sql) {
        return sql().findAll(criteriaQuery, sql);
    }

    @Override
    public <T> String findAll(Class<T> cls, boolean parentFlag) {
        return sql().findAll(cls, parentFlag);
    }

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

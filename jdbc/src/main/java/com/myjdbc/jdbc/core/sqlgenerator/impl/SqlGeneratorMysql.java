package com.myjdbc.jdbc.core.sqlgenerator.impl;


import com.myjdbc.jdbc.core.sqlgenerator.SqlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author 陈文
 * @Date 2019/12/26  12:38
 * @Description Mysql数据库的SQL生成器
 */
@Service("sqlGeneratorMysql")
public class SqlGeneratorMysql extends SqlGeneratorBase implements SqlGenerator {
    private final Logger logger = LoggerFactory.getLogger(SqlGeneratorMysql.class);

    /* 日期转换格式 */
    protected final String dateFormat = "date_format(?, '%Y-%m-%d %H:%i:%s')";

    @Override
    protected String getDateFormat() {
        return dateFormat;
    }

    /**
     * @Author 陈文
     * @Date 2020/3/2  15:51
     * @Description 获取主查询的分页SQL
     */
    @Override
    protected String getValuePagSplice(String sql) {
        StringBuffer pagSql = new StringBuffer("SELECT count(1) FROM (").append(sql).append(") a");
        return pagSql.toString();
    }

    /**
     * @Author 陈文
     * @Date 2020/3/2  15:51
     * @Description 分页的组装拼接
     */
    @Override
    protected StringBuffer pagSplice(String sql, int page, int rows) {
        int end = page * rows;
        int start = end - rows;
        StringBuffer newSql = new StringBuffer(sql).append("  limit ").append(start).append(",").append(end);
        return newSql;
    }

}

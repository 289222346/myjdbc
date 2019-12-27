package com.myjdbc.jdbc.core.sqlgenerator.impl;


import com.myjdbc.jdbc.core.sqlgenerator.SqlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author 陈文
 * @Date 2019/12/26  12:38
 * @Description Oracle数据库的SQL生成器
 */
@Service("sqlGeneratorOracle")
public class SqlGeneratorOracle extends SqlGeneratorBase implements SqlGenerator {
    private static final Logger logger = LoggerFactory.getLogger(SqlGeneratorOracle.class);

    /* 日期转换格式 */
    protected static final String todate = "to_date(?, 'yyyy-MM-dd hh24:mi:ss')";


}

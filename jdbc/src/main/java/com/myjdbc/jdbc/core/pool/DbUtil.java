package com.myjdbc.core.pool;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库配置工具
 *
 * @author 笔落墨成
 * @version 1.0
 */
@Service
public class DbUtil {

    /**
     * @Author 陈文
     * @Date 2019/12/26  11:43
     * @Description 数据库连接池
     */
    private static DataSource dataSource = DataSourceFactory.creatDataSource("Druid");

    /**
     * 新建数据库连接对象
     *
     * @return 数据库连接对象
     */
    public static Connection newConn() {
        try {
            //从连接池取出连接对象
            Connection conn = dataSource.getConnection();
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}

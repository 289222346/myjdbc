package com.myjdbc.jdbc.pool;

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
public class DBUtil {

    /**
     * @Author 陈文
     * @Date 2019/12/26  11:43
     * @Description 数据库连接池
     */
    private static DataSource dataSource = DataSourceFactory.creatDataSource("Druid");

    /**
     * 新建数据库连接对象
     *
     * @return
     */
    public static Connection newConn() {
        try {
            Connection conn = dataSource.getConnection();//从连接池取出连接对象
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}

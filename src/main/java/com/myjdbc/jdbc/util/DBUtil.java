package com.myjdbc.jdbc.util;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connection连接池
 *
 * @author 笔落墨成
 * @version 1.0
 */
@Component
public class DBUtil {

    /* 数据库配置 */
    private static DBconfig dbconfig = new DBconfig();

    public static DBconfig getDbconfig() {
        return dbconfig;
    }

    public static void setDbconfig(DBconfig dbconfig) {
        DBUtil.dbconfig = dbconfig;
    }

    /**
     * 新建数据库连接对象
     *
     * @return
     */
    public static Connection newConn() {
        try {
            try {
                if (dbconfig.driver != null) {
                    Class.forName(dbconfig.driver);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Connection conn = dbconfig.dataSource.getConnection();//从连接池取出连接对象
                    //DriverManager.getConnection(dbconfig.url, dbconfig.username, dbconfig.password);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}

package com.myjdbc.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Connection连接池
 *
 * @author 笔落墨成
 * @version 1.0
 */
@Component
public class DBUtil {

    /* 驱动 */
    private static String driver = "com.mysql.jdbc.Driver";
    /* 数据库URL */
    private static String url = "jdbc:mysql://127.0.0.1:3306/chinaone.xyz?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8";
    /* 用户 */
    private static String username = "root";
    /* 密码 */
    private static String password = "123456";
    /* 最大连接数 */
    private static int maxCount = 100;
    /* 最小连接数 */
    private static int minCount = 10;
    /* 关闭空闲连接扫描时间（分钟） */
    private static int closeTime = 5;
    /* 连接池 */
    private final static List<PoolConnection> connections = new ArrayList<>();

    public static List<PoolConnection> getConnections() {
        return connections;
    }

    @Value("${spring.datasource.driver-class-name:com.mysql.jdbc.Driver}")
    public void setDriver(String driver) {
        this.driver = driver;
    }

    @Value("${spring.datasource.url:jdbc:mysql://127.0.0.1:3306?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8}")
    public void setUrl(String url) {
        this.url = url;
    }

    @Value("${spring.datasource.username:root}")
    public void setUsername(String username) {
        this.username = username;
    }

    @Value("${spring.datasource.password:root}")
    public void setPassword(String password) {
        this.password = password;
    }

    @Value("${dbutil.maxcount:100}")
    public void setMaxCount(int maxcount) {
        DBUtil.maxCount = maxcount;
    }

    @Value("${dbutil.mincount:10}")
    public void setMinCount(int mincount) {
        DBUtil.minCount = mincount;
        initConnections();
    }

    @Value("${dbutil.closetime:10}")
    public void setCloseTime(int closetime) {
        DBUtil.closeTime = closetime;
    }

    /**
     * 初始化数据库连接池（根据最小连接数新建连接）
     */
    private void initConnections() {
        for (int i = 0; i < DBUtil.minCount; i++) {
            PoolConnection poolConnection = null;
            try {
                poolConnection = new PoolConnection(DriverManager.getConnection(url, username, password));
                connections.add(poolConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 新建数据库连接对象
     *
     * @return
     */
    private static Connection newConn() {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从数据库连接池获取数据库连接,并更新该池
     * 遍历连接对象,如果连接池已到达最大值，则返回null
     * 如果没有达到最大值，但当前可用已满，则新建临时连接
     *
     * @return
     */
    public static PoolConnection getConnection() {
        synchronized (connections) {
            for (PoolConnection pconn : connections) {
                if (!pconn.getFlag()) {
                    pconn.setFlag(true);//占用该连接标致
                    return pconn;
                }
            }
        }
        if (connections.size() < maxCount) {
            //如果在连接池中没有找到可用连接，且连接池没达到最大上线，则新建一个连接放入连接池
            PoolConnection poolConnection = new PoolConnection(newConn(), true);
            connections.add(poolConnection);
            return poolConnection;
        }
        return null;
    }

    /**
     * 定期检查连接池，断开多余空闲超时连接
     */
    @Scheduled(fixedRate = 1000 * 60)
    public static void closeConnection() {
        synchronized (connections) {
            for (int i = 0; i < connections.size() && connections.size() > minCount; i++) {
                PoolConnection pconn = connections.get(i);
                if (!pconn.getFlag()) {
                    pconn.addTimes();
                    if (pconn.getTimes() >= closeTime) {
                        //空闲超时则释放掉该连接
                        try {
                            pconn.getConn().close();
                            connections.remove(i);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}

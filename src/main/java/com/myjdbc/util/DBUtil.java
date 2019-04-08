package com.myjdbc.util;

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

    private final static DBconfig dbconfig = new DBconfig();
    /* 连接池 */
    private final static List<PoolConnection> connections = new ArrayList<>();

    public static List<PoolConnection> getConnections() {
        return connections;
    }

    /**
     * 初始化数据库连接池（根据最小连接数新建连接）
     */
    private void initConnections() {
        for (int i = 0; i < dbconfig.minCount; i++) {
            PoolConnection poolConnection = null;
            try {
                poolConnection = new PoolConnection(DriverManager.getConnection(dbconfig.url, dbconfig.username, dbconfig.password));
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
            Connection conn = DriverManager.getConnection(dbconfig.url, dbconfig.username, dbconfig.password);
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
        if (connections.size() < dbconfig.maxCount) {
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
            for (int i = 0; i < connections.size() && connections.size() > dbconfig.minCount; i++) {
                PoolConnection pconn = connections.get(i);
                if (!pconn.getFlag()) {
                    pconn.addTimes();
                    if (pconn.getTimes() >= dbconfig.closeTime) {
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

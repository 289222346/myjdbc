package com.myjdbc.util;

import java.sql.Connection;

public class PoolConnection {
    private Connection conn = null;//数据库连接
    private boolean flag = false;//连接标志，初始为未连接
    private int times = 0;

    public PoolConnection(Connection conn) {
        this.conn = conn;
    }

    public PoolConnection(Connection conn, boolean flag) {
        this.conn = conn;
        this.flag = flag;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        if (flag) {
            resetTimes();
        }
        this.flag = flag;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void addTimes() {
        times++;
    }

    public void resetTimes() {
        times = 0;
    }
}
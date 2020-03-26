package com.myjdbc.jdbc.core.bo;

public class Pag {
    private int total = 0;//总记录数
    private int page = 1;// 当前页
    private int rows = 10;// 每页显示记录数
    private String sql;//用于保存查询总记录数的SQL

    public Pag(int page, int rows) {
        this.page = page;
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public int getRows() {
        return rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}

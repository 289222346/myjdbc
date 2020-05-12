package com.myjdbc.core.model;

/**
 * 分页辅助实体
 *
 * @author 陈文
 */
public class Pag {

    /**
     * 总记录数
     */
    private long total = 0;
    /**
     * 当前页
     */
    private int page = 1;
    /**
     * 每页显示记录数
     */
    private int rows = 10;

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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}

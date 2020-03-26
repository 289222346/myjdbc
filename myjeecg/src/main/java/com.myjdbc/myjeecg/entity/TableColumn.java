package com.myjdbc.myjeecg.entity;

/**
 * @Author 陈文
 * @Date 2019/12/13  13:07
 * @Description EsayUI-DataGrid封装类
 */
public class TableColumn {

    public TableColumn() {

    }

    public TableColumn(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public TableColumn(String id, Integer width, String title) {
        this.id = id;
        this.width = width;
        this.title = title;
    }

    /**
     * 列ID
     */
    private String id;

    /**
     * 列名称
     */
    private String title;

    /**
     * The width of column. If not defined, the width will auto expand to fit its contents.
     * No width definition will reduce performance.
     * 列的宽度。如果未定义，则宽度将自动展开以适合其内容。没有宽度定义将降低性能。
     */
    private Integer width = 100;

    /**
     * True to hide the column.
     * 如果为True，则隐藏列。
     */
    private boolean hidden;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public Integer getWidth() {
//        return width;
//    }
//
//    public void setWidth(Integer width) {
//        this.width = width;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}

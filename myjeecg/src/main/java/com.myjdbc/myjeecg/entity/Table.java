package com.myjdbc.myjeecg.entity;

import java.util.ArrayList;
import java.util.List;

public class Table {

    public Table(String title) {
        this.title = title;
    }

    /**
     * The column title text.
     * 列标题文本。
     */
    private String title;

    /**
     * 列
     */
    private List<List<TableColumn>> columns = new ArrayList<>();

    /**
     * True to show a row number column.
     * 如果为True，则显示行号列。
     */
    private Boolean rownumbers = false;

    /**
     * True to show a pagination toolbar on datagrid bottom.
     * 如果为True，则在数据网格底部显示分页工具栏。
     */
    private Boolean pagination = false;

    /**
     * True to auto expand/contract the size of the columns to fit the grid width and prevent horizontal scrolling.
     * 如果为True，则自动展开/收缩列的大小以适应网格宽度并防止水平滚动。
     */
    private Boolean fitColumns = false;

    /**
     * True to allow selecting only one row.
     * 如果为True，则只允许选择一行。
     */
    private Boolean singleSelect = false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getRownumbers() {
        return rownumbers;
    }

    public void setRownumbers(Boolean rownumbers) {
        this.rownumbers = rownumbers;
    }

    public Boolean getPagination() {
        return pagination;
    }

    public void setPagination(Boolean pagination) {
        this.pagination = pagination;
    }

    public Boolean getFitColumns() {
        return fitColumns;
    }

    public void setFitColumns(Boolean fitColumns) {
        this.fitColumns = fitColumns;
    }

    public Boolean getSingleSelect() {
        return singleSelect;
    }

    public void setSingleSelect(Boolean singleSelect) {
        this.singleSelect = singleSelect;
    }

    public List<List<TableColumn>> getColumns() {
        return columns;
    }

    public void setColumns(List<List<TableColumn>> columns) {
        this.columns = columns;
    }
}
